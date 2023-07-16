package com.luba.tokenizer.service.impl;

import com.luba.tokenizer.service.BPEDataProvider;
import com.luba.tokenizer.service.TokenizationService;
import jakarta.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenizationServiceImpl implements TokenizationService {


    private final BPEDataProvider bpeDataProvider;
    private Map<String, Integer> vocab;
    private Map<List<String>, Integer> bpeRanks = new HashMap<>();
    private Map<Integer, String> byteToUnicode = new HashMap<>();


    @PostConstruct
    void init() {
        this.vocab = this.bpeDataProvider.getVocab();
        this.byteToUnicode = this.bpeDataProvider.getBytesToUnicode();
        this.bpeRanks = this.bpeDataProvider.getBpeRanks();
    }

    @Override
    public Integer getTokenCount(String text) {
        List<Integer> tokens = encode(text);
        return tokens.size();
    }

    private List<Integer> encode(String text) {
        List<String> tokens = parseParts(text);
        List<Integer> newTokens = new ArrayList<>();
        tokens.forEach((token) -> {
            byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
            var buffer = ByteBuffer.wrap(tokenBytes);
            var newToken = Stream
                .generate(buffer::get)
                .limit(buffer.capacity())
                .map(Byte::toUnsignedInt)
                .map(b -> byteToUnicode.get(b))
                .collect(Collectors.joining());
            var bpeResult = bpe(newToken).split(" ");
            newTokens.addAll(Arrays.stream(bpeResult).map((s) -> this.vocab.get(s)).toList());
        });

        return newTokens;
    }

    private List<String> parseParts(String text) {
        Pattern pat = Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+", Pattern.UNICODE_CHARACTER_CLASS);
        var result = pat.matcher(text);
        return result.results().map(MatchResult::group).collect(Collectors.toList());
    }

    private String bpe(String token) {
        List<String> word = new ArrayList<>(Arrays.asList(token.split("")));

        Set<List<String>> pairs = getPairs(word);

        if (pairs.isEmpty()) {
            return token;
        }

        while (true) {
            Map<Integer, List<String>> minPairs = new TreeMap<>();
            for (List<String> pair : pairs) {
                Integer rank = bpeRanks.getOrDefault(pair, Integer.MAX_VALUE);
                minPairs.put(rank, pair);
            }
            List<String> bigram = minPairs.get(Collections.min(minPairs.keySet()));

            if (!bpeRanks.containsKey(bigram)) {
                break;
            }
            String first = bigram.get(0);
            String second = bigram.get(1);
            List<String> newWord = new ArrayList<>();
            int i = 0;

            while (i < word.size()) {
                int j = word.subList(i, word.size()).indexOf(first);
                if (j == -1) {
                    newWord.addAll(word.subList(i, word.size()));
                    break;
                }
                newWord.addAll(word.subList(i, i + j));
                i += j;

                if (word.get(i).equals(first) && i < word.size() - 1 && word.get(i + 1).equals(second)) {
                    newWord.add(first + second);
                    i += 2;
                } else {
                    newWord.add(word.get(i));
                    i++;
                }
            }
            word = newWord;
            if (word.size() == 1) {
                break;
            } else {
                pairs = getPairs(word);
            }
        }
        return String.join(" ", word);
    }

    private Set<List<String>> getPairs(List<String> word) {
        Set<List<String>> pairs = new HashSet<>();
        Iterator<String> iterator = word.iterator();
        String prevChar = iterator.next();

        while (iterator.hasNext()) {
            String chara = iterator.next();
            pairs.add(Arrays.asList(prevChar, chara));
            prevChar = chara;
        }

        return pairs;
    }
}

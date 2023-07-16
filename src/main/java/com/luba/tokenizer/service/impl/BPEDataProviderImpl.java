package com.luba.tokenizer.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.luba.tokenizer.service.BPEDataProvider;
import jakarta.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BPEDataProviderImpl implements BPEDataProvider {

    private final ResourceLoader resourceLoader;

    @Getter
    private List<String> merges;
    @Getter
    private Map<String, Integer> vocab;
    @Getter
    private Map<Integer, String> bytesToUnicode;
    @Getter
    private Map<List<String>, Integer> bpeRanks;

    @PostConstruct
    void init() {
        String VOCABULARY_PATH = "classpath:bpe/merges.txt";
        Resource vocabularyResource = this.resourceLoader.getResource(VOCABULARY_PATH);
        String vocab = getContentAsString(vocabularyResource);
        this.merges = Arrays.stream(vocab.split("\n")).toList();

        String ENCODING_PATH = "classpath:bpe/encoding.json";
        Resource encodingResource = this.resourceLoader.getResource(ENCODING_PATH);
        JSONObject content = getJSONObject(encodingResource);
        this.vocab = content.toMap().entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> (Integer) e.getValue()));

        this.bytesToUnicode = generateBytesToUnicode();

        this.bpeRanks = generateBpeRanks();
    }

    private String getContentAsString(Resource resource) {
        String result = null;
        try {
            resource.getContentAsByteArray();
            result = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Failed to load content from {} to memory" + resource.getFilename());
        }
        return result;
    }

    private JSONObject getJSONObject(Resource resource) {
        JSONObject result = null;
        try {
            result = new JSONObject(new String(resource.getContentAsByteArray(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Failed to load content from {} to memory" + resource.getFilename());
        }
        return result;
    }

    private Map<Integer, String> generateBytesToUnicode() {
        List<Integer> bs = new ArrayList<>();
        bs.addAll(range('!', '~' + 1));
        bs.addAll(range('¡', '¬' + 1));
        bs.addAll(range('®', 'ÿ' + 1));

        List<Integer> cs = new ArrayList<>(bs);
        int n = 0;

        for (int b = 0; b < Math.pow(2, 8); b++) {
            if (!bs.contains(b)) {
                bs.add(b);
                cs.add((int) (Math.pow(2, 8) + n));
                n = n + 1;
            }
        }


        List<String> csString = cs.stream().map(c -> new String(ByteBuffer.allocate(4).putInt(c).array(), Charset.forName("UTF-32"))).toList();

        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < bs.size(); i++) {
            result.put(bs.get(i), csString.get(i));
        }
        return result;
    }

    private ArrayList<Integer> range(int start, int end) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(i);
        }
        return result;
    }

    private Map<List<String>, Integer> generateBpeRanks() {
        List<List<String>> bpeMerges = new ArrayList<>();

        for(int i = 1; i < merges.size() - 1; i++) {
            List<String> parts = List.of(merges.get(i).split("\\s+"));
            if(parts.size() > 0 && !parts.get(0).trim().isEmpty()) {
                bpeMerges.add(parts);
            }
        }

        Map<List<String>, Integer> bpeRanks = new HashMap<>();
        for (int i = 0; i < bpeMerges.size(); i++) {
            bpeRanks.put(bpeMerges.get(i), i);
        }
        return bpeRanks;
    }

}

package com.luba.tokenizer.service;

import java.util.List;
import java.util.Map;

public interface BPEDataProvider {

    Map<Integer, String> getBytesToUnicode();

    Map<String, Integer> getVocab();

    Map<List<String>, Integer> getBpeRanks();

}

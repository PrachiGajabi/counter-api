package com.example.wordsearch.service;

import java.util.List;
import java.util.Map;

public interface ParagraphProcessingService<E> {
	
	Map<String, Integer> wordsCountFromPara(final List<String> words);
	
	List<Map.Entry<String, Integer>> getTopCount(final int top);

}

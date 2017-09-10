package com.example.wordsearch.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.wordsearch.utils.WordComparator;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

@Service
public class ParagraphProcessingServiceImpl<E> implements ParagraphProcessingService<E> {

	final static private String EXEMPTED_CHARS = ".,'\"";
	final static private String PARAGRAPH_FILE_NAME = "paragraph.txt";

	final static private Multiset<String> wordCountSet = HashMultiset.create();
	static private List<Entry<String>> wordCountSetSorted;

	static {
		File fileTORead = new File(
				ParagraphProcessingServiceImpl.class.getClassLoader().getResource(PARAGRAPH_FILE_NAME).getFile());

		Scanner scanner = null;
		try {
			scanner = new Scanner(fileTORead);
		} catch (FileNotFoundException e) {
			System.err.println("Error: paragraph.txt file is not available on classpath");
		}
		while (scanner.hasNext()) {
			wordCountSet.add(StringUtils.deleteAny(scanner.next().toLowerCase(), EXEMPTED_CHARS));
		}
		wordCountSetSorted = getEntriesSortedByFrequency(wordCountSet);
	}

	@Override
	public Map<String, Integer> wordsCountFromPara(final List<String> words) {
		Map<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
		for (String word : words) {
			int wordCount = getcount(word.toLowerCase());
			wordMap.put(word, wordCount);
		}
		return wordMap;
	}

	@Override
	public List<Map.Entry<String, Integer>> getTopCount(int top) {
		top = top > wordCountSetSorted.size() ? wordCountSetSorted.size() : top;
		List<Entry<String>> topList = wordCountSetSorted.subList(0, top);
		Map<String, Integer> topMap = topList.stream().collect(Collectors.toMap(Entry::getElement, Entry::getCount));
		return getSortedMapByValue(topMap);
	}

	private int getcount(final String word) {
		return wordCountSet.count(word);
	}

	private static <E> List<Entry<String>> getEntriesSortedByFrequency(Multiset<String> setToSort) {
		final List<Entry<String>> entryList = Lists.newArrayList(setToSort.entrySet());
		Collections.sort(entryList, new WordComparator());
		return entryList;
	}

	private List<Map.Entry<String, Integer>> getSortedMapByValue(Map<String, Integer> mapToSort) {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(mapToSort.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				if (o1.getValue() == null && o2.getValue() == null)
					return 0;
				if (o1.getValue() == null)
					return -1; // Nulls last
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		return entries;
	}
}

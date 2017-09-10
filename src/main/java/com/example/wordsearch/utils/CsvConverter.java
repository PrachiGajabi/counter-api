package com.example.wordsearch.utils;

import java.util.List;
import java.util.Map;

public class CsvConverter {
	
	private static final String FIELD_SEPARATOR = "|";
	private static final String LINE_SEPARATOR = System.lineSeparator();

	public static String getCsvValues(List<Map.Entry<String, Integer>> entryMap) {
		StringBuilder csvString = new StringBuilder();
		for (Map.Entry<String, Integer> entry : entryMap) {
			csvString = csvString.append(entry.getKey()).append(FIELD_SEPARATOR).append(entry.getValue()).append(LINE_SEPARATOR);
		}
		return csvString.toString().trim();
	}

}

package com.example.wordsearch.utils;

import java.util.Comparator;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public class WordComparator implements Comparator<Multiset.Entry<String>> {

	@Override
	public int compare(Entry<String> x, Entry<String> y) {
		return Integer.compare(y.getCount(), x.getCount());
	}

}

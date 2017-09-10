package com.example.wordsearch.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.wordsearch.model.WordWrapper;
import com.example.wordsearch.service.ParagraphProcessingService;
import com.example.wordsearch.utils.CsvConverter;

@RestController
public class SearchTextRestController {

	ParagraphProcessingService<Map<String, Integer>> paragraphProcessingService;
	
	@Autowired
	public SearchTextRestController(ParagraphProcessingService<Map<String, Integer>> paragraphProcessingService) {
		this.paragraphProcessingService = paragraphProcessingService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Map<String, Integer>> getWordAndCount(@RequestBody WordWrapper searchText) {
		Map<String, Integer> listOfWords = new HashMap<>();
		listOfWords = paragraphProcessingService.wordsCountFromPara(searchText.getSearchText());
		Map<String, Map<String, Integer>> finalresult = new HashMap<>();
		finalresult.put("counts", listOfWords);
		return finalresult;
	}

	@RequestMapping(value = "/top/{topCount}", method = RequestMethod.GET, produces = "text/csv")
	public String getTopCountWords(@PathVariable("topCount") final int topCount) {
		if (topCount < 0) {
			return "Please input positive number";
		}
		return CsvConverter.getCsvValues(paragraphProcessingService.getTopCount(topCount));
	}
}

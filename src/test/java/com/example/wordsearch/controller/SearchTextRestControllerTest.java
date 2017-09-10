package com.example.wordsearch.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.wordsearch.WordsearchApplication;
import com.example.wordsearch.service.ParagraphProcessingService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WordsearchApplication.class)
@WebAppConfiguration
public class SearchTextRestControllerTest {

	private MockMvc mockMvc;

	@Mock
	ParagraphProcessingService<Map<String, Integer>> mockService;

	@Before
	public void setup() throws Exception {
		SearchTextRestController instance = new SearchTextRestController(mockService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(instance).build();
	}

	@Test
	public void shouldReturnTopCountWords() throws Exception {
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put("testA", 10);
		map.put("testB", 8);
		List<Map.Entry<String, Integer>> listCount = new ArrayList<Map.Entry<String, Integer>>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			listCount.add(entry);
		}

		String expectedResult = "testA|10"+System.lineSeparator()+"testB|8";
		when(mockService.getTopCount(2)).thenReturn(listCount);
		
		mockMvc.perform(get("/top/2")).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("text/csv"))
				.andExpect(content().string(expectedResult));
		verify(mockService, times(1)).getTopCount(2);
		verifyNoMoreInteractions(mockService);
	}
}

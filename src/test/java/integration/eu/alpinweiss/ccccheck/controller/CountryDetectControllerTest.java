package integration.eu.alpinweiss.ccccheck.controller;

import eu.alpinweiss.ccccheck.service.PhoneNumberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CountryDetectControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private PhoneNumberService phoneNumberService;

	@Test
	public void testEmptyNumber() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/detect/country/by/number/"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}

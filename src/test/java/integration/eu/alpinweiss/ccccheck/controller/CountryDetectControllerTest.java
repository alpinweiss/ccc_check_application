package integration.eu.alpinweiss.ccccheck.controller;

import eu.alpinweiss.ccccheck.CccChechkApplication;
import eu.alpinweiss.ccccheck.service.PhoneNumberService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CccChechkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CountryDetectControllerTest {

	public static final String PAGE_URL_TEMPLATE = "/detect/country/by/number/";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PhoneNumberService phoneNumberService;

	@Test
	public void testEmptyNumber() throws Exception {
		Mockito.when(phoneNumberService.getCountryName(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get(PAGE_URL_TEMPLATE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testThrowErrorOnText() throws Exception {
		Mockito.when(phoneNumberService.getCountryName(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get(PAGE_URL_TEMPLATE + "+12asd"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testThrowErrorOnNotFoundCountry() throws Exception {
		Mockito.when(phoneNumberService.getCountryName(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get(PAGE_URL_TEMPLATE + "+1234"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testReturnOneCountry() throws Exception {
		String number = "+34 324 2342";
		String countryName = "Test";
		Set<String> countrySet = new HashSet<>(Arrays.asList(countryName));
		Mockito.when(phoneNumberService.getCountryName(number.replace(" ", ""))).thenReturn(countrySet);
		mockMvc.perform(MockMvcRequestBuilders.get(PAGE_URL_TEMPLATE + number))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.country", Matchers.is(countryName)));
	}

	@Test
	public void testReturnTwoCountry() throws Exception {
		String number = "00343242342";
		String countryName1 = "Test1";
		String countryName2 = "Test2";
		Set<String> countrySet = new HashSet<>(Arrays.asList(countryName1, countryName2));
		Mockito.when(phoneNumberService.getCountryName(number.replace(" ", ""))).thenReturn(countrySet);
		mockMvc.perform(MockMvcRequestBuilders.get(PAGE_URL_TEMPLATE + number))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.country", Matchers.is(countryName1 + " or " + countryName2)));
	}
}

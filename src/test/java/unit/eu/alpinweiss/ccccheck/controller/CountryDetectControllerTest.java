package unit.eu.alpinweiss.ccccheck.controller;

import eu.alpinweiss.ccccheck.controller.CountryDetectController;
import eu.alpinweiss.ccccheck.service.PhoneNumberService;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class CountryDetectControllerTest {

	@Mock
	private PhoneNumberService phoneNumberService;

	@InjectMocks
	private CountryDetectController countryDetectController;

	@Test
	public void testNullNumber() {
		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber(null);

		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testNumberIsNotProvided() {
		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber("");

		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testNumberWithoutPrefix() {
		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber("123");

		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testIOException() throws IOException {
		Mockito.when(phoneNumberService.getCountryName(Mockito.anyString())).thenThrow(new IOException());
		JSONObject responseBody = new JSONObject();
		responseBody.put("message", "Can't fetch phone numbers from wiki!");
		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber("+123");

		checkResponse(HttpStatus.INTERNAL_SERVER_ERROR, responseBody, response);
	}

	@Test
	public void testOneCountry() throws IOException {
		String number = "+1234567";
		Set<String> countrySet = new HashSet<>(Arrays.asList("Asd"));
		JSONObject responseBody = new JSONObject();
		responseBody.put("country", String.join(" or ", countrySet));
		Mockito.when(phoneNumberService.getCountryName(number)).thenReturn(countrySet);

		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber(number);

		checkResponse(HttpStatus.OK, responseBody, response);
	}

	@Test
	public void testTwoCountry() throws IOException {
		String number = "+834643";
		Set<String> countrySet = new HashSet<>(Arrays.asList("Asd", "KJhg"));
		JSONObject responseBody = new JSONObject();
		responseBody.put("country", String.join(" or ", countrySet));
		Mockito.when(phoneNumberService.getCountryName(number)).thenReturn(countrySet);

		ResponseEntity<JSONObject> response = countryDetectController.detectCountryByPhoneNumber(number);

		checkResponse(HttpStatus.OK, responseBody, response);
	}

	public void checkResponse(HttpStatus status, JSONObject expectedJson, ResponseEntity<JSONObject> response) {
		Assert.assertEquals(status, response.getStatusCode());
		Assert.assertEquals(expectedJson, response.getBody());
	}
}

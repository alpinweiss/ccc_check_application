package eu.alpinweiss.ccccheck.controller;

import eu.alpinweiss.ccccheck.service.PhoneNumberService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
public class CountryDetectController {

	private final static String EXCEPTION_ERROR_MESSAGE = "Can't fetch phone numbers from wiki!";

	@Autowired
	private PhoneNumberService phoneNumberService;

	@RequestMapping(value = "/detect/country/by/number/{phoneNumber}", produces = "application/json")
	public ResponseEntity<JSONObject> detectCountryByPhoneNumber(@PathVariable String phoneNumber) {
		JSONObject response = new JSONObject();
		if (isNumberValid(phoneNumber)) {
			return ResponseEntity.badRequest().body(response);
		}

		try {
			Set<String> countrySet = phoneNumberService.getCountryName(phoneNumber.replaceAll(" ", ""));
			response.put("country", String.join(" or ", countrySet));
			return ResponseEntity.ok(response);
		} catch (IOException ioe) {
			response.put("message", EXCEPTION_ERROR_MESSAGE);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private boolean isNumberValid(String number) {
		return StringUtils.isEmpty(number) || !number.startsWith("+") && !number.startsWith("00");
	}
}

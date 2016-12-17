package eu.alpinweiss.ccccheck.phonenumber;

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

	@Autowired
	private PhoneNumberService phoneNumberService;

	@RequestMapping(value = "/detect/country/by/number/{phoneNumber}", produces = "application/json")
	public ResponseEntity<String> detectCountryByPhoneNumber(@PathVariable String phoneNumber) {
		if (StringUtils.isEmpty(phoneNumber) || !phoneNumber.startsWith("+") && !phoneNumber.startsWith("00")) {
			return ResponseEntity.badRequest().body(null);
		}
		try {
			Set<String> countrySet = phoneNumberService.getCountryName(phoneNumber.replaceAll(" ", ""));
			return ResponseEntity.ok(String.join(" or ", countrySet));
		} catch (IOException ioe) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't fetch phone numbers from wiki!");
		}
	}
}

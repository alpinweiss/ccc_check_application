package eu.alpinweiss.ccccheck;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryDetectController {

	@RequestMapping(value = "/detect/country/by/number/{phoneNumber}")
	public void detectCountryByPhoneNumber(String phoneNumber) {

	}
}

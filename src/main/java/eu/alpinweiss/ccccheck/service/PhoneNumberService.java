package eu.alpinweiss.ccccheck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class PhoneNumberService {

	@Autowired
	private WikiCountryCodeParserService wikiCountryCodeParserService;

	public Set<String> getCountryName(String number) throws IOException {
		Map<String, Set<String>> countryByPrefixMap = wikiCountryCodeParserService.getCountryCodes();

		String prefix = "+";
		int offset = getOffsetByNumber(number);
		Set<String> countrySet = null;
		for (int i = offset; i < number.length(); i++) {
			prefix += number.charAt(i);
			if (countryByPrefixMap.containsKey(prefix)) {
				countrySet = countryByPrefixMap.get(prefix);
			}
		}
		return countrySet;
	}

	public int getOffsetByNumber(String number) {
		int offset = 0;
		if (number.startsWith("+")) {
			offset = 1;
		} else if (number.startsWith("00")) {
			offset = 2;
		}
		return offset;
	}
}

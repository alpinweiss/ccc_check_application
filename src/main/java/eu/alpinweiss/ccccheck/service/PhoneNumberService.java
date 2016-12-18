package eu.alpinweiss.ccccheck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class PhoneNumberService {

	private final static long CACHE_TIME = TimeUnit.HOURS.toMillis(3);

	@Autowired
	private WikiCountryCodeParserService wikiCountryCodeParserService;

	private Map<String, Set<String>> countryByPrefixMap = new HashMap<>();
	private long lastFetchTime;

	public Set<String> getCountryName(String number) throws IOException {
		if (countryByPrefixMap.isEmpty() || isTimeToRefresh()) {
			countryByPrefixMap.putAll(wikiCountryCodeParserService.getCountryCodes());
			lastFetchTime = System.currentTimeMillis();
		}

		Assert.notNull(number);
		String prefix = "+";
		int offset = 0;
		if (number.startsWith("+")) {
			offset = 1;
		} else if (number.startsWith("00")) {
			offset = 2;
		}
		Set<String> countrySet = null;
		for (int i = offset; i < number.length(); i++) {
			prefix += number.charAt(i);
			if (countryByPrefixMap.containsKey(prefix)) {
				countrySet = countryByPrefixMap.get(prefix);
			}
		}
		return countrySet;
	}

	private boolean isTimeToRefresh() {
		return lastFetchTime + CACHE_TIME < System.currentTimeMillis();
	}
}

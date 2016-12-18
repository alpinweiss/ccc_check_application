package unit.eu.alpinweiss.ccccheck.service;

import eu.alpinweiss.ccccheck.service.PhoneNumberService;
import eu.alpinweiss.ccccheck.service.WikiCountryCodeParserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class PhoneNumberServiceTest {

	private final static String LATVIA_NAME = "Latvia";
	private final static String RUSSIA_NAME = "Russia";

	private final static String LATVIAN_PREFIX = "+371";
	private final static String RUSSIAN_PREFIX = "+7";
	private final static String LONG_PREFIX = "99999999";

	private final static int BIG_LIST_SIZE = 15000;

	@Mock
	private WikiCountryCodeParserService wikiCountryCodeParserService;

	@InjectMocks
	private PhoneNumberService service;

	@Test
	public void testEmptyPhoneList() throws IOException {
		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(new HashMap<>());
		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testFindCountryInOneNumberInList() throws IOException {
		Map<String, Set<String>> countryByPrefix = new HashMap<>();
		countryByPrefix.put(LATVIAN_PREFIX, new HashSet<>(Arrays.asList(LATVIA_NAME)));

		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(countryByPrefix);

		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertEquals(1, countrySet.size());
		Assert.assertTrue(countrySet.contains(LATVIA_NAME));
	}

	@Test
	public void testIncorrectPrefixWithOneNumberInList() throws IOException {
		Map<String, Set<String>> countryByPrefix = new HashMap<>();
		countryByPrefix.put(RUSSIAN_PREFIX, new HashSet<>(Arrays.asList(RUSSIA_NAME)));

		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(countryByPrefix);

		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testIncorrectLongPrefixWithOneNumberInList() throws IOException {
		Map<String, Set<String>> countryByPrefix = new HashMap<>();
		countryByPrefix.put(RUSSIAN_PREFIX, new HashSet<>(Arrays.asList(RUSSIA_NAME)));
		countryByPrefix.put(LATVIAN_PREFIX, new HashSet<>(Arrays.asList(LATVIA_NAME)));

		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(countryByPrefix);

		Set<String> countrySet = service.getCountryName(LONG_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testTwoCountrysByOnePrefix() throws IOException {
		Map<String, Set<String>> countryByPrefix = new HashMap<>();
		countryByPrefix.put(LATVIAN_PREFIX, new HashSet<>(Arrays.asList(RUSSIA_NAME, LATVIA_NAME)));

		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(countryByPrefix);

		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertEquals(2, countrySet.size());
		Assert.assertTrue(countrySet.contains(RUSSIA_NAME));
		Assert.assertTrue(countrySet.contains(LATVIA_NAME));
	}

	@Test
	public void testFindCountryInBigList() throws IOException {
		Map<String, Set<String>> countryByPrefix = new HashMap<>();
		for (int i = 0; i < BIG_LIST_SIZE; i++) {
			String prefix = "+" + i;
			countryByPrefix.put(prefix, new HashSet<>(Arrays.asList(prefix)));
		}
		Mockito.when(wikiCountryCodeParserService.getCountryCodes()).thenReturn(countryByPrefix);

		String randomPrefix = "+" + new Random().nextInt(BIG_LIST_SIZE) ;

		Set<String> countrySet = service.getCountryName(randomPrefix);

		Assert.assertEquals(1, countrySet.size());
		Assert.assertTrue(countrySet.contains(randomPrefix));
	}
}

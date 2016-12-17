package eu.alpinweiss.ccccheck.phonenumber;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.Set;

public class PhoneNumberServiceTest {

	private final static String LATVIA_NAME = "Latvia";
	private final static String RUSSIA_NAME = "Russia";

	private final static String LATVIAN_PREFIX = "371";
	private final static String RUSSIAN_PREFIX = "7";
	private final static String LONG_PREFIX = "99999999";

	private final static int BIG_LIST_SIZE = 15000;

	private PhoneNumberService service;

	@Before
	public void init() {
		service = new PhoneNumberService();
	}

	/*@Test
	public void testEmptyPhoneList() {
		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testFindCountryInOneNumberInList() {
		service.put(LATVIAN_PREFIX, LATVIA_NAME);

		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertEquals(1, countrySet.size());
		Assert.assertTrue(countrySet.contains(LATVIA_NAME));
	}

	@Test
	public void testIncorrectPrefixWithOneNumberInList() {
		service.put(RUSSIAN_PREFIX, RUSSIA_NAME);
		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testIncorrectLongPrefixWithOneNumberInList() {
		service.put(RUSSIAN_PREFIX, RUSSIA_NAME);
		service.put(LATVIAN_PREFIX, LATVIA_NAME);
		Set<String> countrySet = service.getCountryName(LONG_PREFIX);

		Assert.assertNull(countrySet);
	}

	@Test
	public void testTwoCountrysByOnePrefix() {
		service.put(LATVIAN_PREFIX, RUSSIA_NAME);
		service.put(LATVIAN_PREFIX, LATVIA_NAME);

		Set<String> countrySet = service.getCountryName(LATVIAN_PREFIX);

		Assert.assertEquals(2, countrySet.size());
		Assert.assertTrue(countrySet.contains(RUSSIA_NAME));
		Assert.assertTrue(countrySet.contains(LATVIA_NAME));
	}

	@Test
	public void testFindCountryInBigList() {
		for (int i = 0; i < BIG_LIST_SIZE; i++) {
			String prefix = i + "";
			service.put(prefix, prefix);
		}
		String randomPrefix = new Random().nextInt(BIG_LIST_SIZE) + "";

		Set<String> countrySet = service.getCountryName(randomPrefix);

		Assert.assertEquals(1, countrySet.size());
		Assert.assertTrue(countrySet.contains(randomPrefix));
	}*/
}

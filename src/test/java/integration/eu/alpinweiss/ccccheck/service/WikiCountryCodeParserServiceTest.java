package integration.eu.alpinweiss.ccccheck.service;

import eu.alpinweiss.ccccheck.CccChechkApplication;
import eu.alpinweiss.ccccheck.service.WikiCountryCodeParserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CccChechkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WikiCountryCodeParserServiceTest {

	private final static String LATVIA_PREFIX = "+371";
	private final static String LATVIA_NAME = "Latvia";

	@Autowired
	private WikiCountryCodeParserService service;

	@Test
	public void testRealDataByCountryPrefix() throws IOException {
		Map<String, Set<String>> countryCodes = service.getCountryCodes();

		Set<String> countryNames = countryCodes.get(LATVIA_PREFIX);

		Assert.assertNotNull(countryNames);
		Assert.assertTrue(countryNames.contains(LATVIA_NAME));
	}
}

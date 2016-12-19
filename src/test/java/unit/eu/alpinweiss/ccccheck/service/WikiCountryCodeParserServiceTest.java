package unit.eu.alpinweiss.ccccheck.service;

import eu.alpinweiss.ccccheck.service.WikiCountryCodeParserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class WikiCountryCodeParserServiceTest {

	private final static String COUNTRY_HEADER = "Country, Territory or Service";
	private final static String CODE_HEADER = "Code";
	private final static List<String> DEFAULT_HEADER_ENTRIES = Arrays.asList(COUNTRY_HEADER, CODE_HEADER);
	private final static String LATVIA_NAME = "Latvia";
	private final static String LATVIA_PREFIX = "+371";

	@InjectMocks
	private WikiCountryCodeParserService service;

	@Test
	public void testValidPage() {
		Map<String, List<String>> countryPrefixMap = new HashMap<>();
		countryPrefixMap.put(LATVIA_NAME, Arrays.asList(LATVIA_PREFIX));
		String wikiPage = getHTMLWikiPage(countryPrefixMap, DEFAULT_HEADER_ENTRIES);

		Document document = Jsoup.parse(wikiPage);
		Map<String, Set<String>> countryMap = service.parseWikiPage(document);


		Assert.assertNotNull(countryMap);
		Assert.assertEquals(1, countryMap.size());

		Set<String> countrySet = countryMap.get(LATVIA_PREFIX);
		Assert.assertNotNull(countrySet);
		Assert.assertEquals(1, countrySet.size());
		Assert.assertTrue(countrySet.contains(LATVIA_NAME));
	}

	@Test
	public void testPageWithoutHeader() {
		Map<String, List<String>> countryPrefixMap = new HashMap<>();
		countryPrefixMap.put(LATVIA_NAME, Arrays.asList(LATVIA_PREFIX));
		String wikiPage = getHTMLWikiPage(countryPrefixMap, null);

		Document document = Jsoup.parse(wikiPage);
		Map<String, Set<String>> countryMap = service.parseWikiPage(document);


		Assert.assertNotNull(countryMap);
		Assert.assertTrue(countryMap.isEmpty());
	}

	@Test
	public void testPageWithoutContent() {
		Map<String, List<String>> countryPrefixMap = new HashMap<>();
		countryPrefixMap.put(LATVIA_NAME, Arrays.asList(LATVIA_PREFIX));
		String wikiPage = getHTMLWikiPage(null, DEFAULT_HEADER_ENTRIES);

		Document document = Jsoup.parse(wikiPage);
		Map<String, Set<String>> countryMap = service.parseWikiPage(document);


		Assert.assertNotNull(countryMap);
		Assert.assertTrue(countryMap.isEmpty());
	}

	@Test
	public void testValidRefreshTime() {
		Assert.assertTrue(service.isTimeToRefresh());
	}

	@Test
	public void testInvalidRefreshTime() {
		ReflectionTestUtils.setField(service, "dataCacheTimeInMilliseconds", 5000);
		ReflectionTestUtils.setField(service, "lastFetchTime", System.currentTimeMillis());
		Assert.assertFalse(service.isTimeToRefresh());
	}

	private String getHTMLWikiPage(Map<String, List<String>> countryPrefixMap, List<String> headerEntries) {
		StringBuilder wikiPage =  new StringBuilder();

		wikiPage
			.append("<!DOCTYPE html>")
			.append("<html lang=\"en\">")
				.append("<body>")
					.append("<table class=\"wikitable\"></table>")
					.append("<table class=\"wikitable\">")
						.append(getWikiCountryHeaderTable(headerEntries))
						.append(getWikiCountryTable(countryPrefixMap))
					.append("</table>")
				.append("</body>")
			.append("</html>");
		return wikiPage.toString();
	}

	private String getWikiCountryHeaderTable(List<String> headerEntries) {
		StringBuilder headerContent =  new StringBuilder();

		if (headerEntries != null) {
			headerContent.append("<tr>");
			for (String entry : headerEntries) {
				headerContent.append("<th>").append(entry).append("</th>");
			}
			headerContent.append("</tr>");
		}

		return headerContent.toString();
	}

	private String getWikiCountryTable(Map<String, List<String>> countryPrefixMap) {
		StringBuilder wikiTableContent =  new StringBuilder();

		if (countryPrefixMap != null) {
			for (Map.Entry<String, List<String>> countryPrefixEntry : countryPrefixMap.entrySet()) {
				wikiTableContent.append("<tr>");
				wikiTableContent.append("<td>").append(countryPrefixEntry.getKey()).append("</td>");
				wikiTableContent.append("<td>");
				if (countryPrefixEntry.getValue() != null) {
					for (String prefix : countryPrefixEntry.getValue()) {
						wikiTableContent.append("<a>").append(prefix).append("</a>");
					}
				}
				wikiTableContent.append("</td>");
				wikiTableContent.append("</tr>");
			}
		}

		return wikiTableContent.toString();
	}
}

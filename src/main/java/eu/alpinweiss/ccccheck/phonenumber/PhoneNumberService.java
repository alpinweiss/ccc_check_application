package eu.alpinweiss.ccccheck.phonenumber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Service
public class PhoneNumberService {

	private final static String COUNTRY_CALLING_CODES_LIST_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";

	private final static String TABLE_CLASS = ".wikitable";
	private final static Integer CODES_TABLE_NUMBER = 1;
	private final static String TABLE_HEAD_COUNTRY_NAME = "Country, Territory or Service";
	private final static String TABLE_HEAD_COUNTRY_CODE_NAME = "Code";

	private Map<String, Set<String>> countryByPrefixMap = new TreeMap<>();

	public void put(String prefix, String country) {
		Assert.notNull(prefix);
		Set<String> countrySet = countryByPrefixMap.get(prefix);
		if (countrySet == null) {
			countrySet = new HashSet<>();
			countryByPrefixMap.put(prefix, countrySet);
		}
		countrySet.add(country);
	}

	public Set<String> getCountryName(String number) throws IOException {
		if (countryByPrefixMap.isEmpty()) {
			loadCountryCodes();
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

	private void loadCountryCodes() throws IOException {
		Document document = Jsoup.connect(COUNTRY_CALLING_CODES_LIST_URL).get();

		Elements tableRows = getTableRows(document);
		int countryNamePosition = -1;
		int codePosition = -1;

		Element headerRow = tableRows.first();
		Elements headerContent = headerRow.select("th");

		for (int i = 0; i < headerContent.size() && (countryNamePosition < 0 || codePosition < 0); i++) {
			Element thElement = headerContent.get(i);
			String thText = thElement.text();
			if (TABLE_HEAD_COUNTRY_NAME.equals(thText)) {
				countryNamePosition = i;
			} else if (TABLE_HEAD_COUNTRY_CODE_NAME.equals(thText)) {
				codePosition = i;
			}
		}

		parseAndSaveCountryCodes(tableRows, countryNamePosition, codePosition);
	}

	private Elements getTableRows(Document document) {
		Elements tableElements = document.select(TABLE_CLASS);
		Element tableCountryCode = tableElements.get(CODES_TABLE_NUMBER);
		return tableCountryCode.select("tr");
	}

	private void parseAndSaveCountryCodes(Elements tableRows, int countryNamePosition, int codePosition) {
		for (int i = 1; i < tableRows.size(); i++) {
			Element row = tableRows.get(i);
			String country = row.select("td:eq(" + countryNamePosition + ")").html();
			String[] codes = row.select("td:eq(" + codePosition + ") > a").html().split("\n");

			for (String codeRaw : codes) {
				for (String code : codeRaw.split(",")) {
					put(code.replaceAll(" ", ""), country);
				}
			}
		}
	}
}

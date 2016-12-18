package eu.alpinweiss.ccccheck.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class WikiCountryCodeParserService {

	private final static String COUNTRY_CALLING_CODES_LIST_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";

	private final static String TABLE_CLASS = ".wikitable";
	private final static Integer CODES_TABLE_NUMBER = 1;
	private final static String TABLE_HEAD_COUNTRY_NAME = "Country, Territory or Service";
	private final static String TABLE_HEAD_COUNTRY_CODE_NAME = "Code";

	public Map<String, Set<String>> getCountryCodes() throws IOException {
		Document document = getPageDocument();

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

		return parseAndSaveCountryCodes(tableRows, countryNamePosition, codePosition);
	}

	private Document getPageDocument() throws IOException {
		return Jsoup.connect(COUNTRY_CALLING_CODES_LIST_URL).get();
	}

	private Elements getTableRows(Document document) {
		Elements tableElements = document.select(TABLE_CLASS);
		Element tableCountryCode = tableElements.get(CODES_TABLE_NUMBER);
		return tableCountryCode.select("tr");
	}

	private Map<String, Set<String>> parseAndSaveCountryCodes(Elements tableRows, int countryNamePosition, int codePosition) {
		Map<String, Set<String>> countryCodes = new HashMap<>();

		if (countryNamePosition >= 0 && codePosition >= 0) {
			for (int i = 1; i < tableRows.size(); i++) {
				Element row = tableRows.get(i);
				String country = row.select("td:eq(" + countryNamePosition + ")").html();
				String[] codes = row.select("td:eq(" + codePosition + ") > a").html().split("\n");


				for (String codeRaw : codes) {
					for (String code : codeRaw.split(",")) {
						String prefix = code.replaceAll(" ", "");
						Set<String> countrySet = countryCodes.get(prefix);
						if (countrySet == null) {
							countrySet = new HashSet<>();
							countryCodes.put(prefix, countrySet);
						}
						countrySet.add(country);
					}
				}
			}
		}

		return countryCodes;
	}
}

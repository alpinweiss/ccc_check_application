package eu.alpinweiss.ccccheck.wiki;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PageParserService {

	private final static String COUNTRY_CALLING_CODES_LIST_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";

	private final static String TABLE_HEAD_TAG_NAME = "thead";
	private final static String TABLE_BODY_TAG_NAME = "tbody";
	private final static String TABLE_CLASS = ".wikitable";
	private final static Integer CODES_TABLE_NUMBER = 1;
	private final static String TABLE_HEAD_COUNTRY_NAME = "Country, Territory or Service";
	private final static String TABLE_HEAD_COUNTRY_CODE_NAME = "Code";

	public void loadPage() throws IOException {
		Document document = Jsoup.connect(COUNTRY_CALLING_CODES_LIST_URL).get();

		Elements tableElements = document.select(TABLE_CLASS);
		Element tableCountryCode = tableElements.get(CODES_TABLE_NUMBER);
		Elements tableRows = tableCountryCode.select("tr");
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

		for (int i = 1; i < tableRows.size(); i++) {
			Element row = tableRows.get(i);
			String country = row.select("td:eq(" + countryNamePosition + ")").html();
			String code = row.select("td:eq(" + codePosition + ") > a").html();
			System.out.println(country + " " + code);
		}
	}

}

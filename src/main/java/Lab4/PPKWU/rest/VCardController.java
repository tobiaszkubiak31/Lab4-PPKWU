package Lab4.PPKWU.rest;

import com.google.gson.Gson;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.StructuredName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VCardController {

	private static final String URL_SERVICE = "https://panoramafirm.pl/szukaj?k=";

	@GetMapping("/VCard")
	public ResponseEntity generateVCardEndpoint(
		@RequestParam(value = "companyName") String companyName,@RequestParam(value = "telephone") String telephone,
		@RequestParam(value = "email") String email,
		@RequestParam(value = "address") String address,
		HttpServletResponse response)
		throws IOException {
		String generatedVCard = generateVCard(companyName,telephone,email,address);
		File file = new File(companyName + ".vcf");
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(generatedVCard.getBytes());
		outputStream.flush();
		outputStream.close();

		Path path = Paths.get(companyName + ".vcf");
		Resource resource = new UrlResource(path.toUri());
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
			.body(resource);
	}

	@GetMapping("/search")
	public String generateSearchHTMLEndpoint(
		@RequestParam(value = "searchKey") String searchKey, HttpServletResponse response)
		throws IOException {
		Document document = getDocumentFromUrl(URL_SERVICE + searchKey);
		List<Contact> contacts = getContactsFromHtml(document);
		String contactsToHtml = convertContactsHtml(contacts);
		return "<html>\n" +"<script> console.log( \"Hello JavaScript!\"); </script>"
			+
			"<header><title>Welcome</title></header>\n" +
			"<body>\n" + contactsToHtml  + "</body></html>";
	}

	private String convertContactsHtml(List<Contact> contacts) {
		String html = "";
		for (Contact contact : contacts) {
			html +=  "<br><br><br>" +
				"<a> name:" + contact.getName() +"</a> \n" +
				"<a> email:" + contact.getEmail() +"</a> \n" +
				"<a> telephone:" + contact.getTelephone() +"</a> \n" +
				"<a> address:" + contact.getAddress() +"</a> \n" +
				"<button> Generate vcard: </button> \n";
		}
		return html;
	}

	public String generateVCard(String companyName, String telephone, String email,
		String address){
		VCard vcard = new VCard();

		StructuredName n = new StructuredName();
		n.setFamily("Doe");
		n.setGiven("Jonathan");
		n.getPrefixes().add("Mr");
		vcard.setStructuredName(n);

		vcard.setFormattedName("John Doe");

		return Ezvcard.write(vcard).version(VCardVersion.V4_0).go();
	}

	private List<Contact> getContactsFromHtml(Document document){
		Gson gsonInstance = new Gson();
		List<Contact> contacts = new ArrayList<Contact>();
		Elements elements = document.select("script");
		List<Element> filteredElements = elements.stream().filter(el -> el.attr("type").equals("application/ld+json")).collect(Collectors.toList());
		for (Element element : filteredElements) {
			if (element.attr("type").equals("application/ld+json")) {
				contacts.add(gsonInstance.fromJson(element.data(), Contact.class));
			}
		}
		return contacts;
	}

	private Document getDocumentFromUrl(String urlhtml) throws IOException {
		URL url = new URL(urlhtml);
		URLConnection connection = url.openConnection();
		StringBuilder fromWebsite = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;

		while ((line = reader.readLine()) != null) {
			fromWebsite.append(line);
		}
		String htmlContent = fromWebsite.toString();
		Document document = Jsoup.parse(htmlContent);
		return document;
	}
}

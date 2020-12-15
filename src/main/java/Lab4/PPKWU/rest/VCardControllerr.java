package Lab4.PPKWU.rest;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.StructuredName;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VCardControllerr {

	@GetMapping("/VCard")
	public String countAllStatisticsEndpoint(
		@RequestParam(value = "searchKey") String searchKey, HttpServletResponse response) {
		return generateVCard();
	}

	public String generateVCard(){
		VCard vcard = new VCard();

		StructuredName n = new StructuredName();
		n.setFamily("Doe");
		n.setGiven("Jonathan");
		n.getPrefixes().add("Mr");
		vcard.setStructuredName(n);

		vcard.setFormattedName("John Doe");

		return Ezvcard.write(vcard).version(VCardVersion.V4_0).go();
	}

}

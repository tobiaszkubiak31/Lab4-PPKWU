package Lab4.PPKWU.rest;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VCardControllerr {

	@GetMapping("/VCard")
	public String countAllStatisticsEndpoint(
		@RequestParam(value = "searchKey") String searchKey, HttpServletResponse response) {
		return searchKey;
	}

}

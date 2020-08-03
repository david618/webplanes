package org.jennings.webplanes;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlanesCsv {

	
	@GetMapping(value="/get-text",
			  produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getText() {
	    return "Hello world";
	}	
	
}

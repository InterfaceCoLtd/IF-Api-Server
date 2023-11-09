package xyz.interfacesejong.interfaceapi.global.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AppleAppSiteAssociation {
    @GetMapping(value = "/.well-known/apple-app-site-association", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource appleAppSiteAssociation(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return new ClassPathResource("static/apple-app-site-association");
    }
}

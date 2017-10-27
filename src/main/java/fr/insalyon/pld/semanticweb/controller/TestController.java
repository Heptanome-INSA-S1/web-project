package fr.insalyon.pld.semanticweb.controller;


import fr.insalyon.pld.semanticweb.tools.URLRequester;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {



    @RequestMapping("/queryGoogle")
    public @ResponseBody
    List<String> queryGoogle(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "offset", defaultValue = "0") String offset
    ) throws IOException, XPathExpressionException {

        URLRequester urlRequester = new URLRequester("https://www.google.fr/search");

        if(query.isEmpty()) {
            return new ArrayList<>();
        }
        urlRequester = urlRequester
                .with("q", query)
                .with("start", offset);
        return urlRequester.retrieveUrls();

    }

    @RequestMapping("/dereference")
    public @ResponseBody
    String dereferenceUrl(
            @RequestParam("url") String url
    ) throws IOException {

        if(url.isEmpty()) {
            return "";
        }
        URLRequester urlRequester = new URLRequester(url);
        return urlRequester.outSource();
    }

}

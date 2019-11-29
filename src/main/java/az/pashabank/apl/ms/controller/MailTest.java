package az.pashabank.apl.ms.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MailTest {
    public static void main (String[] args) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user", "pasha_life_app");
        map.add("pass", "pA5haL!f3");
        map.add("html", "1");
        map.add("message", URLEncoder.encode("Test message body", "UTF-8"));
        map.add("subject", "Subject of the email");
        map.add("to", "sadmur2002@gmail.com");
        map.add("from", "no-reply@pashabank.az");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String result = restTemplate.postForObject("http://192.168.110.101:8080/MailServiceRs/send", request, String.class);
        System.out.println(result);
    }
}

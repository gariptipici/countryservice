package com.garip.countryservice.client;

import com.garip.countryservice.entity.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RestCountriesClient {
    final RestTemplate restTemplate;
    private final String restcountriesUrl;

    public RestCountriesClient(RestTemplateBuilder restTemplateBuilder, @Value("${restcountries.base.url}") String restcountriesUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.restcountriesUrl = restcountriesUrl;
    }

    public Country[] getCountries(String service, Optional<String> serviceParameter, Optional<Map<String, List<String>>> queryParameters, String... fields) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(restcountriesUrl)
                .pathSegment(service + "/");
        serviceParameter.ifPresent(uriComponentsBuilder::path);
        queryParameters.ifPresent(qp -> qp.forEach(uriComponentsBuilder::queryParam));
        UriComponents uri = uriComponentsBuilder.query("fields={keyword}").buildAndExpand(String.join(",", fields));
        return restTemplate.getForObject(uri.toUri(), Country[].class);
    }
}
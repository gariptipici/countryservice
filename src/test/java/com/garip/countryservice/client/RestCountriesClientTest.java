package com.garip.countryservice.client;

import com.garip.countryservice.entity.Country;
import com.garip.countryservice.entity.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

class RestCountriesClientTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestCountriesClient restCountriesClient;

    private String restcountriesUrl = "http://restcountries.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        restCountriesClient = new RestCountriesClient(restTemplateBuilder, restcountriesUrl);
    }

    @Test
    void testGetCountries() {
        Country country1 = new Country("USA", new Name("United States"), "Americas", null, 331000000, 9833517);
        Country country2 = new Country("CHN", new Name("China"), "Asia", null, 1439323776, 9596961);
        Country[] expectedCountries = new Country[]{country1, country2};

        String service = "all";
        Optional<String> serviceParameter = Optional.empty();
        Optional<Map<String, List<String>>> queryParameters = Optional.empty();
        String[] fields = {"cca3", "name", "region", "population", "area"};

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(restcountriesUrl)
                .path(service + "/");
        UriComponents uri = uriComponentsBuilder.query("fields={keyword}")
                .buildAndExpand(String.join(",", fields));

        when(restTemplate.getForObject(uri.toUri(), Country[].class)).thenReturn(expectedCountries);

        Country[] actualCountries = restCountriesClient.getCountries(service, serviceParameter, queryParameters, fields);

        assertArrayEquals(expectedCountries, actualCountries);
    }
}

package com.garip.countryservice.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CountryControllerTestIT {
    private static ClientAndServer mockServer;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    void testGetCountriesByPopulationDensity() throws Exception {
        String responseBody = "[{\"cca3\":\"USA\",\"name\":{\"common\":\"United States\"},\"region\":\"Americas\",\"borders\":[],\"population\":331002651,\"area\":9833520}," +
                "{\"cca3\":\"CAN\",\"name\":{\"common\":\"Canada\"},\"region\":\"Americas\",\"borders\":[],\"population\":37742154,\"area\":9984670}]";
        Map<String, List<String>> queryParams  = new HashMap<>() {{
            put("fields", List.of("cca3,name,population,area,region"));
        }};
        mockResponse(MediaType.APPLICATION_JSON, "GET", "/v3.1/all/", queryParams, HttpStatus.OK, responseBody);

        mockMvc.perform(get("/all/countries/byPopulationDensity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("United States")))
                .andExpect(jsonPath("$[1].name", is("Canada")));
    }

    @Test
    void testGetMostBorderingCountryOfADifferentRegion() throws Exception {
        String countriesResponse = "[{\"cca3\":\"DEU\",\"name\":{\"common\":\"Germany\"},\"region\":\"Europe\",\"borders\":[\"CHE\",\"FRA\"]}," +
                "{\"cca3\":\"CHE\",\"name\":{\"common\":\"Switzerland\"},\"region\":\"Europe\",\"borders\":[\"DEU\",\"FRA\"]}," +
                "{\"cca3\":\"FRA\",\"name\":{\"common\":\"France\"},\"region\":\"Europe\",\"borders\":[\"DEU\",\"CHE\"]}]";

        String bordersResponse = "[{\"cca3\":\"CHE\",\"name\":{\"common\":\"Switzerland\"},\"region\":\"Europe\"}," +
                "{\"cca3\":\"FRA\",\"name\":{\"common\":\"France\"},\"region\":\"Europe\"}]";

        Map<String, List<String>> queryParamsForAll  = new HashMap<>() {{
            put("fields", List.of("cca3,name,region,borders"));
        }};

        Map<String, List<String>> queryParamsForAlpha  = new HashMap<>() {{
            put("fields", List.of("cca3,name,region"));
        }};

        mockResponse(MediaType.APPLICATION_JSON, "GET", "/v3.1/all/", queryParamsForAll, HttpStatus.OK, countriesResponse);
        mockResponse(MediaType.APPLICATION_JSON, "GET", "/v3.1/alpha/", queryParamsForAlpha, HttpStatus.OK, bordersResponse);

        mockMvc.perform(get("/all/countries/mostBorderingCountryOfADifferentRegion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Germany")));
    }

    public static void mockResponse(MediaType mediaType, String method, String path,  Map<String, List<String>> queryParams, HttpStatus status, String responseBody) {
        new MockServerClient("localhost", 1080)
                .when(getRequestDefinition(method, path, queryParams))
                .respond(response()
                        .withStatusCode(status.value())
                        .withContentType(mediaType)
                        .withBody(responseBody));
    }

    private static HttpRequest getRequestDefinition(String method, String path, Map<String, List<String>> queryParams) {
        return request()
                .withMethod(method)
                .withPath(path)
                .withQueryStringParameters(queryParams);
    }
}

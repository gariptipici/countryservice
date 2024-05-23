package com.garip.countryservice.controller;

import com.garip.countryservice.dto.CountryDTO;
import com.garip.countryservice.enums.ServiceType;
import com.garip.countryservice.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CountryControllerTest {

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @Test
    void testGetCountriesByPopulationDensity() throws Exception {
        CountryDTO country1 = CountryDTO.builder().name("United States").area(100).population(10000000).build();//100000
        CountryDTO country2 = CountryDTO.builder().name("China").area(100).population(1000000000).build();//10000000
        CountryDTO country3 = CountryDTO.builder().name("Russia").area(1000).population(10000000).build();//10000

        List<CountryDTO> countries = Arrays.asList(country2, country1, country3);

        when(countryService.getCountriesByPopulationDensity(any(ServiceType.class), any()))
                .thenReturn(countries);

        mockMvc.perform(get("/ALL/countries/byPopulationDensity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(countries.size()))
                .andExpect(jsonPath("$[0].name").value("China"))
                .andExpect(jsonPath("$[1].name").value("United States"))
                .andExpect(jsonPath("$[2].name").value("Russia"));
    }

    @Test
    void testGetCountriesByPopulationDensityThrowsException() throws Exception {
        CountryDTO country1 = CountryDTO.builder().name("United States").area(9833517).population(331000000).build();
        CountryDTO country2 = CountryDTO.builder().name("China").area(9596961).population(1439323776).build();
        CountryDTO country3 = CountryDTO.builder().name("Russia").area(17098242).population(145912025).build();

        List<CountryDTO> countries = Arrays.asList(country2, country1, country3);

        when(countryService.getCountriesByPopulationDensity(any(ServiceType.class), any()))
                .thenReturn(countries);

        mockMvc.perform(get("/xyz/countries/byPopulationDensity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetMostBorderingCountryOfADifferentRegion() throws Exception {
        CountryDTO borderingCountry = CountryDTO.builder().name("Turkey").region("Asia").build();
        CountryDTO country = CountryDTO.builder().name("Greece").region("Europe").borders(Collections.singletonList(borderingCountry)).build();

        when(countryService.getMostBorderingCountryOfADifferentRegion(any(ServiceType.class), any()))
                .thenReturn(country);

        mockMvc.perform(get("/ALL/countries/mostBorderingCountryOfADifferentRegion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Greece"))
                .andExpect(jsonPath("$.region").value("Europe"))
                .andExpect(jsonPath("$.borders.size()").value(1))
                .andExpect(jsonPath("$.borders[0].name").value("Turkey"));
    }
}

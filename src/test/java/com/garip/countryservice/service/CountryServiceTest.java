package com.garip.countryservice.service;

import com.garip.countryservice.client.RestCountriesClient;
import com.garip.countryservice.dto.CountryDTO;
import com.garip.countryservice.entity.Country;
import com.garip.countryservice.entity.Name;
import com.garip.countryservice.enums.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    @Mock
    private RestCountriesClient restCountriesClient;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCountriesByPopulationDensity() {
        Country country1 = new Country("USA", new Name("United States"), "Americas", null, 331000000, 9833517);
        Country country2 = new Country("CHN", new Name("China"), "Asia", null, 1439323776, 9596961);
        Country country3 = new Country("RUS", new Name("Russia"), "Europe", null, 145912025, 17098242);

        when(restCountriesClient.getCountries(anyString(), any(), any(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Country[]{country1, country2, country3});

        List<CountryDTO> result = countryService.getCountriesByPopulationDensity(ServiceType.ALL, Optional.empty());

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("China", result.get(0).getName());
    }

    @Test
    void testGetMostBorderingCountryOfADifferentRegion() {
        Country country1 = new Country("GRE", new Name("Greece"), "Europe", Arrays.asList("TUR", "ALB", "BUL"), 83149300, 357114);
        Country country2 = new Country("FRA", new Name("France"), "Europe", Arrays.asList("BEL", "LUX", "ESP"), 65273511, 551695);
        Country country3 = new Country("ESP", new Name("Spain"), "Europe", Arrays.asList("FRA", "POR"), 38386000, 312696);

        when(restCountriesClient.getCountries(eq("region"), eq(Optional.of("Europe")), eq(Optional.empty()), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Country[]{country1, country2, country3});

        Map<String, List<String>> queryParamsForCountry1  = new HashMap<>() {{
            put("codes", List.of("TUR", "ALB", "BUL"));
        }};
        Map<String, List<String>> queryParamsForCountry2  = new HashMap<>() {{
            put("codes", List.of("BEL", "LUX", "ESP"));
        }};
        Map<String, List<String>> queryParamsForCountry3  = new HashMap<>() {{
            put("codes", List.of("FRA", "POR"));
        }};

        Country borderingCountry1 = new Country("TUR", new Name("Turkey"), "Asia", null, 65273511, 551695);
        Country borderingCountry2 = new Country("ALB", new Name("Albania"), "Europe", null, 38386000, 312696);
        Country borderingCountry3 = new Country("BUL", new Name("Bulgaria"), "Europe", null, 8654622, 41284);
        Country borderingCountry4 = new Country("BEL", new Name("Belgium"), "Europe", null, 46754778, 505992);
        Country borderingCountry5 = new Country("LUX", new Name("Luxembourg"), "Europe", null, 44134693, 603550);
        Country borderingCountry6 = new Country("ESP", new Name("Spain"), "Europe", null, 44134693, 603550);
        Country borderingCountry7 = new Country("FRA", new Name("France"), "Europe", null, 44134693, 603550);
        Country borderingCountry8 = new Country("POR", new Name("Portugal"), "Europe", null, 44134693, 603550);

        when(restCountriesClient.getCountries(eq("alpha"), any(), eq(Optional.of(queryParamsForCountry1)), anyString(), anyString(), anyString()))
                .thenReturn(new Country[]{borderingCountry1, borderingCountry2, borderingCountry3});
        when(restCountriesClient.getCountries(eq("alpha"), any(), eq(Optional.of(queryParamsForCountry2)), anyString(), anyString(), anyString()))
                .thenReturn(new Country[]{borderingCountry4, borderingCountry5, borderingCountry6});
        when(restCountriesClient.getCountries(eq("alpha"), any(), eq(Optional.of(queryParamsForCountry3)), anyString(), anyString(), anyString()))
                .thenReturn(new Country[]{borderingCountry7, borderingCountry8});

        CountryDTO result = countryService.getMostBorderingCountryOfADifferentRegion(ServiceType.REGION, Optional.of("Europe"));

        assertNotNull(result);
        assertEquals("Greece", result.getName());
        assertEquals(1, result.getBorders().size()); // Assuming we find only one country bordering a different region.
    }
}

package com.garip.countryservice.service;

import com.garip.countryservice.client.RestCountriesClient;
import com.garip.countryservice.dto.CountryDTO;
import com.garip.countryservice.entity.Country;
import com.garip.countryservice.enums.ServiceType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountryService {

    public static final String CCA_3 = "cca3";
    public static final String NAME = "name";
    public static final String POPULATION = "population";
    public static final String AREA = "area";
    public static final String REGION = "region";
    public static final String BORDERS = "borders";

    public final RestCountriesClient restCountriesClient;

    public CountryService(RestCountriesClient restCountriesClient) {
        this.restCountriesClient = restCountriesClient;
    }

    public List<CountryDTO> getCountriesByPopulationDensity(ServiceType service, Optional<String> parameter) {
        Country[] countries = restCountriesClient.getCountries(service.value(), parameter, Optional.empty(), CCA_3, NAME, POPULATION, AREA, REGION);

        return Arrays.stream(countries)
                .filter(c -> c.area() != 0)
                .sorted(Comparator.comparing(c -> (double) c.population() / c.area(), Comparator.reverseOrder()))
                .map(c -> CountryDTO.builder()
                        .name(c.name().common())
                        .area(c.area())
                        .population(c.population())
                        .build())
                .toList();
    }

    public CountryDTO getMostBorderingCountryOfADifferentRegion(ServiceType service, Optional<String> parameter) {
        Country[] countries = restCountriesClient.getCountries(service.value(), parameter, Optional.empty(), CCA_3, NAME, REGION, BORDERS);

        List<CountryDTO> countriesBorderingWithDifferentRegions = Arrays.stream(countries)
                .filter(c -> c.borders() != null && !c.borders().isEmpty()) //filter out countries without borders
                .map(c -> {
                    Map<String, List<String>> queryParams = new HashMap<>();
                    queryParams.put("codes", c.borders());
                    Country[] borderCountries = restCountriesClient.getCountries("alpha", Optional.empty(), Optional.of(queryParams), CCA_3, NAME, REGION);

                    List<CountryDTO> borderingCountriesInDifferentRegion = Arrays.stream(borderCountries)
                            .filter(bc -> !bc.region().equals(c.region())) // filter out bordering countries within the same region
                            .map(bc -> CountryDTO.builder()
                                    .name(bc.name().common())
                                    .region(bc.region())
                                    .build())
                            .toList();
                    return CountryDTO.builder()
                            .name(c.name().common())
                            .region(c.region())
                            .borders(borderingCountriesInDifferentRegion)
                            .build();
                }).toList();

        return countriesBorderingWithDifferentRegions.stream()
                .max(Comparator.comparing(c -> c.getBorders().size()))
                .orElse(null);
    }
}
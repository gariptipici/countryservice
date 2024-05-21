package com.garip.countryservice.controller;

import com.garip.countryservice.dto.CountryDTO;
import com.garip.countryservice.enums.ServiceType;
import com.garip.countryservice.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = {"{service}/countries", "{service}/{parameter}/countries"})
public class CountryController {
    public final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("byPopulationDensity")
    public List<CountryDTO> getCountriesByPopulationDensity(@PathVariable("service") ServiceType service, @PathVariable("parameter") Optional<String> parameter) {
        return countryService.getCountriesByPopulationDensity(service, parameter);
    }

    @GetMapping("mostBorderingCountryOfADifferentRegion")
    public CountryDTO getMostBorderingCountryOfADifferentRegion(@PathVariable("service") ServiceType service, @PathVariable("parameter") Optional<String> parameter) {
        return countryService.getMostBorderingCountryOfADifferentRegion(service, parameter);
    }
}

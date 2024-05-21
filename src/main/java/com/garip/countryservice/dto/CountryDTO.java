package com.garip.countryservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDTO {
    String name;
    String region;
    List<CountryDTO> borders;
    Integer population;
    Integer area;
}
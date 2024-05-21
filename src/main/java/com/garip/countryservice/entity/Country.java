package com.garip.countryservice.entity;

import java.util.List;

public record Country(String cca3,
                      Name name,
                      String region,
                      List<String> borders,
                      Integer population,
                      Integer area) {
}

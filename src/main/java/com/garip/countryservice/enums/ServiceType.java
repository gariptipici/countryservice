package com.garip.countryservice.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ServiceType {
    ALL("all"), ALFA("alfa"), REGION("region");

    private final String value;

    public String value() {
        return value;
    }
}

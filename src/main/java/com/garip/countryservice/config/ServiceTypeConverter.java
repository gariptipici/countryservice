package com.garip.countryservice.config;

import com.garip.countryservice.enums.ServiceType;
import org.springframework.core.convert.converter.Converter;

public class ServiceTypeConverter implements Converter<String, ServiceType> {
    @Override
    public ServiceType convert(String source) {
        try {
            return ServiceType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
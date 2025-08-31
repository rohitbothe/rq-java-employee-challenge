package com.reliaquest.api.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * @author Rohit Bothe
 */
public class PrefixStrategy extends PropertyNamingStrategies.NamingBase {
    @Override
    public String translate(String propertyName) {
        if ("id".equals(propertyName)) {
            return propertyName;
        }
        return "employee_" + propertyName;
    }
}

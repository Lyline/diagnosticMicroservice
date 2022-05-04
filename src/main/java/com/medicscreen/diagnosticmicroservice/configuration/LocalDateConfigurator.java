package com.medicscreen.diagnosticmicroservice.configuration;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public interface LocalDateConfigurator {
  LocalDate now();
}

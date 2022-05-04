package com.medicscreen.diagnosticmicroservice.configuration;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LocalDateConfigImpl implements LocalDateConfigurator {
  @Override
  public LocalDate now(){
    return LocalDate.now();
  }
}

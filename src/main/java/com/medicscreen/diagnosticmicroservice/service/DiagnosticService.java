package com.medicscreen.diagnosticmicroservice.service;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient;
import org.springframework.stereotype.Service;

import java.time.Period;

@Service
public class DiagnosticService {

  private final LocalDateConfigurator localDate;

  public DiagnosticService(LocalDateConfigurator localDate) {
    this.localDate = localDate;
  }

  public int calculateAge(Patient patient) {
    return Period.between(patient.getDateOfBirth(), localDate.now()).getYears();
  }
}

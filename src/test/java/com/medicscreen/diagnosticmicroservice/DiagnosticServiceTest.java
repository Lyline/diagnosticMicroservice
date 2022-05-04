package com.medicscreen.diagnosticmicroservice;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigImpl;
import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient.PatientBuilder;
import com.medicscreen.diagnosticmicroservice.service.DiagnosticService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagnosticServiceTest {

  private LocalDateConfigurator localDate= mock(LocalDateConfigImpl.class);
  private DiagnosticService service= new DiagnosticService(localDate);

  @Test
  void givenAPatientWhenCalculateAgeThenReturnAge() {
    //Given
    Patient patient= new PatientBuilder()
        .firstName("John")
        .lastName("Doe")
        .dateOfBirth(LocalDate.of(2000,1,1))
        .gender("M")
        .build();

    when(localDate.now()).thenReturn(LocalDate.of(2020,1,1));

    //When
    int actual= service.calculateAge(patient);

    //Then
    assertThat(actual).isEqualTo(20);
  }


}

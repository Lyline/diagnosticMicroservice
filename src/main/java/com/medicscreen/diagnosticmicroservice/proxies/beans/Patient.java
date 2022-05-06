package com.medicscreen.diagnosticmicroservice.proxies.beans;

import java.time.LocalDate;

public class Patient {

  private LocalDate dateOfBirth;
  private String gender;

  public Patient(){}

  public Patient(PatientBuilder builder) {
    this.dateOfBirth= builder.dateOfBirth;
    this.gender= builder.gender;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public static class PatientBuilder{
    private LocalDate dateOfBirth;
    private String gender;

    public PatientBuilder dateOfBirth(LocalDate dateOfBirth) {
      this.dateOfBirth=dateOfBirth;
      return this;
    }

    public PatientBuilder gender(String gender) {
      this.gender=gender;
      return this;
    }

    public Patient build() {
      return new Patient(this);
    }
  }
}

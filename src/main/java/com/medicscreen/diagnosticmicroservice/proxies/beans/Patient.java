package com.medicscreen.diagnosticmicroservice.proxies.beans;

import java.time.LocalDate;


public class Patient {

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String gender;
  private String address;
  private String phone;

  public Patient(){}

  public Patient(PatientBuilder builder) {
    this.firstName= builder.firstName;
    this.lastName= builder.lastName;
    this.dateOfBirth= builder.dateOfBirth;
    this.gender= builder.gender;
    this.address= builder.address;
    this.phone= builder.phone;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public static class PatientBuilder{
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phone;

    public PatientBuilder firstName(String firstName){
      this.firstName=firstName;
      return this;
    }

    public PatientBuilder lastName(String lastName) {
      this.lastName=lastName;
      return this;
    }

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

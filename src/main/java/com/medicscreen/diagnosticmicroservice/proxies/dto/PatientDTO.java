package com.medicscreen.diagnosticmicroservice.proxies.dto;

public class PatientDTO {
  private Integer id;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String gender;
  private String address;
  private String phone;

  public PatientDTO(){}

  public PatientDTO(Integer id, String firstName, String lastName,
                    String dateOfBirth, String gender, String address, String phone) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
    this.address = address;
    this.phone = phone;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

}

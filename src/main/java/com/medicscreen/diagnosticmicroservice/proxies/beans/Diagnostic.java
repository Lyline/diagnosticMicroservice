package com.medicscreen.diagnosticmicroservice.proxies.beans;

public class Diagnostic {
  private String diagnostic;
  private int age;

  public Diagnostic(int age, String diagnostic) {
    this.age= age;
    this.diagnostic = diagnostic;
  }

  public String getDiagnostic() {
    return diagnostic;
  }

  public int getAge() {
    return age;
  }
}

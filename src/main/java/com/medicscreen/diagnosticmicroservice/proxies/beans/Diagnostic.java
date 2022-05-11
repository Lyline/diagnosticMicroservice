package com.medicscreen.diagnosticmicroservice.proxies.beans;

public class Diagnostic {
  private String diagnostic;

  public Diagnostic(String diagnostic) {
    this.diagnostic = diagnostic;
  }

  public String getDiagnostic() {
    return diagnostic;
  }
}

package com.medicscreen.diagnosticmicroservice.controller;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.NoteProxy;
import com.medicscreen.diagnosticmicroservice.proxies.PatientProxy;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Diagnostic;
import com.medicscreen.diagnosticmicroservice.service.DiagnosticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiagnosticController {

  @Autowired
  private DiagnosticService service;

  private final LocalDateConfigurator localDate;
  private final PatientProxy patientProxy;
  private final NoteProxy noteProxy;

  public DiagnosticController(LocalDateConfigurator localDate, PatientProxy patientProxy, NoteProxy noteProxy) {
    this.localDate = localDate;
    this.patientProxy = patientProxy;
    this.noteProxy = noteProxy;
  }

  @GetMapping("/diagnosticAPI/{id}")
  public ResponseEntity<Diagnostic> getDiagnostic(@PathVariable int id){
    Diagnostic diagnostic=service.generateDiagnostic(id);
    return new ResponseEntity<>(diagnostic, HttpStatus.OK);
  }
}

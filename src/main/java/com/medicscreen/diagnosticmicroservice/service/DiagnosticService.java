package com.medicscreen.diagnosticmicroservice.service;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Note;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DiagnosticService {

  private final LocalDateConfigurator localDate;

  public DiagnosticService(LocalDateConfigurator localDate) {
    this.localDate = localDate;
  }

  public String generateDiagnostic(Patient patient, List<Note> notes) {
    int age= calculateAge(patient);
    int marker= analyzeNote(notes);

    if (marker<=1){
      return "None";
    }

    if (patient.getGender().equals("M")) {
      return diagnosticToMan(age, marker);
    }

    if (patient.getGender().equals("F")){
      return diagnosticToWoman(age, marker);
    }
    return "No result";
  }

  private int calculateAge(Patient patient) {
    return Period.between(patient.getDateOfBirth(), localDate.now()).getYears();
  }

  private int analyzeNote(List<Note> notes) {
    List<String> markers=new ArrayList<>();
    List<String> notesToAnalyze= new ArrayList<>();
    int markerCount=0;

    markers.addAll(List.of("hémoglobine a1c","microalbumine","taille","poids","fumeur","anormal",
        "cholestérol","vertige","rechute","réaction","anticorps"));

    for (Note note:notes){
      String noteLowerCase= note.getNoteContent().toLowerCase(Locale.ROOT);
      notesToAnalyze.add(noteLowerCase);
    }

    for (String marker:markers){
      for (String note:notesToAnalyze){
        boolean markerIsPresent=note.contains(marker);
        if (markerIsPresent){
          markerCount++;
        }
      }
    }

    return markerCount;
  }

  private String diagnosticToWoman(int age, int marker) {
    if (age>30 & marker ==2){
      return "Borderline";
    }
    if (age>30 & marker==6){
      return "In Danger";
    }
    if (age<30 & marker ==4){
      return "In Danger";
    }
    if (age<30 & marker ==7){
      return "Early Onset";
    }
    return null;
  }

  private String diagnosticToMan(int age, int marker) {
    if (age>30 & marker ==2){
      return "Borderline";
    }
    if (age>30 & marker==6){
      return "In Danger";
    }
    if (age<30 & marker ==3){
      return "In Danger";
    }
    if (age<30 & marker == 5){
      return "Early Onset";
    }
    return null;
  }
}

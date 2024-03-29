package com.medicscreen.diagnosticmicroservice.service;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.NoteProxy;
import com.medicscreen.diagnosticmicroservice.proxies.PatientProxy;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Diagnostic;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Note;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient.PatientBuilder;
import com.medicscreen.diagnosticmicroservice.proxies.dto.NoteDTO;
import com.medicscreen.diagnosticmicroservice.proxies.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class DiagnosticService {

  private final LocalDateConfigurator localDate;
  private final PatientProxy patientProxy;
  private final NoteProxy noteProxy;

  public DiagnosticService(LocalDateConfigurator localDate, PatientProxy patientProxy, NoteProxy noteProxy) {
    this.localDate = localDate;
    this.patientProxy = patientProxy;
    this.noteProxy = noteProxy;
  }

  public Diagnostic generateDiagnostic(int patientId) {
    Patient patient= getPatient(patientId);

    if (Objects.nonNull(patient)) {
      List<Note> notes= getNotes(patientId);

      String result = "";

      int age= calculateAge(patient);
      int marker= analyzeNote(notes);

      if(notes.isEmpty() | Objects.isNull(notes)){
        result= "None";
      }

      else if(marker<=1 | age>30 & marker>=6 ){
        result= diagnosticToCommon(age,marker);
      }

      else if (patient.getGender().equals("M")) {
        result= diagnosticToMan(age, marker);
      }

      else if (patient.getGender().equals("F")){
        result= diagnosticToWoman(age, marker);
      }
      return new Diagnostic(age,result);
    }
    return null;
  }

  private Patient getPatient(Integer id){
    PatientDTO patientDTO=patientProxy.getPatientDTO(id);
    if (Objects.nonNull(patientDTO)) {
      return new PatientBuilder()
          .gender(patientDTO.getGender())
          .dateOfBirth(LocalDate.parse(patientDTO.getDateOfBirth()))
          .build();
    }
    return null;
  }

  private List<Note>getNotes(Integer patientId){
    List<Note> notes=new ArrayList<>();
    List<NoteDTO> notesDto= noteProxy.getNoteDto(patientId);

    if (notesDto!=null) {
      for (NoteDTO noteDto:notesDto){
        Note note= new Note(noteDto.getNoteContent());
        notes.add(note);
      }return notes;
    }
    return Collections.emptyList();
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
    String result="";

    if(age<30){
      if (marker>=7){
        result= "Early Onset";
      }
      else if (marker>=4){
        result= "In Danger";
      }
      else if(marker<=3){
        result="Borderline";
      }
    }

    if(age>30){
      if (marker<=5){
        result= "Borderline";
      }
    }
    return result;
  }

  private String diagnosticToMan(int age, int marker) {
    String result="";

    if(age<30){
     if (marker>=5){
        result= "Early Onset";
      }
      else if (marker>=3){
        result= "In Danger";
      }
      else if (marker<=2){
        result="Borderline";
      }
    }

    if (age>30){
      if (marker<=5){
        result= "Borderline";
      }
    }
    return result;
  }

  private String diagnosticToCommon(int age, int marker){
    String result="";

    if (marker<=1){
      result= "None";
    }
    if (age>30){
      if (marker>=8){
      result= "Early Onset";
      }
      else if(marker>=6){
        result="In Danger";
      }
    }
    return result;
  }
}

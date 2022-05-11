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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    List<Note> notes= getNotes(patientId);

    String result = null;

    int age= calculateAge(patient);
    int marker= analyzeNote(notes);

    if(marker<=2 | age>30 & marker==2 | age>30 & marker>=8 ){
      result= diagnosticToCommon(age,marker);
    }

    else if (patient.getGender().equals("M")) {
      result= diagnosticToMan(age, marker);
    }

    else if (patient.getGender().equals("F")){
      result= diagnosticToWoman(age, marker);
    }
    return new Diagnostic(result);
  }

  private Patient getPatient(Integer id){
    PatientDTO patientDTO=patientProxy.getPatientDTO(id);
    return new PatientBuilder()
        .gender(patientDTO.getGender())
        .dateOfBirth(LocalDate.parse(patientDTO.getDateOfBirth()))
        .build();
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
    if (age>30 & marker==6){
      result= "In Danger";
    }
    else if (age<30 & marker ==4){
      result= "In Danger";
    }
    else if (age<30 & marker ==7){
      result= "Early Onset";
    }
    return result;
  }

  private String diagnosticToMan(int age, int marker) {
    String result="";

    if (age>30 & marker==6){
      result= "In Danger";
    }
    else if (age<30 & marker ==3){
      result= "In Danger";
    }
    else if (age<30 & marker == 5){
      result= "Early Onset";
    }
    return result;
  }

  private String diagnosticToCommon(int age, int marker){
    String result=null;
    if (marker<=1){
      result= "None";
    }

    else if (age>30 & marker==2){
      result= "Borderline";
    }

    else if (age>30 & marker>=8){
      result= "Early Onset";
    }
    return result;
  }
}

package com.medicscreen.diagnosticmicroservice;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigImpl;
import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Note;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Note.NoteBuilder;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient;
import com.medicscreen.diagnosticmicroservice.proxies.beans.Patient.PatientBuilder;
import com.medicscreen.diagnosticmicroservice.service.DiagnosticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagnosticServiceTest {

  private LocalDateConfigurator localDate= mock(LocalDateConfigImpl.class);
  private DiagnosticService service= new DiagnosticService(localDate);

  Patient patient= new PatientBuilder()
      .firstName("John")
      .lastName("Doe")
      .dateOfBirth(LocalDate.of(2000,1,1))
      .gender("M")
      .build();

  @BeforeEach
  void setUp() {
    when(localDate.now()).thenReturn(LocalDate.of(2020,1,1));
  }

  /**
   ○ aucun risque (None) - Le dossier du patient ne contient aucune note du
   médecin contenant les déclencheurs (terminologie)
   */
  @Test
  void givenPatientWithoutMarkerWhenGenerateDiagnosticThenReturnNone(){
    //Given
    Patient patient= new PatientBuilder()
        .dateOfBirth(LocalDate.of(2000,1,1))
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of());

    //Then
    assertThat(actual).isEqualTo("None");

  }

  /**
   ○ risque limité (Borderline) - Le dossier du patient contient deux déclencheurs et
   le patient est âgé de plus de 30 ans,
   */
  @Test
  void givenPatient35YearOldWithTwoMarkersWhenGenerateDiagnosticThenReturnBorderline() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("F")
        .dateOfBirth(LocalDate.of(1985,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("fumeur")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("cholestérol")
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("Borderline");
  }

  /**
   ○ danger (In Danger) - Si le patient est un homme et a moins de 30 ans,
   alors trois termes déclencheurs doivent être présents.
   */
  @Test
  void givenPatientMan25YearOldWithThreeMarkersWhenGenerateDiagnosticThenReturnInDanger() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("M")
        .dateOfBirth(LocalDate.of(1995,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("Poids, taille excessive")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("Fumeur")
        .build();

    //When
    String actual= service.generateDiagnostic(patient, List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("In Danger");
  }

  /**
   ○ danger (In Danger) - Si le patient est une femme et a moins de 30 ans, il faudra quatre
   termes déclencheurs.
   */
  @Test
  void givenPatientWoman25YearOldWithFourMarkersWhenGenerateDiagnosticThenReturnInDanger() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("F")
        .dateOfBirth(LocalDate.of(1995,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("Poids, taille excessive")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("Fumeur, vertige")
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("In Danger");
  }

  /**
   ○ danger (In Danger) - Si le patient a plus de 30 ans, alors il en faudra six.
   */
  @Test
  void givenPatient35YearOldWithSixMarkersWhenGenerateDiagnosticThenReturnInDanger() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("M")
        .dateOfBirth(LocalDate.of(1985,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("Poids, taille excessive, microalbumine")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("Fumeur, vertige, rechute")
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("In Danger");
  }

  /**
   ○ apparition précoce (Early onset) - Si le patient est un homme et a moins de 30 ans, alors cinq termes
   déclencheurs sont nécessaires.
   */
  @Test
  void givenPatientMan25YearOldWithFiveMarkersWhenGenerateDiagnosticThenReturnEarlyOnset() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("M")
        .dateOfBirth(LocalDate.of(1995,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("Poids, taille excessive, microalbumine")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("Fumeur, vertige")
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("Early Onset");
  }

  /**
   ○ apparition précoce (Early onset) - Si le patient est une femme et a moins de 30
   ans, il faudra sept termes déclencheurs.
   */
  @Test
  void givenPatientWoman25YearOldWithSevenMarkerWhenGenerateDiagnosticThenReturnEarlyOnset() {
    //Given
    Patient patient= new PatientBuilder()
        .gender("F")
        .dateOfBirth(LocalDate.of(1995,1,1))
        .build();

    Note note= new NoteBuilder()
        .noteContent("Poids, taille excessive, microalbumine")
        .build();

    Note note1= new NoteBuilder()
        .noteContent("Fumeur, vertige, Poids,cholestérol")
        .build();

    //When
    String actual= service.generateDiagnostic(patient,List.of(note1,note));

    //Then
    assertThat(actual).isEqualTo("Early Onset");
  }
}

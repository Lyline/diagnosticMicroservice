package com.medicscreen.diagnosticmicroservice;

import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigImpl;
import com.medicscreen.diagnosticmicroservice.configuration.LocalDateConfigurator;
import com.medicscreen.diagnosticmicroservice.proxies.NoteProxy;
import com.medicscreen.diagnosticmicroservice.proxies.PatientProxy;
import com.medicscreen.diagnosticmicroservice.proxies.dto.NoteDTO;
import com.medicscreen.diagnosticmicroservice.proxies.dto.PatientDTO;
import com.medicscreen.diagnosticmicroservice.service.DiagnosticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagnosticServiceTest {

  private LocalDateConfigurator localDate= mock(LocalDateConfigImpl.class);
  private PatientProxy patientProxy= mock(PatientProxy.class);
  private NoteProxy noteProxy= mock(NoteProxy.class);

  private DiagnosticService service= new DiagnosticService(localDate, patientProxy, noteProxy);

  PatientDTO manLess30YearOld = new PatientDTO(1,"John","Doe",
      "1995-01-01", "M", null,null);

  PatientDTO womanLess30YearOld = new PatientDTO(1,"Jane","Doe",
      "1995-01-01","F", null,null);

  PatientDTO manMore30YearOld = new PatientDTO(1,"John","Doe",
      "1985-01-01","M", null,null);

  PatientDTO womanMore30YearOld = new PatientDTO(1,"Jane","Doe",
      "1985-01-01","F", null,null);

  NoteDTO noteOneMarker= new NoteDTO("2",1,"Rechute");
  NoteDTO noteTwoMarkers= new NoteDTO("1",1,"fumeur, cholestérol");
  NoteDTO noteThreeMarkers= new NoteDTO("3",1,"microalbumine, anormal, anticorps");


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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(manLess30YearOld);

    //When
    String actual= service.generateDiagnostic(1);

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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(womanMore30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteTwoMarkers));

    //When
    String actual= service.generateDiagnostic(1);

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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(manLess30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteOneMarker,noteTwoMarkers));

    //When
    String actual= service.generateDiagnostic(1);

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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(womanLess30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteTwoMarkers,noteTwoMarkers));

    //When
    String actual= service.generateDiagnostic(1);

    //Then
    assertThat(actual).isEqualTo("In Danger");
  }

  /**
   ○ danger (In Danger) - Si le patient a plus de 30 ans, alors il en faudra six.
   */
  @Test
  void givenPatient35YearOldWithSixMarkersWhenGenerateDiagnosticThenReturnInDanger() {
    //Given
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(manMore30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteThreeMarkers,noteThreeMarkers));

    //When
    String actual= service.generateDiagnostic(1);

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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(manLess30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteTwoMarkers,noteThreeMarkers));

    //When
    String actual= service.generateDiagnostic(1);

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
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(womanLess30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteThreeMarkers,noteTwoMarkers,noteTwoMarkers));

    //When
    String actual= service.generateDiagnostic(1);

    //Then
    assertThat(actual).isEqualTo("Early Onset");
  }

  /**
   ○ apparition précoce (Early onset) - Si le patient a plus de 30 ans, alors il en
   faudra huit ou plus.
   */
  @Test
  void givenPatientWoman35YearOldWithNineMarkerWhenGenerateDiagnosticThenReturnEarlyOnset(){
    //Given
    when(patientProxy.getPatientDTO(anyInt())).thenReturn(womanMore30YearOld);
    when(noteProxy.getNoteDto(anyInt())).thenReturn(List.of(noteThreeMarkers,noteThreeMarkers,noteThreeMarkers));

    //When
    String actual= service.generateDiagnostic(1);

    //Then
    assertThat(actual).isEqualTo("Early Onset");
  }
}

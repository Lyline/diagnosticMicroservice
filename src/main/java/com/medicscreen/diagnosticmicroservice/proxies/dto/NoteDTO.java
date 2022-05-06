package com.medicscreen.diagnosticmicroservice.proxies.dto;

public class NoteDTO {
  private String id;
  private Integer patientId;
  private String noteContent;

  public NoteDTO() {}

  public NoteDTO(String id, Integer patientId, String noteContent) {
    this.id = id;
    this.patientId = patientId;
    this.noteContent = noteContent;
  }

  public String getNoteContent() {
    return noteContent;
  }
}

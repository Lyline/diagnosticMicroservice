package com.medicscreen.diagnosticmicroservice.proxies.beans;

public class Note {

  private String noteContent;

  public Note() {}

  public String getNoteContent() {
    return noteContent;
  }

  public Note(String noteContent) {
    this.noteContent = noteContent;
  }

}

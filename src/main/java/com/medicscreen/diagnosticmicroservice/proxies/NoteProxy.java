package com.medicscreen.diagnosticmicroservice.proxies;

import com.medicscreen.diagnosticmicroservice.proxies.dto.NoteDTO;
import feign.Param;
import feign.RequestLine;

import java.util.List;


public interface NoteProxy {

  @RequestLine("GET /patient_notes/{id}")
  List<NoteDTO> getNoteDto(@Param Integer id);
}

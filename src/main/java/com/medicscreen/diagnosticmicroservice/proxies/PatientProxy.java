package com.medicscreen.diagnosticmicroservice.proxies;

import com.medicscreen.diagnosticmicroservice.proxies.dto.PatientDTO;
import feign.Param;
import feign.RequestLine;


public interface PatientProxy {

  @RequestLine("GET /patients/{id}")
  PatientDTO getPatientDTO(@Param("id")int id);
}

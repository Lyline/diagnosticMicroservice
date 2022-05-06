package com.medicscreen.diagnosticmicroservice.configuration;

import com.medicscreen.diagnosticmicroservice.proxies.NoteProxy;
import com.medicscreen.diagnosticmicroservice.proxies.PatientProxy;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiagnosticModule {
  Logger logger= LoggerFactory.getLogger(DiagnosticModule.class);

  @Value("${patientURL}")
  private String patientUrl;

  @Value("${noteURL}")
  private String noteUrl;

  @Bean
  public PatientProxy getPatientService(){
    logger.info("Initializing patientProxy @ "+ patientUrl);
    return Feign.builder().client(new OkHttpClient())
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .target(PatientProxy.class, patientUrl);
  }

  @Bean
  public NoteProxy getNoteService(){
    logger.info("Initializing noteProxy @ "+ noteUrl);
    return Feign.builder().client(new OkHttpClient())
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .target(NoteProxy.class, noteUrl);
  }
}

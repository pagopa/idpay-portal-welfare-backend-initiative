package it.gov.pagopa.initiative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "it.gov.pagopa")
public class InitiativeApplication {

  public static void main(String[] args) {
    SpringApplication.run(InitiativeApplication.class, args);
  }

}


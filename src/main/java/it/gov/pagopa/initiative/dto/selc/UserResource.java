package it.gov.pagopa.initiative.dto.selc;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserResource {

    private UUID id;
    private String name;
    private String surname;
    private String email;
    private List<String> roles;

}

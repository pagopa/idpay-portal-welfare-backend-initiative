package it.gov.pagopa.initiative.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Document("config_mcc")
public class ConfigMcc {
    private String code;

    private String description;
}

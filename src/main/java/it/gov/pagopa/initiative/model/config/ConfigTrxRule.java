package it.gov.pagopa.initiative.model.config;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Document("config_trx_rule")
public class ConfigTrxRule {
    private String code;

    private String description;

    private Boolean enabled;

    private Boolean checked;
}

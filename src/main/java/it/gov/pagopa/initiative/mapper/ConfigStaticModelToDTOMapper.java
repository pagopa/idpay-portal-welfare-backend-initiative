package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.config.ConfigMccDTO;
import it.gov.pagopa.initiative.dto.config.ConfigTrxRuleDTO;
import it.gov.pagopa.initiative.model.config.ConfigMcc;
import it.gov.pagopa.initiative.model.config.ConfigTrxRule;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class ConfigStaticModelToDTOMapper {
    public List<ConfigMccDTO> toMccDTOs(List<ConfigMcc> configMccList) {
        if(CollectionUtils.isEmpty(configMccList))
            return Collections.emptyList();

        return configMccList.stream().map(x -> ConfigMccDTO.builder()
                .code(x.getCode())
                .description(x.getDescription())
                .build()).toList();
    }

    public List<ConfigTrxRuleDTO> toTrxRulesDTOs(List<ConfigTrxRule> configTrxRuleList) {
        if(CollectionUtils.isEmpty(configTrxRuleList))
            return Collections.emptyList();

        return configTrxRuleList.stream().map(x -> ConfigTrxRuleDTO.builder()
                .code(x.getCode())
                .description(x.getDescription())
                .checked(x.getChecked())
                .enabled(x.getEnabled())
                .build()).toList();
    }
}

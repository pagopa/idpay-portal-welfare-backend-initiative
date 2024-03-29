package it.gov.pagopa.initiative.controller;

import it.gov.pagopa.initiative.dto.config.ConfigMccDTO;
import it.gov.pagopa.initiative.dto.config.ConfigTrxRuleDTO;
import it.gov.pagopa.initiative.mapper.ConfigStaticModelToDTOMapper;
import it.gov.pagopa.initiative.service.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConfigApiImpl implements ConfigApi {

    private final ConfigService configService;
    private final ConfigStaticModelToDTOMapper configStaticModelToDTOMapper;

    public ConfigApiImpl(ConfigService configService, ConfigStaticModelToDTOMapper configStaticModelToDTOMapper) {
        this.configService = configService;
        this.configStaticModelToDTOMapper = configStaticModelToDTOMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ConfigMccDTO>> getMccConfig() {

        return ResponseEntity.ok(configStaticModelToDTOMapper.toMccDTOs(configService.findAllMcc()));
    }

    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ConfigTrxRuleDTO>> getTransactionConfigRules() {

        return ResponseEntity.ok(configStaticModelToDTOMapper.toTrxRulesDTOs(configService.findAllTrxRules()));
    }
}
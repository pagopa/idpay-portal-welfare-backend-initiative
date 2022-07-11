package it.gov.pagopa.mapper;

import it.gov.pagopa.dto.*;
import it.gov.pagopa.model.Initiative;
import it.gov.pagopa.model.InitiativeAdditional;
import it.gov.pagopa.model.InitiativeGeneral;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitiativeMapper {

    public InitiativeDTO toDtoOnlyId(Initiative initiative) {
        if (initiative == null) {
            return null;
        }
        InitiativeDTO dto = new InitiativeDTO();
        dto.setInitiativeId(initiative.getInitiativeId());
        return dto;
    }

    public Initiative toInitiativeInfoModel(InitiativeInfoDTO initiativeInfoDto) {
        if (initiativeInfoDto == null) {
            return null;
        }
        InitiativeGeneralDTO generalDTO = initiativeInfoDto.getGeneral();
        Initiative initiative = new Initiative();

        if (generalDTO != null) {
            initiative.setGeneral(InitiativeGeneral.builder().beneficiaryBudget(generalDTO.getBeneficiaryBudget())
                    .beneficiaryKnown(generalDTO.getBeneficiaryKnown())
                    .beneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.valueOf(generalDTO.getBeneficiaryType().name()))
                    .budget(generalDTO.getBudget())
                    .name(generalDTO.getName())
                    .endDate(generalDTO.getEndDate())
                    .startDate(generalDTO.getStartDate())
                    .rankingEndDate(generalDTO.getRankingEndDate())
                    .rankingStartDate(generalDTO.getRankingStartDate()).build());
        }
        InitiativeAdditionalDTO additionalDTO = initiativeInfoDto.getAdditionalInfo();
        if (additionalDTO != null) {
            initiative.setAdditionalInfo(InitiativeAdditional.builder().serviceId(additionalDTO.getServiceId())
                    .argument(additionalDTO.getArgument())
                    .description(additionalDTO.getDescription())
                    .serviceName(additionalDTO.getServiceName()).build());
        }
        return initiative;
    }

    public List<InitiativeSummaryDTO> toInitiativeSummaryDtoList(List<Initiative> initiatives) {
        if(CollectionUtils.isEmpty(initiatives)){
            return Collections.EMPTY_LIST;
        }
        return initiatives.stream().map(x -> InitiativeSummaryDTO.builder()
                .initiativeId(x.getInitiativeId())
                .initiativeName(StringUtils.isNotBlank(x.getInitiativeName()) ?
                        x.getInitiativeName() :
                        x.getAdditionalInfo() != null ?
                                x.getAdditionalInfo().getServiceName()
                                : StringUtils.EMPTY)
                .status(x.getStatus())
                .build()).collect(Collectors.toList());
    }

}

package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InitiativeAdditionalDTOsToIOServiceRequestDTOMapper {

    @Value("${rest-client.backend-io.service.request.departmentName}")
    private String productDepartmentName;
    @Value("${rest-client.backend-io.service.request.isVisible}")
    private Boolean isVisible;

    public ServiceRequestDTO toServicePayloadDTO(InitiativeAdditionalDTO initiativeAdditionalDTO, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        Map<ChannelDTO.TypeEnum, String> channelMap = initiativeAdditionalDTO.getChannels().stream().collect(Collectors.toMap(ChannelDTO::getType, ChannelDTO::getContact));
        ServiceMetadataDTO serviceMetadataDTO = ServiceMetadataDTO.builder()
                .email(channelMap.get(ChannelDTO.TypeEnum.EMAIL))
                .phone(channelMap.get(ChannelDTO.TypeEnum.MOBILE))
                .supportUrl(channelMap.get(ChannelDTO.TypeEnum.WEB))
                .privacyUrl(initiativeAdditionalDTO.getPrivacyLink())
                .tosUrl(initiativeAdditionalDTO.getTcLink())
                .description(initiativeAdditionalDTO.getDescription())
                .scope(initiativeAdditionalDTO.getServiceScope().name())
                .build();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(initiativeAdditionalDTO.getServiceName())
                .departmentName(StringUtils.isNotBlank(initiativeOrganizationInfoDTO.getOrganizationName()) ? initiativeOrganizationInfoDTO.getOrganizationName() : productDepartmentName)
                .organizationName(initiativeOrganizationInfoDTO.getOrganizationName())
                .organizationFiscalCode(initiativeOrganizationInfoDTO.getOrganizationVat())
                .isVisible(isVisible)
                .build();
    }

}

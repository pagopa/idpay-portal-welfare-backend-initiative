package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.model.Channel;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitiativeAdditionalDTOsToIOServiceRequestDTOMapper {

    @Value("${rest-client.backend-io.service.request.departmentName}")
    private String productDepartmentName;
    @Value("${rest-client.backend-io.service.request.isVisible}")
    private Boolean isVisible;

    public ServiceRequestDTO toServiceRequestDTO(InitiativeAdditional initiativeAdditional, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        Map<Channel.TypeEnum, String> channelMap = initiativeAdditional.getChannels().stream().collect(Collectors.toMap(channel -> channel.getType(), Channel::getContact));
        ServiceMetadataDTO serviceMetadataDTO = ServiceMetadataDTO.builder()
                .email(channelMap.get(Channel.TypeEnum.EMAIL))
                .phone(channelMap.get(Channel.TypeEnum.MOBILE))
                .supportUrl(channelMap.get(Channel.TypeEnum.WEB))
                .privacyUrl(initiativeAdditional.getPrivacyLink())
                .tosUrl(initiativeAdditional.getTcLink())
                .description(initiativeAdditional.getDescription())
                .scope(initiativeAdditional.getServiceScope().name())
                .build();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(initiativeAdditional.getServiceName())
                .departmentName(StringUtils.isNotBlank(initiativeOrganizationInfoDTO.getOrganizationName()) ? initiativeOrganizationInfoDTO.getOrganizationName() : productDepartmentName)
                .organizationName(initiativeOrganizationInfoDTO.getOrganizationName())
                .organizationFiscalCode(initiativeOrganizationInfoDTO.getOrganizationVat())
                .isVisible(isVisible)
                .build();
    }

}

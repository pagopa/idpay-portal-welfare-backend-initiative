package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.ChannelDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceMetadataDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class InitiativeAdditionalDTOsToIOServiceRequestDTOMapper {

    @Value("${rest-client.backend-io.service.request.departmentName}")
    private String productDepartmentName;
    @Value("${rest-client.backend-io.service.request.isVisible}")
    private Boolean isVisible;

    public ServiceRequestDTO toServicePayloadDTO(InitiativeAdditionalDTO initiativeAdditionalDTO, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        ChannelDTO channelDTO = Optional.ofNullable(initiativeAdditionalDTO.getChannels()).stream().flatMap(Collection::stream).findFirst().orElseThrow(() -> new IllegalStateException("At least one channel should have been associated"));
        ServiceMetadataDTO serviceMetadataDTO = ServiceMetadataDTO.builder()
                .email(channelDTO.getType() == ChannelDTO.TypeEnum.EMAIL ? channelDTO.getContact() : null)
                .phone(channelDTO.getType() == ChannelDTO.TypeEnum.MOBILE ? channelDTO.getContact() : null)
                .supportUrl(channelDTO.getType() == ChannelDTO.TypeEnum.WEB ? channelDTO.getContact() : null)
                .privacyUrl(initiativeAdditionalDTO.getPrivacyLink())
                .tosUrl(initiativeAdditionalDTO.getTcLink())
                .description(initiativeAdditionalDTO.getDescription())
                .scope(initiativeAdditionalDTO.getServiceScope().name())
                .build();
        return ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(initiativeAdditionalDTO.getServiceName())
                .departmentName(productDepartmentName)
                .organizationName(initiativeOrganizationInfoDTO.getOrganizationName())
                .organizationFiscalCode(initiativeOrganizationInfoDTO.getOrganizationVat())
                .isVisible(isVisible)
                .build();
    }

}

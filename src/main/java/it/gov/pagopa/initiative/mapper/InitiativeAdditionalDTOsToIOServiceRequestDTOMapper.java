package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.InitiativeOrganizationInfoDTO;
import it.gov.pagopa.initiative.dto.io.service.OrganizationDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestMetadataDTO;
import it.gov.pagopa.initiative.model.Channel;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitiativeAdditionalDTOsToIOServiceRequestDTOMapper {

    private final String productDepartmentName;
    private final List<String> authorizedRecipients;

    public InitiativeAdditionalDTOsToIOServiceRequestDTOMapper(
            @Value("${rest-client.backend-io-manage.service.request.departmentName}") String productDepartmentName,
            @Value("${rest-client.backend-io-manage.service.request.authorizedRecipients}") List<String> authorizedRecipients) {
        this.productDepartmentName = productDepartmentName;
        this.authorizedRecipients = authorizedRecipients;
    }

    public ServiceRequestDTO toServiceRequestDTO(InitiativeAdditional initiativeAdditional, InitiativeOrganizationInfoDTO initiativeOrganizationInfoDTO){
        Map<Channel.TypeEnum, String> channelMap = initiativeAdditional.getChannels().stream().collect(Collectors.toMap(Channel::getType, Channel::getContact));
        ServiceRequestMetadataDTO serviceMetadataDTO = ServiceRequestMetadataDTO.builder()
                .email(channelMap.get(Channel.TypeEnum.EMAIL))
                .phone(channelMap.get(Channel.TypeEnum.MOBILE))
                .supportUrl(channelMap.get(Channel.TypeEnum.WEB))
                .privacyUrl(initiativeAdditional.getPrivacyLink())
                .tosUrl(initiativeAdditional.getTcLink())
                .scope(initiativeAdditional.getServiceScope().name())
                .topicId(0)
                .build();
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
                .departmentName(StringUtils.isNotBlank(initiativeOrganizationInfoDTO.getOrganizationName()) ? initiativeOrganizationInfoDTO.getOrganizationName() : productDepartmentName)
                .organizationName(initiativeOrganizationInfoDTO.getOrganizationName())
                .organizationFiscalCode(initiativeOrganizationInfoDTO.getOrganizationVat())
                .build();
        ServiceRequestDTO.ServiceRequestDTOBuilder serviceRequestDTOBuilder = ServiceRequestDTO.builder()
                .serviceMetadata(serviceMetadataDTO)
                .serviceName(initiativeAdditional.getServiceName())
                .description(initiativeAdditional.getDescription())
                .organization(organizationDTO);
        return CollectionUtils.isEmpty(authorizedRecipients) ? serviceRequestDTOBuilder.build() : serviceRequestDTOBuilder.authorizedRecipients(authorizedRecipients).build();
    }

}

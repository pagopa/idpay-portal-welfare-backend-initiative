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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitiativeAdditionalDTOsToIOServiceRequestDTOMapper {

    private static final DateTimeFormatter IT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ITALIAN);
    private static final DateTimeFormatter IT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy 'alle' HH:mm", Locale.ITALIAN);

    /**
     * Lingua di default per la risoluzione dei testi localizzati, coerente con la regola di business
     * dell'as-is (per {@code descriptionMap} l'italiano è la lingua obbligatoria/di riferimento).
     */
    private static final String DEFAULT_LANGUAGE = Locale.ITALIAN.getLanguage();

    private static final String SECTION_ELIGIBILITY = "Chi può richiederlo";
    private static final String SECTION_BENEFIT = "Cosa offre";
    private static final String SECTION_HOW_TO_REQUEST = "Come richiederlo";
    private static final String SECTION_HOW_TO_USE = "Come si usa";
    private static final String SECTION_REMINDER = "Ricorda";

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
        String supportUrl = StringUtils.isNotBlank(initiativeAdditional.getSupportUrl())
                ? initiativeAdditional.getSupportUrl()
                : channelMap.get(Channel.TypeEnum.WEB);
        ServiceRequestMetadataDTO serviceMetadataDTO = ServiceRequestMetadataDTO.builder()
                .email(channelMap.get(Channel.TypeEnum.EMAIL))
                .phone(channelMap.get(Channel.TypeEnum.MOBILE))
                .supportUrl(supportUrl)
                .webUrl(StringUtils.trimToNull(initiativeAdditional.getCompatibleProductsUrl()))
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
                .description(buildIoDescription(initiativeAdditional))
                .organization(organizationDTO);
        return CollectionUtils.isEmpty(authorizedRecipients) ? serviceRequestDTOBuilder.build() : serviceRequestDTOBuilder.authorizedRecipients(authorizedRecipients).build();
    }

    /**
     * Costruisce la description (markdown) inviata a IO partendo dalla description base e appendendo,
     * quando valorizzate, le sezioni informative del bonus e le informazioni su date/validità.
     * I testi localizzati sono risolti in italiano (lingua di default), coerentemente con la regola
     * di business dell'as-is per le descrizioni localizzate.
     * Se nessuno dei nuovi campi è valorizzato, ritorna la description base invariata (comportamento as-is).
     */
    private String buildIoDescription(InitiativeAdditional initiativeAdditional) {
        String baseDescription = initiativeAdditional.getDescription();
        StringBuilder extra = new StringBuilder();
        appendSection(extra, SECTION_ELIGIBILITY, initiativeAdditional.getEligibilityInfoMap());
        appendSection(extra, SECTION_BENEFIT, initiativeAdditional.getBenefitInfoMap());
        appendSection(extra, SECTION_HOW_TO_REQUEST, initiativeAdditional.getHowToRequestInfoMap());
        appendSection(extra, SECTION_HOW_TO_USE, initiativeAdditional.getHowToUseInfoMap());
        appendSection(extra, SECTION_REMINDER, initiativeAdditional.getReminderInfoMap());
        appendDates(extra, initiativeAdditional);
        if (extra.isEmpty()) {
            return baseDescription;
        }
        return (StringUtils.isNotBlank(baseDescription) ? baseDescription : StringUtils.EMPTY) + extra;
    }

    private void appendSection(StringBuilder builder, String title, Map<String, String> localizedText) {
        String text = resolveLocalized(localizedText);
        if (StringUtils.isNotBlank(text)) {
            builder.append("\n\n## ").append(title).append("\n").append(text);
        }
    }

    private void appendDates(StringBuilder builder, InitiativeAdditional initiativeAdditional) {
        if (initiativeAdditional.getRequestStartDate() != null) {
            builder.append("\n\nRichieste aperte dal ")
                    .append(initiativeAdditional.getRequestStartDate().format(IT_DATE_FORMATTER));
        }
        if (initiativeAdditional.getBonusValidityDays() != null) {
            builder.append("\n\nValidità del bonus: entro ")
                    .append(initiativeAdditional.getBonusValidityDays())
                    .append(" giorni");
        }
        if (initiativeAdditional.getServiceAvailabilityDate() != null) {
            builder.append("\n\nServizio disponibile dal ")
                    .append(initiativeAdditional.getServiceAvailabilityDate().format(IT_DATE_TIME_FORMATTER));
        }
    }

    /**
     * Risolve il testo localizzato in una singola stringa usando l'italiano come lingua di default,
     * con lo stesso idioma già usato nel codice esistente ({@code StringUtils.defaultString} su
     * {@code descriptionMap}): non ritorna mai {@code null}.
     */
    private String resolveLocalized(Map<String, String> localizedText) {
        if (localizedText == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.defaultString(localizedText.get(DEFAULT_LANGUAGE));
    }

}

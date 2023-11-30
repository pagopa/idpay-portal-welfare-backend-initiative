package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.InitiativeRewardRule;
import it.gov.pagopa.initiative.model.rule.reward.RewardGroups;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.*;
import it.gov.pagopa.initiative.service.AESTokenService;
import it.gov.pagopa.initiative.utils.InitiativeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {InitiativeModelToDTOMapper.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {
        InitiativeModelToDTOMapper.class})
class InitiativeModelToDTOMapperTest {
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";
    public static final String ENCRYPTED_API_KEY_CLIENT_ID = "encryptedApiKeyClientId";
    public static final String ENCRYPTED_API_KEY_CLIENT_ASSERTION = "encryptedApiKeyClientAssertion";
    public static final String ITALIAN_LANGUAGE = "it";
    private final static String SERVICE_ID = "SERVICE_ID";

    @Autowired
    private InitiativeModelToDTOMapper initiativeModelToDTOMapper;

    @MockBean
    private AESTokenService aesTokenServiceMock;
    @MockBean
    private InitiativeUtils initiativeUtilsMock;

    private Initiative fullInitiative;
    private List<Initiative> initiativeList;
    private InitiativeDTO fullInitiativeDTO;
    private InitiativeBeneficiaryRule initiativeBeneficiaryRule;
    private InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO;
    private List<InitiativeSummaryDTO> initiativeSummaryDTOList;
    private InitiativeRefundRuleDTO refundRuleDTO1;
    private InitiativeRefundRule refundRule1;
    private InitiativeRefundRuleDTO refundRuleDTO2;
    private InitiativeRefundRule refundRule2;
    private InitiativeRefundRuleDTO refundRuleDTO3;
    private InitiativeRefundRule refundRule3;
    private InitiativeDTO fullInitiativeDTOStep4;
    private Initiative fullInitiativeStep4RewardAndTrxRules;
    private InitiativeDTO getFullInitiativeDTOStep4RewardGroup;
    private Initiative fullInitiativeStep4RewardAndTrxRulesRewardGroup;
    private InitiativeDTO fullInitiativeDTOStep4RewardLimitEmpty;
    private Initiative fullInitiativeStep4RewardLimitEmpty;
    private InitiativeDTO fullInitiativeDTOStep4DayOfWeekNull;
    private Initiative fullInitiativeStep4DayOfWeekNull;
    private InitiativeDTO fullInitiativeDTOStep4MccFilterNull;
    private Initiative fullInitiativeStep4MccFilterNull;
    private InitiativeDTO fullInitiativeDTOStep4TrxCountNull;
    private Initiative fullInitiativeStep4TrxCountNull;
    private InitiativeDTO fullInitiativeDTOStep4ThresholdNull;
    private Initiative fullInitiativeStep4ThresholdNull;
    private InitiativeAdditionalDTO initiativeAdditionalDTOOnlyTokens;
    private InitiativeAdditional initiativeAdditionalOnlyTokens;
    private InitiativeDetailDTO fullInitiativeDetailDTO;
    private Initiative initiativeStep2BeneficiaryTypeNull;
    private InitiativeDTO initiativeStep2DTOBeneficiaryTypeNull;
    private Initiative initiativeStep2FamilyUnitNotNull;
    private InitiativeDTO initiativeStep2DTOFamilyUnitNotNull;

    @BeforeEach
    public void setUp() {
        fullInitiative = createFullInitiative();
        Initiative fullInitiative2 = createFullInitiative2();
        initiativeList = new ArrayList<>();
        initiativeList.addAll(Arrays.asList(fullInitiative, fullInitiative2));
        fullInitiativeDTO = createFullInitiativeDTO();
        initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        InitiativeSummaryDTO initiativeSummaryDTO = createInitiativeSummaryDTO();
        InitiativeSummaryDTO initiativeSummaryDTO2 = createInitiativeSummaryDTO2();
        initiativeSummaryDTOList = new ArrayList<>();
        initiativeSummaryDTOList.addAll(Arrays.asList(initiativeSummaryDTO, initiativeSummaryDTO2));
        refundRuleDTO1 = createRefundRuleDTOValidWithTimeParameter();
        refundRule1 = createRefundRuleValidWithTimeParameter();
        refundRuleDTO2 = createRefundRuleDTOValidWithAccumulatedAmount();
        refundRule2 = createRefundRuleValidWithAccumulatedAmount();
        refundRuleDTO3 = createRefundRuleDTOValidWithTimeParameterAndAdditionalNull();
        refundRule3 = createRefundRuleValidWithTimeParameterAndAdditionalNull();
        fullInitiativeDTOStep4 = createStep4InitiativeDTO();
        fullInitiativeStep4RewardAndTrxRules = createStep4Initiative();
        getFullInitiativeDTOStep4RewardGroup = createStep4InitiativeDTORewardGroup();
        fullInitiativeStep4RewardAndTrxRulesRewardGroup = createStep4InitiativeRewardGroup();
        fullInitiativeStep4RewardLimitEmpty = createStep4InitiativeRewardLimitEmpty();
        fullInitiativeDTOStep4RewardLimitEmpty = createStep4InitiativeDTORewardLimitEmpty();
        fullInitiativeStep4DayOfWeekNull = createStep4InitiativeDayOfWeekNull();
        fullInitiativeDTOStep4DayOfWeekNull = createStep4InitiativeDTODayOfWeekNull();
        fullInitiativeStep4MccFilterNull = createStep4InitiativeMccFilterNull();
        fullInitiativeDTOStep4MccFilterNull = createStep4InitiativeDTOMccFilterNull();
        fullInitiativeStep4TrxCountNull = createStep4InitiativeTrxCountNull();
        fullInitiativeDTOStep4TrxCountNull = createStep4InitiativeDTOTrxCountNull();
        fullInitiativeStep4ThresholdNull = createStep4InitiativeThresholdNull();
        fullInitiativeDTOStep4ThresholdNull = createStep4InitiativeDTOThresholdNull();
        fullInitiativeDetailDTO = createInitiativeDetailDTO();
        initiativeStep2BeneficiaryTypeNull = createStep2InitiativeBeneficiaryTypeNull();
        initiativeStep2DTOBeneficiaryTypeNull = createStep2InitiativeDTOBeneficiaryTypeNull();
        initiativeStep2FamilyUnitNotNull = createStep2InitiativeFamilyUnitNotNull();
        initiativeStep2DTOFamilyUnitNotNull = createStep2InitiativeDTOFamilyUnitNotNull();

        Mockito.when(aesTokenServiceMock.decrypt(ENCRYPTED_API_KEY_CLIENT_ID)).thenReturn(API_KEY_CLIENT_ID);
        Mockito.when(aesTokenServiceMock.decrypt(ENCRYPTED_API_KEY_CLIENT_ASSERTION)).thenReturn(API_KEY_CLIENT_ASSERTION);
    }

    @Test
    void toInitiativeDataDTO_Null() {

        initiativeModelToDTOMapper.toInitiativeDataDTO(null, Locale.ITALIAN);
        assertNull(null);
    }

    @Test
    void toInitiativeDataDTO_ok() {
        Initiative initiative = createStep4Initiative();
        initiative.getAdditionalInfo().setLogoFileName("logo.png");
        Locale acceptLanguage = Locale.ENGLISH;

        Mockito.when(initiativeUtilsMock.createLogoUrl(initiative.getOrganizationId(), initiative.getInitiativeId())).thenReturn("https://test" + String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE,
                initiative.getOrganizationId(),initiative.getInitiativeId(), InitiativeConstants.Logo.LOGO_NAME));

        InitiativeDataDTO initiativeDataDTO = initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, acceptLanguage);

        assertEquals(initiative.getInitiativeId(), initiativeDataDTO.getInitiativeId());
        assertEquals(initiative.getInitiativeName(), initiativeDataDTO.getInitiativeName());
        assertEquals(ITALIAN_LANGUAGE, initiativeDataDTO.getDescription());
        assertEquals(initiative.getOrganizationId(), initiativeDataDTO.getOrganizationId());
        assertEquals(initiative.getOrganizationName(), initiativeDataDTO.getOrganizationName());
        assertEquals(initiative.getAdditionalInfo().getTcLink(), initiativeDataDTO.getTcLink());
        assertEquals(initiative.getAdditionalInfo().getPrivacyLink(), initiativeDataDTO.getPrivacyLink());
        assertEquals("https://test" + String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE,
                initiative.getOrganizationId(),initiative.getInitiativeId(), InitiativeConstants.Logo.LOGO_NAME), initiativeDataDTO.getLogoURL());
    }

    @Test
    void toInitiativeDataDTO_GeneralNullLogoFileNameNull() {
        Initiative initiative = createStep4Initiative();
        initiative.getAdditionalInfo().setLogoFileName(null);
        initiative.setGeneral(null);
        Locale acceptLanguage = Locale.ITALIAN;
        String description = StringUtils.EMPTY;

        InitiativeDataDTO initiativeDataDTO = initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, acceptLanguage);

        assertEquals(initiative.getInitiativeId(), initiativeDataDTO.getInitiativeId());
        assertEquals(initiative.getInitiativeName(), initiativeDataDTO.getInitiativeName());
        assertEquals(description, initiativeDataDTO.getDescription());
        assertEquals(initiative.getOrganizationId(), initiativeDataDTO.getOrganizationId());
        assertEquals(initiative.getOrganizationName(), initiativeDataDTO.getOrganizationName());
        assertEquals(initiative.getAdditionalInfo().getTcLink(), initiativeDataDTO.getTcLink());
        assertEquals(initiative.getAdditionalInfo().getPrivacyLink(), initiativeDataDTO.getPrivacyLink());
        assertNull(initiativeDataDTO.getLogoURL());

    }

    @Test
    void toInitiativeDataDTO_DescriptionMapNull() {
        Initiative initiative = createStep4Initiative();
        initiative.getGeneral().setDescriptionMap(null);
        Locale language = Locale.ITALIAN;
        String description = StringUtils.EMPTY;

        InitiativeDataDTO initiativeDataDTO = initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, language);

        initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, language);
        assertEquals(initiative.getInitiativeId(), initiativeDataDTO.getInitiativeId());
        assertEquals(initiative.getInitiativeName(), initiativeDataDTO.getInitiativeName());
        assertEquals(description, initiativeDataDTO.getDescription());
        assertEquals(initiative.getOrganizationId(), initiativeDataDTO.getOrganizationId());
        assertEquals(initiative.getOrganizationName(), initiativeDataDTO.getOrganizationName());
        assertEquals(initiative.getAdditionalInfo().getTcLink(), initiativeDataDTO.getTcLink());
        assertEquals(initiative.getAdditionalInfo().getPrivacyLink(), initiativeDataDTO.getPrivacyLink());
        assertNull(initiativeDataDTO.getLogoURL());
    }

    @Test
    void toInitiativeDataDTO_AdditionalInfoNull() {
        Initiative initiative = createStep4Initiative();
        initiative.setAdditionalInfo(null);
        Locale language = Locale.ITALIAN;

        try {
            initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, language);
        } catch (Exception e){
            Assertions.assertTrue(e.getMessage().contains("null"));
        }
    }

    @Test
    void toInitiativeDataDTO_LogoFileNameNull() {
        Initiative initiative = createStep4Initiative();
        initiative.getAdditionalInfo().setLogoFileName(null);
        Locale language = Locale.ITALIAN;

        InitiativeDataDTO initiativeDataDTO = initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, language);
        assertEquals(initiative.getInitiativeId(), initiativeDataDTO.getInitiativeId());
        assertEquals(initiative.getInitiativeName(), initiativeDataDTO.getInitiativeName());
        assertEquals(ITALIAN_LANGUAGE, initiativeDataDTO.getDescription());
        assertEquals(initiative.getOrganizationId(), initiativeDataDTO.getOrganizationId());
        assertEquals(initiative.getOrganizationName(), initiativeDataDTO.getOrganizationName());
        assertEquals(initiative.getAdditionalInfo().getTcLink(), initiativeDataDTO.getTcLink());
        assertEquals(initiative.getAdditionalInfo().getPrivacyLink(), initiativeDataDTO.getPrivacyLink());
        assertNull(initiativeDataDTO.getLogoURL());
    }
    @Test
    void toInitiativeGeneralDTOBeneficiaryTypeNull() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(initiativeStep2BeneficiaryTypeNull);
        assertEquals(initiativeStep2DTOBeneficiaryTypeNull, initiativeDTO);

    }
    @Test
    void toInitiativeGeneralDTOFamilyUnitNotNull() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(initiativeStep2FamilyUnitNotNull);
        assertEquals(initiativeStep2DTOFamilyUnitNotNull, initiativeDTO);

    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesThresholdNull_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4ThresholdNull);
        assertEquals(fullInitiativeDTOStep4ThresholdNull, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesTrxCountNull_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4TrxCountNull);
        assertEquals(fullInitiativeDTOStep4TrxCountNull, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesMccFilterNull_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4MccFilterNull);
        assertEquals(fullInitiativeDTOStep4MccFilterNull, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesDayOfWeekNull_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4DayOfWeekNull);
        assertEquals(fullInitiativeDTOStep4DayOfWeekNull, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesRewardLimitEmpty_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4RewardLimitEmpty);
        assertEquals(fullInitiativeDTOStep4RewardLimitEmpty, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRulesRewardGroup_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4RewardAndTrxRulesRewardGroup);
        assertEquals(getFullInitiativeDTOStep4RewardGroup, initiativeDTO);
    }

    @Test
    void toInitiativeDTOStep4WithRewardAndTrxRules_equals() {
        InitiativeDTO initiativeDTO = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiativeStep4RewardAndTrxRules);
        assertEquals(fullInitiativeDTOStep4, initiativeDTO);
    }

    @Test
    void toRefundRuleDTOValidTimeParameter_equals() {
        InitiativeRefundRuleDTO initiativeRefundRuleDTO = initiativeModelToDTOMapper.toInitiativeRefundRuleDTO(refundRule1);
        assertEquals(initiativeRefundRuleDTO, refundRuleDTO1);
    }

    @Test
    void toRefundRuleDTOValidAccumulatedAmount_equals() {
        InitiativeRefundRuleDTO initiativeRefundRuleDTO = initiativeModelToDTOMapper.toInitiativeRefundRuleDTO(refundRule2);
        assertEquals(initiativeRefundRuleDTO, refundRuleDTO2);
    }

    @Test
    void toRefundRuleDTOValidTimeParameterAndAdditionalNull_equals() {
        InitiativeRefundRuleDTO initiativeRefundRuleDTO = initiativeModelToDTOMapper.toInitiativeRefundRuleDTO(refundRule3);
        assertEquals(initiativeRefundRuleDTO, refundRuleDTO3);
    }

    @Test
    void toInitiativeDTO_equals() {
        InitiativeDTO initiativeDTOtoBeVerified = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiative);

        //Check the equality of the results
        assertEquals(fullInitiativeDTO, initiativeDTOtoBeVerified);
    }
    @Test
    void toInitiativeDTOIsLogoPresent_true() {
        InitiativeDTO initiativeDTO = createFullInitiativeDTO();
        initiativeDTO.setAdditionalInfo(createInitiativeAdditionalDTO());
        initiativeDTO.getAdditionalInfo().setLogoFileName("test.png");
        initiativeDTO.getAdditionalInfo().setLogoURL("test.it");
        initiativeDTO.setIsLogoPresent(true);
        fullInitiative.setAdditionalInfo(createInitiativeAdditional());
        fullInitiative.getAdditionalInfo().setLogoFileName("test.png");

        Mockito.when(initiativeUtilsMock.createLogoUrl(anyString(),anyString())).thenReturn("test.it");

        InitiativeDTO initiativeDTOExpected = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiative);

        assertEquals(initiativeDTO,initiativeDTOExpected);
    }

    @Test
    void toInitiativeDetailDTO_equals() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiativeDetailDTO, initiativeDetailDTO);
    }
    @Test
    void toInitiativeDetailDTO_rewardNull() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.setRewardRule(null);
        fullInitiativeDetailDTO.setRewardRule(null);

        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiativeDetailDTO, initiativeDetailDTO);
    }
    @Test
    void toInitiativeDetailDTO_withRewardGroups() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.setRewardRule(createInitiativeRewardRuleRewardGroup());
        InitiativeRewardRuleDTO rewardGroup = createInitiativeRewardRuleDTORewardGroupDTO();
        ((RewardGroupsDTO) rewardGroup).setType(null);
        fullInitiativeDetailDTO.setRewardRule(rewardGroup);

        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiativeDetailDTO, initiativeDetailDTO);
    }
    @Test
    void toInitiativeDetailDTO_refundRule() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        fullInitiativeDetailDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameterAndAdditionalNull());
        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiativeDetailDTO, initiativeDetailDTO);
    }
    @Test
    void toInitiativeDetailDTOAdditionalInfo_Null () {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setAdditionalInfo(null);
        try {
            initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("null"));
        }
    }
    @Test
    void toInitiativeDetailDTOWithLogoURL() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.getAdditionalInfo().setLogoFileName("test.png");

        Mockito.when(initiativeUtilsMock.createLogoUrl(anyString(),anyString())).thenReturn("test.it");

        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals("test.it", initiativeDetailDTO.getLogoURL());
    }
    @Test
    void toInitiativeDetailDTOWithGeneralInfo_Null() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.setGeneral(null);
        try {
            initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("null"));
        }
    }
    @Test
    void toInitiativeDetailDTOWithRuleDescription_Null() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.getGeneral().setDescriptionMap(null);
        fullInitiativeDetailDTO.setRuleDescription(StringUtils.EMPTY);

        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiativeDetailDTO, initiativeDetailDTO);
    }

    @Test
    void toInitiativeDetailDTO_nullStartAndEndDate() {
        Locale acceptLanguage = Locale.ITALIAN;
        fullInitiative.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        fullInitiative.getGeneral().setRankingStartDate(null);
        fullInitiative.getGeneral().setRankingEndDate(null);

        InitiativeDetailDTO initiativeDetailDTO = initiativeModelToDTOMapper.toInitiativeDetailDTO(fullInitiative,acceptLanguage);

        assertEquals(fullInitiative.getGeneral().getStartDate(), initiativeDetailDTO.getFruitionStartDate());
        assertEquals(fullInitiative.getGeneral().getEndDate(), initiativeDetailDTO.getFruitionEndDate());
    }

    @Test
    void toInitiativeDTONull_equals() {
        assertNull(initiativeModelToDTOMapper.toInitiativeDTO(null));
    }

    @Test
    void toDtoOnlyId_equals() {
        InitiativeDTO initiativeDTOtoBeVerified = initiativeModelToDTOMapper.toDtoOnlyId(fullInitiative);
        //Check the equality of the results
        assertEquals(fullInitiative.getInitiativeId(), initiativeDTOtoBeVerified.getInitiativeId());
    }

    @Test
    void toDtoOnlyNull_equals() {
        assertNull(initiativeModelToDTOMapper.toDtoOnlyId(null));
    }

    @Test
    void toInitiativeBeneficiaryRuleDTO_equals() {
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTOtoBeVerified = initiativeModelToDTOMapper.toInitiativeBeneficiaryRuleDTO(initiativeBeneficiaryRule);
        //Check the equality of the results
        assertEquals(initiativeBeneficiaryRuleDTO, initiativeBeneficiaryRuleDTOtoBeVerified);
    }

    @Test
    void givenApiKeyClientIdNotPresent_toInitiativeDTO() {
        fullInitiative.getBeneficiaryRule().setApiKeyClientId(null);
        InitiativeDTO initiativeDTOActual = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiative);
        //Check the equality of the results
        assertEquals(fullInitiative.getBeneficiaryRule().getApiKeyClientId(), initiativeDTOActual.getBeneficiaryRule().getApiKeyClientId());
    }

    @Test
    void givenApiKeyClientAssertionNotPresent_toInitiativeDTO() {
        fullInitiative.getBeneficiaryRule().setApiKeyClientAssertion(null);
        InitiativeDTO initiativeDTOActual = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiative);
        //Check the equality of the results
        assertEquals(fullInitiative.getBeneficiaryRule().getApiKeyClientAssertion(), initiativeDTOActual.getBeneficiaryRule().getApiKeyClientAssertion());
    }

    @Test
    void toInitiativeAdditionalDTO() {
        Initiative initiative = createStep1Initiative();

        initiative.setAdditionalInfo(null);
        assertNull(initiativeModelToDTOMapper.toInitiativeDTO(initiative).getAdditionalInfo());
    }

    @Test
    void testToChannelsDTO_empty() {
        Initiative initiative = createStep1Initiative();
        InitiativeAdditional additionalInfo = new InitiativeAdditional();
        List<Channel> channels = new ArrayList<>();
        List<ChannelDTO> channelDTO = new ArrayList<>();
        additionalInfo.setChannels(channels);
        additionalInfo.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiative.setAdditionalInfo(additionalInfo);

        assertEquals(initiativeModelToDTOMapper.toInitiativeDTO(initiative).getAdditionalInfo().getChannels(), channelDTO);
    }

    @Test
    void testToInitiativeBeneficiaryRuleDTO_CollectionUtils_isEmpty() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = mock(InitiativeBeneficiaryRule.class);
        when(initiativeBeneficiaryRule.getAutomatedCriteria()).thenReturn(new ArrayList<>());
        when(initiativeBeneficiaryRule.getSelfDeclarationCriteria()).thenReturn(new ArrayList<>());
        InitiativeBeneficiaryRuleDTO actualToInitiativeBeneficiaryRuleDTOResult = initiativeModelToDTOMapper
                .toInitiativeBeneficiaryRuleDTO(initiativeBeneficiaryRule);
        List<AutomatedCriteriaDTO> automatedCriteria = actualToInitiativeBeneficiaryRuleDTOResult.getAutomatedCriteria();
        assertTrue(automatedCriteria.isEmpty());
        assertSame(automatedCriteria, actualToInitiativeBeneficiaryRuleDTOResult.getSelfDeclarationCriteria());
        verify(initiativeBeneficiaryRule).getAutomatedCriteria();
        verify(initiativeBeneficiaryRule).getSelfDeclarationCriteria();
    }

    @Test
    void testToInitiativeBeneficiaryRuleDTO_mapIfs() {
        ArrayList<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(new AutomatedCriteria("JaneDoe", "Code", "Field", FilterOperatorEnumModel.EQ, "42",
                "42", AutomatedCriteria.OrderDirection.ASC, List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.MINORENNE)));
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = mock(InitiativeBeneficiaryRule.class);
        when(initiativeBeneficiaryRule.getAutomatedCriteria()).thenReturn(automatedCriteriaList);
        when(initiativeBeneficiaryRule.getSelfDeclarationCriteria()).thenReturn(new ArrayList<>());
        InitiativeBeneficiaryRuleDTO actualToInitiativeBeneficiaryRuleDTOResult = initiativeModelToDTOMapper
                .toInitiativeBeneficiaryRuleDTO(initiativeBeneficiaryRule);
        assertEquals(1, actualToInitiativeBeneficiaryRuleDTOResult.getAutomatedCriteria().size());
        assertTrue(actualToInitiativeBeneficiaryRuleDTOResult.getSelfDeclarationCriteria().isEmpty());
        verify(initiativeBeneficiaryRule, atLeast(1)).getAutomatedCriteria();
        verify(initiativeBeneficiaryRule).getSelfDeclarationCriteria();
    }

    @Test
    void toInitiativeBeneficiaryRuleDTONull_equals() {
        assertNull(initiativeModelToDTOMapper.toInitiativeBeneficiaryRuleDTO(null));
    }

    @Test
    void testToInitiativeSummaryDTOList_isEmpty() {
        assertTrue(initiativeModelToDTOMapper.toInitiativeSummaryDTOList(new ArrayList<>()).isEmpty());
    }

    @Test
    void toInitiativeSummaryDTOList_equals() {
        List<InitiativeSummaryDTO> initiativeSummaryDTOListActual = initiativeModelToDTOMapper.toInitiativeSummaryDTOList(initiativeList);
        //Check the equality of the results
        assertEquals(initiativeSummaryDTOList, initiativeSummaryDTOListActual);
    }

    @Test
    void testToInitiativeIssuerDTOList_empty() {
        assertTrue(initiativeModelToDTOMapper.toInitiativeIssuerDTOList(new ArrayList<>()).isEmpty());
    }

    @Test
    void testToInitiativeIssuerDTOList_OK() {
        Initiative initiative = createFullInitiative();

        ArrayList<Initiative> initiativeList = new ArrayList<>();
        initiativeList.add(initiative);

        assertEquals(1, initiativeModelToDTOMapper.toInitiativeIssuerDTOList(initiativeList).size());
    }

    @Test
    void testToInitiativeMilDTOList_empty() {
        assertTrue(initiativeModelToDTOMapper.toInitiativeListMilDTO(new ArrayList<>()).isEmpty());
    }

    @Test
    void testToInitiativeMilDTOList_OK() {
        Initiative initiative = createFullInitiative();
        initiative.getAdditionalInfo().setLogoFileName("file");

        ArrayList<Initiative> initiativeList = new ArrayList<>();
        initiativeList.add(initiative);

        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");

        InitiativeMilDTO expectedDTO = InitiativeMilDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .descriptionMap(language)
                .fruitionStartDate(LocalDate.now().plusDays(2))
                .fruitionEndDate(LocalDate.now().plusDays(3))
                .onboardingStartDate(LocalDate.now())
                .onboardingEndDate(LocalDate.now().plusDays(1))
                .beneficiaryKnown(true)
                .status("DRAFT")
                .tcLink("tcLink")
                .privacyLink("privacyLink")
                .initiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND)
                .beneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF)
                .build();

        assertEquals(List.of(expectedDTO), initiativeModelToDTOMapper.toInitiativeListMilDTO(initiativeList));
    }

    @Test
    void testLanguageMap() {
        Initiative initiative = createStep3Initiative();
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ENGLISH.getLanguage(), "en");
        initiative.getGeneral().setDescriptionMap(language);
        initiative.getAdditionalInfo().setLogoFileName("test.png") ;

        ArrayList<Initiative> initiativeList = new ArrayList<>();
        initiativeList.add(initiative);
        Mockito.when(initiativeUtilsMock.createLogoUrl(initiative.getOrganizationId(), initiative.getInitiativeId()))
                .thenReturn("https://test" + String.format(InitiativeConstants.Logo.LOGO_PATH_TEMPLATE,
                initiative.getOrganizationId(),initiative.getInitiativeId(), InitiativeConstants.Logo.LOGO_NAME));

        List<InitiativeIssuerDTO> initiativeIssuerDTOList = initiativeModelToDTOMapper.toInitiativeIssuerDTOList(initiativeList);
        assertEquals(1, initiativeIssuerDTOList.size());
        assertFalse(initiativeIssuerDTOList.get(0).getDescriptionMap().isEmpty());
        assertFalse(initiativeIssuerDTOList.get(0).getLogoURL().isBlank());
    }

    @Test
    void testToRewardRuleDTO() {
        Initiative initiative = createStep3Initiative();
        Mockito.when(initiativeModelToDTOMapper.toInitiativeDTO(initiative).getRewardRule()).thenReturn(null);
        assertNull(initiativeModelToDTOMapper.toInitiativeDTO(initiative).getRewardRule());
    }

    private Initiative createFullInitiative() {
        return createStep5Initiative();
    }

    private Initiative createFullInitiative2() {
        Initiative initiative2 = createStep5Initiative();
        initiative2.getGeneral().setRankingEnabled(Boolean.FALSE);
        return initiative2;
    }
    private InitiativeDTO createFullInitiativeDTO() {
        return createStep5InitiativeDTO();
    }
    private Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");

        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), ITALIAN_LANGUAGE);
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneral.setRankingStartDate(rankingStartDate);
        initiativeGeneral.setRankingEndDate(rankingEndDate);
        initiativeGeneral.setStartDate(startDate);
        initiativeGeneral.setEndDate(endDate);
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("privacyLink");
        initiativeAdditional.setTcLink("tcLink");
        initiativeAdditional.setServiceId(SERVICE_ID);
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        SelfCriteriaBool selfCriteriaBool = new SelfCriteriaBool();
        selfCriteriaBool.set_type(TypeBoolEnum.BOOLEAN);
        selfCriteriaBool.setCode("B001");
        selfCriteriaBool.setDescription("Desc_bool");
        selfCriteriaBool.setValue(true);
        SelfCriteriaMulti selfCriteriaMulti = new SelfCriteriaMulti();
        selfCriteriaMulti.set_type(TypeMultiEnum.MULTI);
        selfCriteriaMulti.setCode("B001");
        selfCriteriaMulti.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMulti.setValue(values);
        List<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        iSelfDeclarationCriteriaList.add(selfCriteriaBool);
        iSelfDeclarationCriteriaList.add(selfCriteriaMulti);
        iSelfDeclarationCriteriaList.add(null);
        initiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList);
        AutomatedCriteria automatedCriteria = new AutomatedCriteria();
        automatedCriteria.setAuthority("Authority_ISEE");
        automatedCriteria.setCode("Code_ISEE");
        automatedCriteria.setField("true");
        automatedCriteria.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteria.setValue("value");
        automatedCriteria.setIseeTypes(List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.SOCIOSANITARIO));
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRule.setApiKeyClientId(ENCRYPTED_API_KEY_CLIENT_ID);
        initiativeBeneficiaryRule.setApiKeyClientAssertion((ENCRYPTED_API_KEY_CLIENT_ASSERTION));
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep1InitiativeDTO() {
        return InitiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), ITALIAN_LANGUAGE);
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudget(new BigDecimal(1000000000));
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneralDTO.setRankingStartDate(rankingStartDate);
        initiativeGeneralDTO.setRankingEndDate(rankingEndDate);
        initiativeGeneralDTO.setStartDate(startDate);
        initiativeGeneralDTO.setEndDate(endDate);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("Description");
        initiativeAdditionalDTO.setPrivacyLink("privacyLink");
        initiativeAdditionalDTO.setTcLink("tcLink");
        initiativeAdditionalDTO.setServiceId(SERVICE_ID);
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    private Initiative createStep2Initiative() {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral());
        return initiative;
    }
    private Initiative createStep2InitiativeBeneficiaryTypeNull() {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral());
        initiative.getGeneral().setBeneficiaryType(null);
        return initiative;
    }
    private Initiative createStep2InitiativeFamilyUnitNotNull() {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral());
        initiative.getGeneral().setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.NF);
        initiative.getGeneral().setFamilyUnitComposition("INPS");
        return initiative;
    }

    private InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO());
        return initiativeDTO;
    }
    private InitiativeDTO createStep2InitiativeDTOBeneficiaryTypeNull() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO());
        initiativeDTO.getGeneral().setBeneficiaryType(null);
        initiativeDTO.setIsLogoPresent(false);
        return initiativeDTO;
    }
    private InitiativeDTO createStep2InitiativeDTOFamilyUnitNotNull() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO());
        initiativeDTO.getGeneral().setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.NF);
        initiativeDTO.getGeneral().setFamilyUnitComposition("INPS");
        initiativeDTO.setIsLogoPresent(false);
        return initiativeDTO;
    }

    private InitiativeBeneficiaryRuleDTO createInitiativeBeneficiaryRuleDTO() {
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = new InitiativeBeneficiaryRuleDTO();
        SelfCriteriaBoolDTO selfCriteriaBoolDTO = new SelfCriteriaBoolDTO();
        selfCriteriaBoolDTO.setType(it.gov.pagopa.initiative.dto.TypeBoolEnum.BOOLEAN);
        selfCriteriaBoolDTO.setCode("B001");
        selfCriteriaBoolDTO.setDescription("Desc_bool");
        selfCriteriaBoolDTO.setValue(true);
        SelfCriteriaMultiDTO selfCriteriaMultiDTO = new SelfCriteriaMultiDTO();
        selfCriteriaMultiDTO.setType(it.gov.pagopa.initiative.dto.TypeMultiEnum.MULTI);
        selfCriteriaMultiDTO.setCode("B001");
        selfCriteriaMultiDTO.setDescription("Desc_Multi");
        List<String> values = new ArrayList<>();
        values.add("valore1");
        values.add("valore2");
        selfCriteriaMultiDTO.setValue(values);
        List<AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems> anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems = new ArrayList<>();
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaBoolDTO);
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(selfCriteriaMultiDTO);
        anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems.add(null);
        initiativeBeneficiaryRuleDTO.setSelfDeclarationCriteria(anyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems);
        AutomatedCriteriaDTO automatedCriteriaDTO = new AutomatedCriteriaDTO();
        automatedCriteriaDTO.setAuthority("Authority_ISEE");
        automatedCriteriaDTO.setCode("Code_ISEE");
        automatedCriteriaDTO.setField("true");
        automatedCriteriaDTO.setOperator(FilterOperatorEnum.EQ);
        automatedCriteriaDTO.setValue("value");
        automatedCriteriaDTO.setIseeTypes(List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.SOCIOSANITARIO));
        List<AutomatedCriteriaDTO> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaDTO);
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRuleDTO;
    }

    private InitiativeSummaryDTO createInitiativeSummaryDTO() {
        InitiativeSummaryDTO initiativeSummaryDTO = new InitiativeSummaryDTO();
        initiativeSummaryDTO.setInitiativeId("Id1");
        initiativeSummaryDTO.setInitiativeName("initiativeName1");
        initiativeSummaryDTO.setStatus("DRAFT");
        initiativeSummaryDTO.setInitiativeRewardType("REFUND");
        return initiativeSummaryDTO;
    }
    private InitiativeSummaryDTO createInitiativeSummaryDTO2() {
        InitiativeSummaryDTO initiativeSummaryDTO2 = new InitiativeSummaryDTO();
        initiativeSummaryDTO2.setInitiativeId("Id1");
        initiativeSummaryDTO2.setInitiativeName("initiativeName1");
        initiativeSummaryDTO2.setStatus("DRAFT");
        initiativeSummaryDTO2.setRankingEnabled(Boolean.FALSE);
        initiativeSummaryDTO2.setInitiativeRewardType("REFUND");
        return initiativeSummaryDTO2;
    }

    private Initiative createStep3Initiative() {
        Initiative initiative = createStep2Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
    }

    private InitiativeDTO createStep3InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        initiativeDTO.setIsLogoPresent(false);
        return initiativeDTO;
    }


    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .build();
    }
    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTOWithoutType() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .build();
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupDTO() {
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        List<RewardGroupsDTO.RewardGroupDTO> list = new ArrayList<>();
        RewardGroupsDTO.RewardGroupDTO groupDTO1 = new RewardGroupsDTO.RewardGroupDTO(BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        list.add(groupDTO1);
        rewardGroupsDTO.setRewardGroups(list);
        return rewardGroupsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOValid() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTORewardLimitEmpty() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTODayOfWeekNull() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(null);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOMccFilterNull() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(null);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOTrxCountNull() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        ThresholdDTO thresholdDTO = new ThresholdDTO();

        thresholdDTO.setFrom(BigDecimal.valueOf(10));
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setTo(BigDecimal.valueOf(30));
        thresholdDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(null);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOThresholdNull() {
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = new InitiativeTrxConditionsDTO();
        List<DayOfWeekDTO.DayConfig> dayConfigs = new ArrayList<>();
        DayOfWeekDTO.DayConfig dayConfig1 = new DayOfWeekDTO.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<DayOfWeekDTO.Interval> intervals = new ArrayList<>();
        DayOfWeekDTO.Interval interval1 = new DayOfWeekDTO.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        DayOfWeekDTO dayOfWeekDTO = new DayOfWeekDTO(dayConfigs);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        MccFilterDTO mccFilterDTO = new MccFilterDTO();
        mccFilterDTO.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilterDTO.setValues(values);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(null);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeDTO createStep4InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }


    private InitiativeDTO createStep4InitiativeDTORewardLimitEmpty() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTORewardLimitEmpty());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTODayOfWeekNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTODayOfWeekNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOMccFilterNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOMccFilterNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOTrxCountNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOTrxCountNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOThresholdNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOThresholdNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTORewardGroup() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardGroupDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }


    private InitiativeRewardRule createInitiativeRewardRuleRewardValue() {
        return RewardValue.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
                .rewardValueType(RewardValue.RewardValueTypeEnum.PERCENTAGE)
                .build();
    }

    private InitiativeRewardRule createInitiativeRewardRuleRewardGroup() {
        RewardGroups rewardGroups = new RewardGroups();
        rewardGroups.setType("rewardGroups");
        List<RewardGroups.RewardGroup> list = new ArrayList<>();
        RewardGroups.RewardGroup group1 = new RewardGroups.RewardGroup(BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        list.add(group1);
        rewardGroups.setRewardGroups(list);
        return rewardGroups;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsValid() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsRewardLimitEmpty() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsDayOfWeekNull() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(null);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsMccFilterNull() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(null);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsTrxCountNull() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        Threshold threshold = new Threshold();

        threshold.setFrom(BigDecimal.valueOf(10));
        threshold.setFromIncluded(true);
        threshold.setTo(BigDecimal.valueOf(30));
        threshold.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(null);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsThresholdNull() {
        InitiativeTrxConditions initiativeTrxConditions = new InitiativeTrxConditions();
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig> dayConfigs = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig dayConfig1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.DayConfig();
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(java.time.DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.THURSDAY);
        dayConfig1.setDaysOfWeek(dayOfWeeks);
        List<it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval> intervals = new ArrayList<>();
        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval interval1 = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek.Interval();
        LocalTime t1 = LocalTime.of(6, 0, 0);
        LocalTime t2 = LocalTime.of(12, 0, 0);
        interval1.setStartTime(t1);
        interval1.setEndTime(t2);
        intervals.add(interval1);
        dayConfig1.setIntervals(intervals);
        dayConfigs.add(dayConfig1);

        it.gov.pagopa.initiative.model.rule.trx.DayOfWeek dayOfWeek = new it.gov.pagopa.initiative.model.rule.trx.DayOfWeek(dayConfigs);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        MccFilter mccFilter = new MccFilter();
        mccFilter.setAllowedList(true);
        Set<String> values = new HashSet<>();
        values.add("123");
        values.add("456");
        mccFilter.setValues(values);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimit(BigDecimal.valueOf(100));
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimit(BigDecimal.valueOf(3000));
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(null);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private Initiative createStep4Initiative() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsValid();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeRewardLimitEmpty() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsRewardLimitEmpty();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeDayOfWeekNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsDayOfWeekNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeMccFilterNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsMccFilterNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeTrxCountNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsTrxCountNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeThresholdNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsThresholdNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeRewardGroup() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardGroup();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsValid();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }


    private AccumulatedAmountDTO createAccumulatedAmountDTOValid() {
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThreshold(BigDecimal.valueOf(100000));
        return amountDTO;
    }

    private TimeParameterDTO createTimeParameterDTOValid() {
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid() {
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTOValid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameterAndAdditionalNull() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTOValid());
        refundRuleDTO.setAdditionalInfo(null);
        return refundRuleDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithAccumulatedAmount() {
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();
        refundRuleDTO.setAccumulatedAmount(createAccumulatedAmountDTOValid());
        refundRuleDTO.setTimeParameter(null);
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }


    private AccumulatedAmount createAccumulatedAmountValid() {
        AccumulatedAmount amount = new AccumulatedAmount();
        amount.setAccumulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amount.setRefundThreshold(BigDecimal.valueOf(100000));
        return amount;
    }

    private TimeParameter createTimeParameterValid() {
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfoValid() {
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }


    private InitiativeRefundRule createRefundRuleValidWithTimeParameter() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }

    private InitiativeRefundRule createRefundRuleValidWithTimeParameterAndAdditionalNull() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameterValid());
        refundRule.setAdditionalInfo(null);
        return refundRule;
    }

    private InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
    }


    private Initiative createStep5Initiative() {
        return createStep4Initiative();
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        return createStep4InitiativeDTO();
    }

    private InitiativeDetailDTO createInitiativeDetailDTO() {
        InitiativeDetailDTO initiativeDetailDTO = new InitiativeDetailDTO();
        initiativeDetailDTO.setInitiativeName("initiativeName1");
        initiativeDetailDTO.setStatus("DRAFT");
        initiativeDetailDTO.setDescription("Description");
        initiativeDetailDTO.setRuleDescription(ITALIAN_LANGUAGE);
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeDetailDTO.setOnboardingStartDate(rankingStartDate);
        initiativeDetailDTO.setOnboardingEndDate(rankingEndDate);
        initiativeDetailDTO.setFruitionStartDate(startDate);
        initiativeDetailDTO.setFruitionEndDate(endDate);
        initiativeDetailDTO.setRewardRule(createInitiativeRewardRuleDTORewardGroupDTO());
        initiativeDetailDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTOWithoutType());
        initiativeDetailDTO.setRefundRule(null);
        initiativeDetailDTO.setPrivacyLink("privacyLink");
        initiativeDetailDTO.setTcLink("tcLink");
        initiativeDetailDTO.setLogoURL(null);
        initiativeDetailDTO.setUpdateDate(LocalDateTime.of(2023,3,20,12,0));
        initiativeDetailDTO.setServiceId(SERVICE_ID);
        return initiativeDetailDTO;
    }

}

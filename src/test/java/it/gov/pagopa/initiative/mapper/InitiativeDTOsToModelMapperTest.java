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
import it.gov.pagopa.initiative.exception.custom.InvalidRewardRuleException;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {InitiativeDTOsToModelMapper.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {
        InitiativeDTOsToModelMapper.class})
class InitiativeDTOsToModelMapperTest {
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";
    public static final String ENCRYPTED_API_KEY_CLIENT_ID = "encryptedApiKeyClientId";
    public static final String ENCRYPTED_API_KEY_CLIENT_ASSERTION = "encryptedApiKeyClientAssertion";
    @Autowired
    InitiativeDTOsToModelMapper initiativeDTOsToModelMapper;

    @MockBean
    AESTokenService aesTokenService;

    private Initiative initiativeOnlyInfoGeneral;
    private Initiative initiativeNoBaseFields;
    private InitiativeBeneficiaryRule initiativeBeneficiaryRule;
    private InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO;
    private InitiativeAdditionalDTO initiativeInfoDTOnoBaseFields;
    private InitiativeGeneralDTO initiativeInfoOnlyInfoGeneralDTO;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOAmount;
    private Initiative initiativeOnlyRefundRule;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOTimeParameter;
    private Initiative initiativeOnlyRefundRule2;

    private InitiativeRefundRuleDTO initiativeRefundRuleDTOAdditionalNull;

    private Initiative initiativeOnlyRefundRule3;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTOcomplete;

    private Initiative initiativeOnlyRewardAndTrxRules;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTORewardRuleNull;

    private Initiative initiativeOnlyRewardAndTrxRulesRewardRuleNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTORewardGroup;

    private Initiative initiativeOnlyRewardAndTrxRulesRewardGroup;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTOThresholdNull;

    private Initiative initiativeOnlyRewardAndTrxRulesThresholdNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTORewardLimitsNull;

    private Initiative initiativeOnlyRewardAndTrxRulesRewardLimitsNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTODayOfWeekNull;

    private Initiative initiativeOnlyRewardAndTrxRulesDayOfWeekNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTOMccFilterNull;

    private Initiative initiativeOnlyRewardAndTrxRulesMccFilterNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTOTrxCountNull;

    private Initiative initiativeOnlyRewardAndTrxRulesTrxCountNull;

    private InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRuleDTOTrxRuleNull;

    private Initiative initiativeTrxRuleNull;

    private Initiative initiativeExpected;

    private InitiativeDTO initiativeDTO;
    private InitiativeRewardAndTrxRulesDTO initiativeOnlyRewardAbsolute;
    private Initiative initiativeTrxNullRewardAbsolute;
    private InitiativeGeneralDTO initiativeInfoOnlyInfoGeneralDTOFamilyUnitNotNull;
    private Initiative initiativeInfoOnlyInfoGeneralFamilyUnitNotNull;

    @BeforeEach
    public void setUp() {
        initiativeOnlyInfoGeneral = createStep1InitiativeOnlyInfoGeneral();
        initiativeInfoOnlyInfoGeneralFamilyUnitNotNull = createStep1InitiativeOnlyInfoGeneralFamilyUnitNotNull();
        initiativeNoBaseFields = createStep1InitiativeNoBaseFields();
        initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiativeInfoOnlyInfoGeneralDTO = createStep1InitiativeInfoDTOonlyInfoGeneral();
        initiativeInfoOnlyInfoGeneralDTOFamilyUnitNotNull = createStep1InitiativeInfoDTOonlyInfoGeneralFamilyUnitNotNull();
        initiativeInfoDTOnoBaseFields = createStep1InitiativeInfoDTOnoBaseFields();
        initiativeRefundRuleDTOAmount = createRefundRuleDTOValidWithAccumulatedAmount();
        initiativeOnlyRefundRule = createInitiativeOnlyRefundRule();
        initiativeRefundRuleDTOTimeParameter = createRefundRuleDTOValidWithTimeParameter();
        initiativeOnlyRefundRule2 = createInitiativeOnlyRefundRule2();
        initiativeRefundRuleDTOAdditionalNull = createRefundRuleDTOValidWithTimeParameterAndAdditionalNull();
        initiativeOnlyRefundRule3 = createInitiativeOnlyRefundRule3();
        initiativeRewardAndTrxRulesDTOcomplete = createInitiativeRewardAndTrxRulesDTO();
        initiativeOnlyRewardAndTrxRules = createInitiativeOnlyRewardAndTrxRules();
        initiativeRewardAndTrxRulesDTORewardRuleNull = createInitiativeRewardAndTrxRulesDTORewardRuleNull();
        initiativeOnlyRewardAndTrxRulesRewardRuleNull = createInitiativeOnlyRewardAndTrxRulesRewardRuleNull();
        initiativeRewardAndTrxRulesDTORewardGroup = createInitiativeRewardAndTrxRulesDTORewardGroup();
        initiativeOnlyRewardAndTrxRulesRewardGroup = createInitiativeOnlyRewardAndTrxRulesRewardGroup();
        initiativeOnlyRewardAndTrxRulesThresholdNull = createInitiativeOnlyRewardAndTrxRulesThresholdNull();
        initiativeRewardAndTrxRulesDTOThresholdNull = createInitiativeRewardAndTrxRulesDTOThresholdNull();
        initiativeOnlyRewardAndTrxRulesRewardLimitsNull = createInitiativeOnlyRewardAndTrxRewardLimitsEmpty();
        initiativeRewardAndTrxRulesDTORewardLimitsNull = createInitiativeRewardAndTrxRulesDTORewardLimitsEmpty();
        initiativeOnlyRewardAndTrxRulesDayOfWeekNull = createInitiativeOnlyRewardAndTrxDayOfWeekNull();
        initiativeRewardAndTrxRulesDTODayOfWeekNull = createInitiativeRewardAndTrxRulesDTODayOfWeekNull();
        initiativeRewardAndTrxRulesDTOMccFilterNull = createInitiativeRewardAndTrxRulesDTOMccFilterNull();
        initiativeOnlyRewardAndTrxRulesMccFilterNull = createInitiativeOnlyRewardAndTrxMccFilterNull();
        initiativeOnlyRewardAndTrxRulesTrxCountNull = createInitiativeOnlyRewardAndTrxRulesTrxCountNull();
        initiativeRewardAndTrxRulesDTOTrxCountNull = createInitiativeRewardAndTrxRulesDTOTrxCountNull();
        initiativeTrxRuleNull = createStep4InitiativeTrxNull();
        initiativeRewardAndTrxRuleDTOTrxRuleNull = createInitiativeRewardAndTrxRulesDTOTrxRuleNull();
        initiativeOnlyRewardAbsolute = createInitiativeOnlyRewardAbsolute();
        initiativeTrxNullRewardAbsolute = createStep4InitiativeTrxNullRewardAbsolute();

        initiativeDTO = createStep5InitiativeDTO();
        initiativeExpected = createStep5Initiative();

        Mockito.when(aesTokenService.encrypt(API_KEY_CLIENT_ID)).thenReturn(ENCRYPTED_API_KEY_CLIENT_ID);
        Mockito.when(aesTokenService.encrypt(API_KEY_CLIENT_ASSERTION)).thenReturn(ENCRYPTED_API_KEY_CLIENT_ASSERTION);
    }

    @Test
    void toInitiativeFromInitiativeDTO_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeDTO);
        assertEquals(initiativeExpected, initiative);
    }

    @Test
    void toInitiativeTrxConditionsNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRuleDTOTrxRuleNull);
        assertEquals(initiativeTrxRuleNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesTrxCountNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTOTrxCountNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesTrxCountNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesMccFilterNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTOMccFilterNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesMccFilterNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesDayOfWeekNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTODayOfWeekNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesDayOfWeekNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesRewardLimitsEmpty_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTORewardLimitsNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesRewardLimitsNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesThresholdNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTOThresholdNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesThresholdNull, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRulesRewardGroups_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTORewardGroup);
       assertEquals(initiativeOnlyRewardAndTrxRulesRewardGroup, initiative);
    }

    @Test
    void toInitiativeOnlyRewardAndTrxRules_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTOcomplete);
        assertEquals(initiativeOnlyRewardAndTrxRules, initiative);
    }
    @Test
    void toInitiative_withRewardAbsolute(){
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeOnlyRewardAbsolute);
        assertEquals(initiativeTrxNullRewardAbsolute, initiative);
    }
    @Test
    void toInitiative_withRewardAbsolute2(){
        InitiativeRewardRuleDTO rewardRule = RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(120))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.ABSOLUTE)
                .type("rewardValue")
                .build();
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(rewardRule);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        Executable executable = () -> initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTO);
        assertDoesNotThrow(executable);
    }
    @Test
    void toInitiative_withRewardPercentageMoreThan100() {
        InitiativeRewardRuleDTO rewardRule = RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(120))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(rewardRule);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        try {
            initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTO);
        } catch (InvalidRewardRuleException e){
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, e.getCode());
            assertEquals("Reward rules of initiative [%s] is not valid", e.getMessage());
        }
    }
    @Test
    void toInitiativeOnlyRewardAndTrxRulesRewardRuleNull_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTORewardRuleNull);
        assertEquals(initiativeOnlyRewardAndTrxRulesRewardRuleNull, initiative);
    }

    @Test
    void toInitiativeOnlyInfoGeneral_equals() {
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeInfoOnlyInfoGeneralDTO);
        //Check the equality of the results
        assertEquals(initiativeOnlyInfoGeneral, initiativeActual);
    }

    @Test
    void toInitiativeNoBaseFields_equals() {
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeInfoDTOnoBaseFields);
        //Check the equality of the results
        assertEquals(initiativeNoBaseFields, initiativeActual);
    }
    
    @Test
    void testToInitiativeGeneral_null() {
        Assertions.assertNull(initiativeDTOsToModelMapper.toInitiative((InitiativeGeneralDTO) null).getGeneral());
    }
    @Test
    void testToInitiativeGeneralFamilyUnitIsNotNull_ok() {
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeInfoOnlyInfoGeneralDTOFamilyUnitNotNull);

        assertEquals(initiativeInfoOnlyInfoGeneralFamilyUnitNotNull,initiativeActual);
    }

    @Test
    void testToInitiativeAdditional_null() {
    Assertions.assertNull(initiativeDTOsToModelMapper.toInitiative((InitiativeAdditionalDTO) null).getAdditionalInfo());
    }

    @Test
    void testToInitiativeAdditionalChannels_empty() {
        Initiative initiative = createStep1Initiative();
        InitiativeAdditional additionalInfo = createInitiativeAdditional();
        InitiativeAdditionalDTO additionalDTO = new InitiativeAdditionalDTO();
        List<Channel> channels = new ArrayList<>();
        List<ChannelDTO> channelDTO = new ArrayList<>();
        additionalDTO.setChannels(channelDTO);
        additionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiative.setAdditionalInfo(additionalInfo);

        assertTrue(CollectionUtils.isEmpty(channels));
        assertEquals(initiativeDTOsToModelMapper.toInitiative(additionalDTO).getAdditionalInfo().getChannels(), channelDTO);
    }
    
    @Test
    void testToInitiativeRewardRule_exception() {
        InitiativeRewardRuleDTO rewardRuleDTO = new InitiativeRewardRuleDTO() {};
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(rewardRuleDTO);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
       try {
           initiativeDTOsToModelMapper.toInitiative(initiativeRewardAndTrxRulesDTO);
       } catch (IllegalArgumentException exception) {
           assertEquals("Initiative Reward Rule not handled: it.gov.pagopa.initiative.mapper.InitiativeDTOsToModelMapperTest$1", exception.getMessage());
       }
}
    
    @Test
    void toBeneficiaryRule_equals() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRuleActual = initiativeDTOsToModelMapper.toBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        //Check the equality of the results
        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRuleActual);
    }

    @Test
    void toBeneficiaryRule_null() {
    assertNull(initiativeDTOsToModelMapper.toBeneficiaryRule(null));
}
    
    @Test
    void toBeneficiaryRule_setAutomatedCriteria() {
        InitiativeBeneficiaryRuleDTO beneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        InitiativeBeneficiaryRule beneficiaryRule = new InitiativeBeneficiaryRule();
        beneficiaryRuleDTO.setAutomatedCriteria(Collections.emptyList());
        beneficiaryRule.setAutomatedCriteria(Collections.emptyList());
        assertTrue(initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDTO).getAutomatedCriteria().isEmpty());
    }

    @Test
    void toBeneficiaryRule_setSelfDeclarationCriteria() {
        InitiativeBeneficiaryRuleDTO beneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        beneficiaryRuleDTO.getAutomatedCriteria().get(0).setOrderDirection(null);
        InitiativeBeneficiaryRule beneficiaryRule = createInitiativeBeneficiaryRule();
        beneficiaryRule.getAutomatedCriteria().get(0).setOrderDirection(null);
        beneficiaryRuleDTO.setSelfDeclarationCriteria(Collections.emptyList());
        beneficiaryRule.setSelfDeclarationCriteria(Collections.emptyList());
        assertTrue(initiativeDTOsToModelMapper.toBeneficiaryRule(beneficiaryRuleDTO).getSelfDeclarationCriteria().isEmpty());
    }

    @Test
    void givenApiKeyCientIdNotPresent_toInitiative() {
        initiativeDTO.getBeneficiaryRule().setApiKeyClientId(null);
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeDTO);
        //Check the equality of the results
        assertEquals(initiativeDTO.getBeneficiaryRule().getApiKeyClientId(), initiativeActual.getBeneficiaryRule().getApiKeyClientId());
    }

    @Test
    void givenApiKeyCientAssertionNotPresent_toInitiative() {
        initiativeDTO.getBeneficiaryRule().setApiKeyClientAssertion(null);
        Initiative initiativeActual = initiativeDTOsToModelMapper.toInitiative(initiativeDTO);
        //Check the equality of the results
        assertEquals(initiativeDTO.getBeneficiaryRule().getApiKeyClientAssertion(), initiativeActual.getBeneficiaryRule().getApiKeyClientAssertion());
    }

    @Test
    void toInitiativeOnlyRefundRule_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOAmount);
        assertEquals(initiativeOnlyRefundRule, initiative);
    }

    @Test
    void toInitiativeOnlyRefundRule2_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOTimeParameter);
        assertEquals(initiativeOnlyRefundRule2, initiative);
    }

    @Test
    void toInitiativeOnlyRefundRule3_equals() {
        Initiative initiative = initiativeDTOsToModelMapper.toInitiative(initiativeRefundRuleDTOAdditionalNull);
        assertEquals(initiativeOnlyRefundRule3, initiative);
    }

    private void createInitiativeBaseFields(Initiative initiative) {
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");
    }

    private Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        createInitiativeBaseFields(initiative);
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private Initiative createStep1InitiativeOnlyInfoGeneral() {
        Initiative initiative = new Initiative();
//        initiative = createInitiativeBaseFields(initiative);
        initiative.setGeneral(createInitiativeGeneral());
        return initiative;
    }
    private Initiative createStep1InitiativeOnlyInfoGeneralFamilyUnitNotNull() {
        Initiative initiative = new Initiative();
        initiative.setGeneral(createInitiativeGeneral());
        initiative.getGeneral().setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.NF);
        initiative.getGeneral().setFamilyUnitComposition("INPS");
        return initiative;
    }

    private Initiative createStep1InitiativeNoBaseFields() {
        Initiative initiative = new Initiative();
        initiative.setAdditionalInfo(createInitiativeAdditional());
        if (initiative.getAdditionalInfo() != null && initiative.getAdditionalInfo().getServiceName() != null)
            initiative.setInitiativeName(initiative.getAdditionalInfo().getServiceName());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudgetCents(1000L);
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.PF);
        initiativeGeneral.setBudgetCents(100000000000L);
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
        initiativeAdditional.setServiceId("serviceId");
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("privacyLink");
        initiativeAdditional.setTcLink("tcLink");
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
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteria);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRule.setApiKeyClientId(ENCRYPTED_API_KEY_CLIENT_ID);
        initiativeBeneficiaryRule.setApiKeyClientAssertion(ENCRYPTED_API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep1InitiativeDTO() {
        return InitiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    private InitiativeAdditionalDTO createStep1InitiativeInfoDTOnoBaseFields() {
        return createInitiativeAdditionalDTO();
    }

    private InitiativeGeneralDTO createStep1InitiativeInfoDTOonlyInfoGeneral() {
        return createInitiativeGeneralDTO();
    }
    private InitiativeGeneralDTO createStep1InitiativeInfoDTOonlyInfoGeneralFamilyUnitNotNull() {
        return createInitiativeGeneralDTOFamilyUnitNotNull();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudgetCents(1000L);
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.PF);
        initiativeGeneralDTO.setBudgetCents(100000000000L);
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
    private InitiativeGeneralDTO createInitiativeGeneralDTOFamilyUnitNotNull() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudgetCents(1000L);
        initiativeGeneralDTO.setBeneficiaryKnown(true);
        initiativeGeneralDTO.setBeneficiaryType(InitiativeGeneralDTO.BeneficiaryTypeEnum.NF);
        initiativeGeneralDTO.setFamilyUnitComposition("INPS");
        initiativeGeneralDTO.setBudgetCents(100000000000L);
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
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("Description");
        initiativeAdditionalDTO.setPrivacyLink("privacyLink");
        initiativeAdditionalDTO.setTcLink("tcLink");
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
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral();
        initiative.setGeneral(initiativeGeneral);
        return initiative;
    }

    private InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        InitiativeGeneralDTO initiativeGeneralDTO = createInitiativeGeneralDTO();
        initiativeDTO.setGeneral(initiativeGeneralDTO);
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
        List<AutomatedCriteriaDTO> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaDTO);
        initiativeBeneficiaryRuleDTO.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRuleDTO;
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
        return initiativeDTO;
    }


    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueAbsoluteDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.ABSOLUTE)
                .type("rewardValue")
                .build();
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardGroupDTO() {
        RewardGroupsDTO rewardGroupsDTO = new RewardGroupsDTO();
        rewardGroupsDTO.setType("rewardGroups");
        List<RewardGroupsDTO.RewardGroupDTO> list = new ArrayList<>();
        RewardGroupsDTO.RewardGroupDTO groupDTO1 = new RewardGroupsDTO.RewardGroupDTO(1000L, 10000L, BigDecimal.valueOf(50));
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

        thresholdDTO.setFromCents(1000L);
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setToCents(3000L);
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
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
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
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(null);
        initiativeTrxConditionsDTO.setTrxCount(trxCountDTO);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTORewardLimitsEmpty() {
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

        thresholdDTO.setFromCents(1000L);
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setToCents(3000L);
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

        thresholdDTO.setFromCents(1000L);
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setToCents(3000L);
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
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
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

        thresholdDTO.setFromCents(1000L);
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setToCents(3000L);
        thresholdDTO.setToIncluded(true);

        TrxCountDTO trxCountDTO = new TrxCountDTO();

        trxCountDTO.setFrom(10L);
        trxCountDTO.setFromIncluded(true);
        trxCountDTO.setTo(30L);
        trxCountDTO.setToIncluded(true);

        List<RewardLimitsDTO> rewardLimitsDTOList = new ArrayList<>();
        RewardLimitsDTO rewardLimitsDTO1 = new RewardLimitsDTO();
        rewardLimitsDTO1.setFrequency(RewardLimitsDTO.RewardLimitFrequency.DAILY);
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
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

        thresholdDTO.setFromCents(1000L);
        thresholdDTO.setFromIncluded(true);
        thresholdDTO.setToCents(3000L);
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
        rewardLimitsDTO1.setRewardLimitCents(10000L);
        RewardLimitsDTO rewardLimitsDTO2 = new RewardLimitsDTO();
        rewardLimitsDTO2.setFrequency(RewardLimitsDTO.RewardLimitFrequency.MONTHLY);
        rewardLimitsDTO2.setRewardLimitCents(300000L);
        rewardLimitsDTOList.add(rewardLimitsDTO1);
        rewardLimitsDTOList.add(rewardLimitsDTO2);

        initiativeTrxConditionsDTO.setDaysOfWeek(dayOfWeekDTO);
        initiativeTrxConditionsDTO.setThreshold(thresholdDTO);
        initiativeTrxConditionsDTO.setTrxCount(null);
        initiativeTrxConditionsDTO.setMccFilter(mccFilterDTO);
        initiativeTrxConditionsDTO.setRewardLimits(rewardLimitsDTOList);

        return initiativeTrxConditionsDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardValueDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = createInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTORewardGroup() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        InitiativeRewardRuleDTO initiativeRewardRuleDTO = createInitiativeRewardRuleDTORewardGroupDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(initiativeRewardRuleDTO);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = createInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTORewardRuleNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(null);
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        InitiativeTrxConditionsDTO initiativeTrxConditionsDTO = createInitiativeTrxConditionsDTOValid();
        initiativeRewardAndTrxRulesDTO.setTrxRule(initiativeTrxConditionsDTO);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOThresholdNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTOThresholdNull());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTORewardLimitsEmpty() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTORewardLimitsEmpty());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOMccFilterNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTOMccFilterNull());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTODayOfWeekNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTODayOfWeekNull());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOTrxCountNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTOTrxCountNull());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTOTrxRuleNull() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeRewardAndTrxRulesDTO.setTrxRule(null);
        return initiativeRewardAndTrxRulesDTO;
    }
    private InitiativeRewardAndTrxRulesDTO createInitiativeOnlyRewardAbsolute() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueAbsoluteDTO());
        initiativeRewardAndTrxRulesDTO.setInitiativeRewardType(InitiativeRewardAndTrxRulesDTO.InitiativeRewardTypeEnum.REFUND);
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeDTO createStep4InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }

    private InitiativeRewardRule createInitiativeRewardRuleRewardValue() {
        return RewardValue.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValue.RewardValueTypeEnum.PERCENTAGE)
                .type("rewardValue")
                .build();
    }
    private InitiativeRewardRule createInitiativeRewardRuleRewardAbsoluteValue() {
        return RewardValue.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .rewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE)
                .type("rewardValue")
                .build();
    }

    private InitiativeRewardRule createInitiativeRewardRuleRewardGroup() {
        RewardGroups rewardGroups = new RewardGroups();
        rewardGroups.setType("rewardGroups");
        List<RewardGroups.RewardGroup> list = new ArrayList<>();
        RewardGroups.RewardGroup group1 = new RewardGroups.RewardGroup(1000L, 10000L, BigDecimal.valueOf(50));
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

        threshold.setFromCents(1000L);
        threshold.setFromIncluded(true);
        threshold.setToCents(3000L);
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
        rewardLimits1.setRewardLimitCents(10000L);
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimitCents(300000L);
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(trxCount);
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
        rewardLimits1.setRewardLimitCents(10000L);
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimitCents(300000L);
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(null);
        initiativeTrxConditions.setTrxCount(trxCount);
        initiativeTrxConditions.setMccFilter(mccFilter);
        initiativeTrxConditions.setRewardLimits(rewardLimitsList);

        return initiativeTrxConditions;
    }

    private InitiativeTrxConditions createInitiativeTrxConditionsRewardLimitsEmpty() {
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

        threshold.setFromCents(1000L);
        threshold.setFromIncluded(true);
        threshold.setToCents(3000L);
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

        threshold.setFromCents(1000L);
        threshold.setFromIncluded(true);
        threshold.setToCents(3000L);
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
        rewardLimits1.setRewardLimitCents(10000L);
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimitCents(300000L);
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

        threshold.setFromCents(1000L);
        threshold.setFromIncluded(true);
        threshold.setToCents(3000L);
        threshold.setToIncluded(true);

        TrxCount trxCount = new TrxCount();

        trxCount.setFrom(10L);
        trxCount.setFromIncluded(true);
        trxCount.setTo(30L);
        trxCount.setToIncluded(true);

        List<RewardLimits> rewardLimitsList = new ArrayList<>();
        RewardLimits rewardLimits1 = new RewardLimits();
        rewardLimits1.setFrequency(RewardLimits.RewardLimitFrequency.DAILY);
        rewardLimits1.setRewardLimitCents(10000L);
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimitCents(300000L);
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

        threshold.setFromCents(1000L);
        threshold.setFromIncluded(true);
        threshold.setToCents(3000L);
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
        rewardLimits1.setRewardLimitCents(10000L);
        RewardLimits rewardLimits2 = new RewardLimits();
        rewardLimits2.setFrequency(RewardLimits.RewardLimitFrequency.MONTHLY);
        rewardLimits2.setRewardLimitCents(300000L);
        rewardLimitsList.add(rewardLimits1);
        rewardLimitsList.add(rewardLimits2);

        initiativeTrxConditions.setDaysOfWeek(dayOfWeek);
        initiativeTrxConditions.setThreshold(threshold);
        initiativeTrxConditions.setTrxCount(null);
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

    private Initiative createStep4InitiativeTrxNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(null);
        return initiative;
    }

    private Initiative createStep4InitiativeTrxNullRewardAbsolute() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardAbsoluteValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(null);
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesThresholdNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsThresholdNull());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesRewardRuleNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(null);
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRewardLimitsEmpty() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsRewardLimitsEmpty());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxDayOfWeekNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsDayOfWeekNull());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxMccFilterNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsMccFilterNull());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRules() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesTrxCountNull() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardValue());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsTrxCountNull());
        return initiative;
    }

    private Initiative createInitiativeOnlyRewardAndTrxRulesRewardGroup() {
        Initiative initiative = new Initiative();
        initiative.setRewardRule(createInitiativeRewardRuleRewardGroup());
        initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        initiative.setTrxRule(createInitiativeTrxConditionsValid());
        return initiative;
    }

    private Initiative createStep5Initiative() {
        return createStep4Initiative();
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        return createStep4InitiativeDTO();
    }


    private AccumulatedAmountDTO createAccumulatedAmountDTOValid() {
        AccumulatedAmountDTO amountDTO = new AccumulatedAmountDTO();
        amountDTO.setAccumulatedType(AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED);
        amountDTO.setRefundThresholdCents(10000000L);
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
        amount.setRefundThresholdCents(10000000L);
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


    private InitiativeRefundRule createRefundRuleValidWithAccumulatedAmount() {
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(createAccumulatedAmountValid());
        refundRule.setTimeParameter(null);
        refundRule.setAdditionalInfo(createAdditionalInfoValid());
        return refundRule;
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

    private Initiative createInitiativeOnlyRefundRule() {
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithAccumulatedAmount());
        return initiative;
    }

    private Initiative createInitiativeOnlyRefundRule2() {
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private Initiative createInitiativeOnlyRefundRule3() {
        Initiative initiative = new Initiative();
        initiative.setRefundRule(createRefundRuleValidWithTimeParameterAndAdditionalNull());
        return initiative;
    }
}

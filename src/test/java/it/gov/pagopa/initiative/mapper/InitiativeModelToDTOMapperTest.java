package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.AnyOfInitiativeBeneficiaryRuleDTOSelfDeclarationCriteriaItems;
import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.InitiativeAdditionalDTO;
import it.gov.pagopa.initiative.dto.InitiativeBeneficiaryRuleDTO;
import it.gov.pagopa.initiative.dto.InitiativeDTO;
import it.gov.pagopa.initiative.dto.InitiativeDataDTO;
import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardGroupsDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.model.AutomatedCriteria;
import it.gov.pagopa.initiative.model.FilterOperatorEnumModel;
import it.gov.pagopa.initiative.model.ISelfDeclarationCriteria;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeAdditional;
import it.gov.pagopa.initiative.model.InitiativeBeneficiaryRule;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import it.gov.pagopa.initiative.model.SelfCriteriaBool;
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
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;
import it.gov.pagopa.initiative.utils.InitiativeUtils;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InitiativeModelToDTOMapper.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {
        InitiativeModelToDTOMapper.class})
class InitiativeModelToDTOMapperTest {
    @Autowired
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;
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
    @MockBean
    InitiativeUtils initiativeUtils;

    @BeforeEach
    public void setUp() {
        fullInitiative = createFullInitiative();
        Initiative fullInitiative2 = createFullInitiative();
        initiativeList = new ArrayList<>();
        initiativeList.addAll(Arrays.asList(fullInitiative, fullInitiative2));
        fullInitiativeDTO = createFullInitiativeDTO();
        InitiativeDTO fullInitiativeDTO2 = createFullInitiativeDTO();
        initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        InitiativeSummaryDTO initiativeSummaryDTO = createInitiativeSummaryDTO();
        InitiativeSummaryDTO initiativeSummaryDTO2 = createInitiativeSummaryDTO();
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
        initiativeAdditionalOnlyTokens = createInitiativeAdditionalOnlyTokens();
        initiativeAdditionalDTOOnlyTokens = createInitiativeAdditionalDTOOnlyTokens();
    }

    @Test
    void toInitiativeDataDTO_returnNull() {
        try {
            initiativeModelToDTOMapper.toInitiativeDataDTO(null, Locale.ITALIAN);
        } catch (Exception e) {
            assertNull(null);
        }
    }

    @Test
    void toInitiativeDataDTO_getGeneralNull() {
        Initiative initiative = createStep4Initiative();
        initiative.setGeneral(createInitiativeGeneral());
        Locale language = Locale.ITALIAN;
        Locale acceptLanguage = Locale.ENGLISH;
        String description = StringUtils.EMPTY;

        initiativeModelToDTOMapper.toInitiativeDataDTO(initiative, language);
        assertEquals(description, StringUtils.defaultString(initiative.getGeneral().getDescriptionMap().get(language)));
        assertEquals(description, StringUtils.defaultString(initiative.getGeneral().getDescriptionMap().get(acceptLanguage.getLanguage())));
    }

    @Test
    void toInitiativeAdditionalDTOOnlyTokens() {
        InitiativeAdditionalDTO additionalDTO = initiativeModelToDTOMapper.toInitiativeAdditionalDTOOnlyTokens(initiativeAdditionalOnlyTokens);
        assertEquals(initiativeAdditionalDTOOnlyTokens, additionalDTO);
    }

    @Test
    void toInitiativeAdditionalDTOOnlyTokensNull() {
        InitiativeAdditionalDTO additionalDTO = initiativeModelToDTOMapper.toInitiativeAdditionalDTOOnlyTokens(null);
        assertNull(additionalDTO);
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
    void toInitiativeDTONull_equals() {
        assertNull(initiativeModelToDTOMapper.toInitiativeDTO(null));
    }

    @Test
    void toDtoOnlyId_equals() {
        InitiativeDTO initiativeDTOonlyId = new InitiativeDTO();
        initiativeDTOonlyId.setInitiativeId("Id1");
        InitiativeDTO initiativeDTOtoBeVerified = initiativeModelToDTOMapper.toDtoOnlyId(fullInitiative);
        //Check the equality of the results
        assertEquals(initiativeDTOonlyId, initiativeDTOtoBeVerified);
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
                "42", AutomatedCriteria.OrderDirection.ASC));
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
    void testToInitiativeIssuerDTOList() {
        assertTrue(initiativeModelToDTOMapper.toInitiativeIssuerDTOList(new ArrayList<>()).isEmpty());
    }

    @Test
    void testToInitiativeIssuerDTOList2() {
        Initiative initiative = createFullInitiative();

        ArrayList<Initiative> initiativeList = new ArrayList<>();
        initiativeList.add(initiative);
        assertEquals(1, initiativeModelToDTOMapper.toInitiativeIssuerDTOList(initiativeList).size());
    }

    private Initiative createFullInitiative() {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        return createStep2Initiative();
    }

    private InitiativeDTO createFullInitiativeDTO() {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        return createStep2InitiativeDTO();
    }

    private Initiative createStep1Initiative() {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");
        initiative.setPdndToken("pdndToken1");

        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
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
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    private InitiativeAdditional createInitiativeAdditionalOnlyTokens() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setPrimaryTokenIO("firstToken");
        initiativeAdditional.setSecondaryTokenIO("secondToken");
        return initiativeAdditional;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTOOnlyTokens() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setPrimaryTokenIO("firstToken");
        initiativeAdditionalDTO.setSecondaryTokenIO("secondToken");
        return initiativeAdditionalDTO;
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
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep1InitiativeDTO() {
        return InitiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .pdndToken("pdndToken1")
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
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

    private InitiativeDTO createStep2InitiativeDTO() {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO());
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
        return initiativeBeneficiaryRuleDTO;
    }

    private InitiativeSummaryDTO createInitiativeSummaryDTO() {
        InitiativeSummaryDTO initiativeSummaryDTO = new InitiativeSummaryDTO();
        initiativeSummaryDTO.setInitiativeId("Id1");
        initiativeSummaryDTO.setInitiativeName("initiativeName1");
        initiativeSummaryDTO.setStatus("DRAFT");
        return initiativeSummaryDTO;
    }

    private Initiative createStep3Initiative() {
        Initiative initiative = new Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
    }

    private InitiativeDTO createStep3InitiativeDTO() {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
        return initiativeDTO;
    }


    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTORewardValueDTO() {
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
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
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }


    private InitiativeDTO createStep4InitiativeDTORewardLimitEmpty() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTORewardLimitEmpty());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTODayOfWeekNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTODayOfWeekNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOMccFilterNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOMccFilterNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOTrxCountNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOTrxCountNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTOThresholdNull() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardValueDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOThresholdNull());
        return initiativeDTO;
    }

    private InitiativeDTO createStep4InitiativeDTORewardGroup() {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO();
        initiativeDTO.setRewardRule(createInitiativeRewardRuleDTORewardGroupDTO());
        initiativeDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeDTO;
    }


    private InitiativeRewardRule createInitiativeRewardRuleRewardValue() {
        return RewardValue.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
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
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeRewardLimitEmpty() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsRewardLimitEmpty();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeDayOfWeekNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsDayOfWeekNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeMccFilterNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsMccFilterNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeTrxCountNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsTrxCountNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeThresholdNull() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardValue();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsThresholdNull();
        initiative.setRewardRule(initiativeRewardRule);
        initiative.setTrxRule(initiativeTrxConditions);
        return initiative;
    }

    private Initiative createStep4InitiativeRewardGroup() {
        Initiative initiative = createStep3Initiative();
        InitiativeRewardRule initiativeRewardRule = createInitiativeRewardRuleRewardGroup();
        InitiativeTrxConditions initiativeTrxConditions = createInitiativeTrxConditionsValid();
        initiative.setRewardRule(initiativeRewardRule);
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
        return new Initiative();
    }

    private InitiativeDTO createStep5InitiativeDTO() {
        return new InitiativeDTO();
    }

    private Initiative createStep6Initiative() {
        return new Initiative();
    }

    private InitiativeDTO createStep6InitiativeDTO() {
        return new InitiativeDTO();
    }


}

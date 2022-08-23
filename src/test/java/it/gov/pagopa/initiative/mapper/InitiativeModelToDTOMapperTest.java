package it.gov.pagopa.initiative.mapper;

import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = {
        InitiativeModelToDTOMapper.class})
@Slf4j
class InitiativeModelToDTOMapperTest {
    @Autowired
    InitiativeModelToDTOMapper initiativeModelToDTOMapper;
    private Initiative fullInitiative;
    private Initiative fullInitiative2;
    private List<Initiative> initiativeList;
    private InitiativeDTO fullInitiativeDTO;
    private InitiativeDTO fullInitiativeDTO2;
    private List<InitiativeDTO> initiativeDTOList;
    private InitiativeBeneficiaryRule initiativeBeneficiaryRule;
    private InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO;
    private InitiativeSummaryDTO initiativeSummaryDTO;
    private InitiativeSummaryDTO initiativeSummaryDTO2;
    private List<InitiativeSummaryDTO> initiativeSummaryDTOList;

    private InitiativeLegal initiativeLegal;

    @BeforeEach
    public void setUp() {
        fullInitiative = createFullInitiative();
        fullInitiative2 = createFullInitiative();
        initiativeList = new ArrayList<>();
        initiativeList.addAll(Arrays.asList(fullInitiative, fullInitiative2));
        fullInitiativeDTO = createFullInitiativeDTO();
        fullInitiativeDTO2 = createFullInitiativeDTO();
        initiativeDTOList = new ArrayList<>();
        initiativeDTOList.addAll(Arrays.asList(fullInitiativeDTO, fullInitiativeDTO2));
        initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiativeSummaryDTO = createInitiativeSummaryDTO();
        initiativeSummaryDTO2 = createInitiativeSummaryDTO();
        initiativeSummaryDTOList = new ArrayList<>();
        initiativeSummaryDTOList.addAll(Arrays.asList(initiativeSummaryDTO, initiativeSummaryDTO2));
        initiativeLegal = createInitiativeLegal();
    }

    @Test
    void toInitiativeDTO_ok(){
        InitiativeDTO initiativeDTOtoBeVerified = initiativeModelToDTOMapper.toInitiativeDTO(fullInitiative);

        //Check the equality of the results
        assertEquals(fullInitiativeDTO, initiativeDTOtoBeVerified);
    }

    @Test
    void toInitiativeLegal_ok() {
        assertEquals("https://", initiativeLegal.getDpiaLink().toString().substring(0, 8));
        assertEquals("https://", initiativeLegal.getPrivacyLink().toString().substring(0, 8));
        assertEquals("https://", initiativeLegal.getTcLink().toString().substring(0, 8));
        assertEquals("https://", initiativeLegal.getRegulationLink().toString().substring(0, 8));
    }
    @Test
    void toInitiativeDTONull_ok(){
        InitiativeDTO initiativeDTO = null;
        assertEquals(null, initiativeModelToDTOMapper.toInitiativeDTO(null));
    }

    @Test
    void toDtoOnlyId_ok(){
        InitiativeDTO initiativeDTOonlyId = new InitiativeDTO();
        initiativeDTOonlyId.setInitiativeId("Id1");
        InitiativeDTO initiativeDTOtoBeVerified = initiativeModelToDTOMapper.toDtoOnlyId(fullInitiative);
        //Check the equality of the results
        assertEquals(initiativeDTOonlyId, initiativeDTOtoBeVerified);
    }

    @Test
    void toDtoOnlyNull_ok(){
        assertEquals(null, initiativeModelToDTOMapper.toDtoOnlyId(null));
    }

    @Test
    void toInitiativeBeneficiaryRuleDTO_ok(){
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTOtoBeVerified = initiativeModelToDTOMapper.toInitiativeBeneficiaryRuleDTO(initiativeBeneficiaryRule);
        //Check the equality of the results
        assertEquals(initiativeBeneficiaryRuleDTO, initiativeBeneficiaryRuleDTOtoBeVerified);
    }

    @Test
    void toInitiativeBeneficiaryRuleDTONull_ok(){
        assertEquals(null, initiativeModelToDTOMapper.toInitiativeBeneficiaryRuleDTO(null));
    }
    @Test
    void toInitiativeSummaryDTOList_ok(){
        List<InitiativeSummaryDTO> initiativeSummaryDTOListActual = initiativeModelToDTOMapper.toInitiativeSummaryDTOList(initiativeList);
        //Check the equality of the results
        assertEquals(initiativeSummaryDTOList, initiativeSummaryDTOListActual);
    }

    Initiative createFullInitiative () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        return createStep2Initiative();
    }

    InitiativeDTO createFullInitiativeDTO () {
        //TODO Test onGoing for different steps. Must use Step6 at the end
        return createStep2InitiativeDTO();
    }

    Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId("Id1");
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId("organizationId1");
        initiative.setStatus("DRAFT");
        initiative.setAutocertificationCheck(true);
        initiative.setBeneficiaryRanking(true);
        initiative.setPdndCheck(true);
        initiative.setPdndToken("pdndToken1");

        initiative.setGeneral(createInitiativeGeneral());
        initiative.setAdditionalInfo(createInitiativeAdditional());
//        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
//        initiative.setLegal(createInitiativeLegal());
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral() {
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
        return initiativeGeneral;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setArgument("Argument");
        initiativeAdditional.setDescription("Description");
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

    private InitiativeLegal createInitiativeLegal() {
        InitiativeLegal initiativeLegal = new InitiativeLegal();
        initiativeLegal.setDpiaLink("https://www.google.it");
        initiativeLegal.setPrivacyLink("https://www.google.it");
        initiativeLegal.setRegulationLink("https://www.google.it");
        initiativeLegal.setTcLink("https://www.google.it");
        return initiativeLegal;
    }

    InitiativeDTO createStep1InitiativeDTO () {
        return InitiativeDTO.builder()
                .initiativeId("Id1")
                .initiativeName("initiativeName1")
                .organizationId("organizationId1")
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .pdndCheck(true)
                .pdndToken("pdndToken1")
                .general(createInitiativeGeneralDTO()).additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    InitiativeInfoDTO createStep1InitiativeInfoDTO() {
        return InitiativeInfoDTO.builder().general(createInitiativeGeneralDTO()).additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO() {
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
        return initiativeGeneralDTO;
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setArgument("Argument");
        initiativeAdditionalDTO.setDescription("Description");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    Initiative createStep2Initiative () {
        Initiative initiative = createStep1Initiative();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = createInitiativeBeneficiaryRule();
        initiative.setBeneficiaryRule(initiativeBeneficiaryRule);
        return initiative;
    }

    InitiativeDTO createStep2InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        InitiativeBeneficiaryRuleDTO initiativeBeneficiaryRuleDTO = createInitiativeBeneficiaryRuleDTO();
        initiativeDTO.setBeneficiaryRule(initiativeBeneficiaryRuleDTO);
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

    Initiative createStep3Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep3InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep4Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep4InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep5Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }

    Initiative createStep6Initiative () {
        Initiative initiative = new Initiative();
        return initiative;
    }

    InitiativeDTO createStep6InitiativeDTO () {
        InitiativeDTO initiativeDTO = new InitiativeDTO();
        return initiativeDTO;
    }


}

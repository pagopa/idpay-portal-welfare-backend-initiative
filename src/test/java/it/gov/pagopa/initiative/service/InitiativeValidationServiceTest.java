package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.*;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.RefundAdditionalInfoDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.dto.rule.reward.InitiativeRewardRuleDTO;
import it.gov.pagopa.initiative.dto.rule.reward.RewardValueDTO;
import it.gov.pagopa.initiative.dto.rule.trx.*;
import it.gov.pagopa.initiative.exception.custom.*;
import it.gov.pagopa.initiative.model.TypeBoolEnum;
import it.gov.pagopa.initiative.model.TypeMultiEnum;
import it.gov.pagopa.initiative.model.*;
import it.gov.pagopa.initiative.model.rule.refund.AccumulatedAmount;
import it.gov.pagopa.initiative.model.rule.refund.AdditionalInfo;
import it.gov.pagopa.initiative.model.rule.refund.InitiativeRefundRule;
import it.gov.pagopa.initiative.model.rule.refund.TimeParameter;
import it.gov.pagopa.initiative.model.rule.reward.RewardValue;
import it.gov.pagopa.initiative.model.rule.trx.InitiativeTrxConditions;
import it.gov.pagopa.initiative.model.rule.trx.Threshold;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Stream;

import static it.gov.pagopa.initiative.model.InitiativeGeneral.BeneficiaryTypeEnum.PF;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "app.initiative.ranking.gracePeriod=10"
        })
@SpringJUnitConfig
@ImportAutoConfiguration(classes = {InitiativeValidationServiceImpl.class, ValidationAutoConfiguration.class})
class InitiativeValidationServiceTest {

    private static final String INITIATIVE_ID = "initiativeId";
    private static final String INITIATIVE_NAME = "initiativeName1";
    private static final String ORGANIZATION_ID = "organizationId1";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String SERVICE_ID = "serviceId";
    private static final String ANY_ROLE = "ANY_ROLE";
    private static final String ADMIN_ROLE = "admin";
    private static final String PAGOPA_ADMIN_ROLE = "pagopa_admin";
    private static final String ISEE = "ISEE";
    public static final String API_KEY_CLIENT_ID = "apiKeyClientId";
    public static final String API_KEY_CLIENT_ASSERTION = "apiKeyClientAssertion";


    @Autowired
    InitiativeValidationService initiativeValidationService;

    @MockBean
    InitiativeRepository initiativeRepository;

    @MockBean
    AutomatedCriteria automatedCriteria;

    @Test
    void givenAdminRole_whenInitiativeStatusIsValid_thenOk() {
        Initiative step2Initiative = createStep2Initiative(true);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(step2Initiative));
        
        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ADMIN_ROLE);

        //Check the equality of the results
        assertEquals(Optional.of(step2Initiative).get(), initiative);

        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1)).retrieveInitiativeSummary(anyString());
    }

    @Test
    void whenInitiativeNotFound_then404isRaisedForInitiativeException() {
        //Instruct the Repo Mock to return Dummy Initiatives
        //Automatically doThrow InitiativeException for Optional.empty()
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.empty());

        InitiativeNotFoundException exception = assertThrows(InitiativeNotFoundException.class, () -> initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, ANY_ROLE));

        assertEquals(InitiativeConstants.Exception.NotFound.INITIATIVE_NOT_FOUND, exception.getCode());
        assertEquals(String.format("Initiative with initiativeId [%s] not found", INITIATIVE_ID), exception.getMessage());
    }

    @Test
    void givenPagoPaAdminRole_whenInitiativeStatusIsValid_thenOk() {
        Initiative step2Initiative = createStep2Initiative(true);
        step2Initiative.setStatus(InitiativeConstants.Status.IN_REVISION);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        Initiative initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, PAGOPA_ADMIN_ROLE);
        //Check the equality of the results
        assertEquals(Optional.of(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))

        clearInvocations(initiativeRepository);

        step2Initiative.setStatus(InitiativeConstants.Status.TO_CHECK);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, PAGOPA_ADMIN_ROLE);
        //Check the equality of the results
        assertEquals(Optional.of(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))

        clearInvocations(initiativeRepository);

        step2Initiative.setStatus(InitiativeConstants.Status.APPROVED);
        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(step2Initiative));
        //Try to call the Real Service (which is using the instructed Repo)
        initiative = initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, PAGOPA_ADMIN_ROLE);
        //Check the equality of the results
        assertEquals(Optional.of(step2Initiative).get(), initiative);
        // you are expecting repo to be called once with correct param
        verify(initiativeRepository).findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true); // same as: verify(initiativeRepository, times(1))
    }

    @Test
    void givenPagoPaAdmin_whenInitiativeUnprocessableForStatusNotValid_then400isRaisedForInitiativeException() {
        Initiative step2Initiative = createStep2Initiative(true);

        //Instruct the Repo Mock to return Dummy Initiatives
        when(initiativeRepository.findByOrganizationIdAndInitiativeIdAndEnabled(ORGANIZATION_ID, INITIATIVE_ID, true)).thenReturn(Optional.of(step2Initiative));

        AdminPermissionException exception = assertThrows(AdminPermissionException.class, () -> initiativeValidationService.getInitiative(ORGANIZATION_ID, INITIATIVE_ID, PAGOPA_ADMIN_ROLE));

        assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_ADMIN_ROLE_NOT_ALLOWED, exception.getCode());
        assertEquals(String.format("Admin permission not allowed for current initiative [%s]", step2Initiative.getInitiativeId()), exception.getMessage());
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNF_ok() {
        Initiative fullInitiative = createStep2Initiative(true);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        generalInfoInitiative.setFamilyUnitComposition(InitiativeConstants.FamilyUnitCompositionConstant.ANPR);
        fullInitiative.setGeneral(generalInfoInitiative);

        Executable executable = () -> initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(fullInitiative);
        assertDoesNotThrow(executable);
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNFAndFamilyUnitCompositionIsNull_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        generalInfoInitiative.setBeneficiaryType(PF);
        fullInitiative.setGeneral(generalInfoInitiative);

        try {
            initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(fullInitiative);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be unset because beneficiary type is not NF".formatted(fullInitiative.getInitiativeId()),e.getMessage());
        }
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNFAndFamilyUnitCompositionIsNotInpsOrAnpr_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setFamilyUnitComposition("TEST");

        try {
            initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(fullInitiative);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be set as 'INPS' or 'ANPR'".formatted(fullInitiative.getInitiativeId()),e.getMessage());
        }
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsPFAndFamilyUnitCompositionIsNotNull_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setBeneficiaryType(PF);


        try {
            initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(fullInitiative);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be unset because beneficiary type is not NF".formatted(fullInitiative.getInitiativeId()),e.getMessage());
        }
    }
    @ParameterizedTest
    @ValueSource(strings = {InitiativeConstants.FamilyUnitCompositionConstant.INPS, InitiativeConstants.FamilyUnitCompositionConstant.ANPR})
    void updateGeneralInfoWhenBeneficiaryTypeIsNFAndFieldISeeExist_ok(String familyUnit) {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        generalInfoInitiative.setFamilyUnitComposition(familyUnit);
        fullInitiative.setGeneral(generalInfoInitiative);
        InitiativeBeneficiaryRule beneficiaryInfoInitiative = createInitiativeBeneficiaryRule();
        fullInitiative.setBeneficiaryRule(beneficiaryInfoInitiative);
        List<AutomatedCriteria> automatedCriteriaList = fullInitiative.getBeneficiaryRule().getAutomatedCriteria();

        Executable executable = () -> initiativeValidationService.checkAutomatedCriteria(fullInitiative, automatedCriteriaList);
        assertDoesNotThrow(executable);

    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsPFAndFamilyUnitCompositionIsNull_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setBeneficiaryType(PF);
        fullInitiative.getGeneral().setFamilyUnitComposition(null);

        try {
            initiativeValidationService.checkBeneficiaryTypeAndFamilyUnit(fullInitiative);
        } catch (InitiativeFamilyUnitCompositionException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_FAMILY_UNIT_COMPOSITION_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] family unit composition must be set as 'INPS' or 'ANPR'".formatted(fullInitiative.getInitiativeId()),e.getMessage());
        }
    }
    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsNFFamilyUnitINPSAndISeeIsMissing_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        InitiativeBeneficiaryRule beneficiaryInfoInitiative = createInitiativeBeneficiaryRuleWithoutISEE();
        fullInitiative.setBeneficiaryRule(beneficiaryInfoInitiative);
        List<AutomatedCriteria> automatedCriteriaList = fullInitiative.getBeneficiaryRule().getAutomatedCriteria();

        try {
            initiativeValidationService.checkAutomatedCriteria(fullInitiative,automatedCriteriaList);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_BENEFICIARY_NF_ISEE_MISSING, e.getCode());
            assertEquals("Automated criteria for family initiative [%s] not valid because ISEE is missing".formatted(fullInitiative.getInitiativeId()) , e.getMessage());
        }
    }
    @Test
    void givenUpdateGeneralInfoWhenRankingInitiativeAndISeeIsMissing_ko() {
        Initiative fullInitiative = createFullInitiative(true);
        List<AutomatedCriteria> automatedCriteriaList = fullInitiative.getBeneficiaryRule().getAutomatedCriteria();
        for(AutomatedCriteria automatedCriteriaLocal : automatedCriteriaList) {
            automatedCriteriaLocal.setOperator(FilterOperatorEnumModel.NOT_EQ);
            automatedCriteriaLocal.setOrderDirection(AutomatedCriteria.OrderDirection.ASC);
            automatedCriteriaLocal.setCode("null");
        }

        try {
            initiativeValidationService.checkAutomatedCriteria(fullInitiative,automatedCriteriaList);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ISEE_MISSING, e.getCode());
            assertEquals("Automated criteria for ranking initiative [%s] not valid because ISEE is missing".formatted(fullInitiative.getInitiativeId()) , e.getMessage());
        }
    }

    @Test
    void updateGeneralInfoWhenBeneficiaryTypeIsPFAndISeeIsMissing_ko() {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        fullInitiative.getGeneral().setBeneficiaryType(PF);
        InitiativeBeneficiaryRule beneficiaryInfoInitiative = createInitiativeBeneficiaryRuleWithoutISEE();
        fullInitiative.setBeneficiaryRule(beneficiaryInfoInitiative);
        List<AutomatedCriteria> automatedCriteriaList = fullInitiative.getBeneficiaryRule().getAutomatedCriteria();

        try {
            initiativeValidationService.checkAutomatedCriteria(fullInitiative,automatedCriteriaList);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_BENEFICIARY_NF_ISEE_MISSING, e.getCode());
            assertEquals("Automated criteria for family initiative [%s] not valid because ISEE is missing".formatted(fullInitiative.getInitiativeId()) , e.getMessage());
        }
    }

    @Test
    void testCheckPermissionBeforeInsert() {
        assertThrows(AdminPermissionException.class,
                () -> initiativeValidationService.checkPermissionBeforeInsert("pagopa_admin"));
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking() {
        Initiative step3Initiative = createStep3Initiative(false);
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        Executable executable = () -> initiativeValidationService.checkAutomatedCriteria(step3Initiative, automatedCriteriaList);
        assertDoesNotThrow(executable);
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking2() {
        Initiative step3Initiative = createStep3Initiative(true);
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        assertThrows(AutomatedCriteriaNotValidException.class, () -> initiativeValidationService.checkAutomatedCriteria(step3Initiative, automatedCriteriaList));
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking3() {
        Initiative step3Initiative = createStep3Initiative_EQ();
        List<AutomatedCriteria> automatedCriteriaList = step3Initiative.getBeneficiaryRule().getAutomatedCriteria();
        assertThrows(AutomatedCriteriaNotValidException.class, () -> initiativeValidationService.checkAutomatedCriteria(step3Initiative, automatedCriteriaList));
    }

    @Test
    void testCheckAutomatedCriteriaOrderDirectionWithRanking_Exception() {
        Initiative step3Initiative = createStep3Initiative(true);
        AutomatedCriteria automatedCriteriaLocal = new AutomatedCriteria();
        automatedCriteriaLocal.setCode("ISEE");
        automatedCriteriaLocal.setIseeTypes(List.of(IseeTypologyEnum.ORDINARIO));
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaLocal);

        try {
            initiativeValidationService.checkAutomatedCriteria(step3Initiative,
                    automatedCriteriaList);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_ORDER_DIRECTION_MISSING , e.getCode());
            assertEquals("Automated criteria for ranking initiative [%s] not valid because OrderDirection is missing".formatted(step3Initiative.getInitiativeId()),e.getMessage());
        }
    }
    @Test
    void testCheckAutomatedCriteria_iseeTypeNotValid() {
        Initiative step3Initiative = createStep3Initiative(false);
        AutomatedCriteria automatedCriteriaLocal = new AutomatedCriteria();
        automatedCriteriaLocal.setCode(ISEE);
        automatedCriteriaLocal.setIseeTypes(null);
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaLocal);

        try {
            initiativeValidationService.checkAutomatedCriteria(step3Initiative,
                    automatedCriteriaList);
        } catch (AutomatedCriteriaNotValidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_AUTOMATED_CRITERIA_NOT_VALID_TYPOLOGY_ISEE_MISSING , e.getCode());
            assertEquals("Automated criteria not valid because typology ISEE is missing", e.getMessage());
        }
    }

    @Test
    void givenRankingEndDateAndStartDateNotValid_whenValidateAllWizardSteps_thenExceptionThrown() {
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO(true);
        assertThrows(ValidationWizardException.class, () -> initiativeValidationService.validateAllWizardSteps(step5InitiativeDTO));
    }

    @Test
    void givenInitiativeOk_whenValidateAllWizardSteps_thenDoNothing() {
        InitiativeDTO step5InitiativeDTO = createStep5InitiativeDTO(true);
        LocalDate startDate = step5InitiativeDTO.getGeneral().getStartDate();
        startDate = startDate.plusDays(20);
        LocalDate endDate = step5InitiativeDTO.getGeneral().getEndDate();
        endDate = endDate.plusDays(20);
        step5InitiativeDTO.getGeneral().setStartDate(startDate);
        step5InitiativeDTO.getGeneral().setEndDate(endDate);
        Executable executable = () -> initiativeValidationService.validateAllWizardSteps(step5InitiativeDTO);
        assertDoesNotThrow(executable);
    }

    @Test
    void checkRewardRuleAbsolute_noInstanceOf(){
        Initiative step4Initiative = createStep4Initiative(false);
        Executable executable = () -> initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        assertDoesNotThrow(executable);
    }
    @Test
    void checkRewardRuleAbsolute_noRewardAbsolute(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.PERCENTAGE);
        step4Initiative.setRewardRule(rewardValue);
        Executable executable = () -> initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        assertDoesNotThrow(executable);
    }
    @Test
    void checkRewardRuleAbsolute_thresholdNull(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE);
        step4Initiative.setRewardRule(rewardValue);
        InitiativeTrxConditions trxConditions = new InitiativeTrxConditions();
        trxConditions.setThreshold(null);
        step4Initiative.setTrxRule(trxConditions);
        try {
            initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        } catch (InvalidRewardRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, e.getCode());
            assertEquals("Reward rules of initiative [%s] is not valid".formatted(step4Initiative.getInitiativeId()), e.getMessage());
        }
    }

    @Test
    void checkRewardRuleAbsolute_thresholdFromNull(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE);
        step4Initiative.setRewardRule(rewardValue);
        InitiativeTrxConditions trxConditions = new InitiativeTrxConditions();
        trxConditions.setThreshold(new Threshold());
        step4Initiative.setTrxRule(trxConditions);
        try {
            initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        } catch (InvalidRewardRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, e.getCode());
            assertEquals("Reward rules of initiative [%s] is not valid".formatted(step4Initiative.getInitiativeId()), e.getMessage());
        }
    }
    @Test
    void checkRewardRuleAbsolute_thresholdFromWrong(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE);
        rewardValue.setRewardValue(BigDecimal.valueOf(40));
        step4Initiative.setRewardRule(rewardValue);
        InitiativeTrxConditions trxConditions = new InitiativeTrxConditions();
        Threshold threshold = new Threshold();
        threshold.setFromCents(3000L);
        trxConditions.setThreshold(threshold);
        step4Initiative.setTrxRule(trxConditions);
        try {
            initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        } catch (InvalidRewardRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, e.getCode());
            assertEquals("Reward rules of initiative [%s] is not valid".formatted(step4Initiative.getInitiativeId()), e.getMessage());
        }
    }

    @Test
    void checkRewardRuleAbsolute_thresholdOK(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE);
        rewardValue.setRewardValue(BigDecimal.valueOf(30));
        step4Initiative.setRewardRule(rewardValue);
        InitiativeTrxConditions trxConditions = new InitiativeTrxConditions();
        Threshold threshold = new Threshold();
        threshold.setFromCents(4000L);
        trxConditions.setThreshold(threshold);
        step4Initiative.setTrxRule(trxConditions);
        Executable executable = () -> initiativeValidationService.checkRewardRuleAbsolute(step4Initiative);
        assertDoesNotThrow(executable);
    }

    @Test
    void checkReward_PERCENTAGE_ok(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.PERCENTAGE);
        rewardValue.setRewardValue(BigDecimal.valueOf(10));
        step4Initiative.setRewardRule(rewardValue);
        Executable executable = () -> initiativeValidationService.checkReward(step4Initiative);
        assertDoesNotThrow(executable);
    }

    @Test
    void checkReward_PERCENTAGE_ko(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.PERCENTAGE);
        rewardValue.setRewardValue(BigDecimal.valueOf(105));
        step4Initiative.setRewardRule(rewardValue);
        try {
            initiativeValidationService.checkReward(step4Initiative);
            Assertions.fail();
        } catch (InvalidRewardRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, e.getCode());
            assertEquals("Reward rules of initiative [%s] is not valid".formatted(step4Initiative.getInitiativeId()), e.getMessage());
        }
    }

    @Test
    void checkReward_ABSOLUTE_ok(){
        Initiative step4Initiative = createStep4Initiative(false);
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValueType(RewardValue.RewardValueTypeEnum.ABSOLUTE);
        rewardValue.setRewardValue(BigDecimal.valueOf(105));
        step4Initiative.setRewardRule(rewardValue);
        Executable executable = () -> initiativeValidationService.checkReward(step4Initiative);
        assertDoesNotThrow(executable);
    }

    @Test
    void checkReward_noInstanceOf(){
        Initiative step4Initiative = createStep4Initiative(false);
        Executable executable = () -> initiativeValidationService.checkReward(step4Initiative);
        assertDoesNotThrow(executable);
    }

    @Test
    void checkRefundRuleDiscountInitiative_RefundType(){
        Initiative step5Initiative = createStep5Initiative(false);
        step5Initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.REFUND);
        Executable executable = () -> initiativeValidationService.checkRefundRuleDiscountInitiative(step5Initiative.getInitiativeRewardType().name(),
                new InitiativeRefundRule());
        assertDoesNotThrow(executable);
    }
    @Test
    void checkRefundRuleDiscountInitiative_discountType_noAccumulatedAmount(){
        Initiative step5Initiative = createStep5Initiative(false);
        step5Initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.DISCOUNT);
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setTimeParameter(new TimeParameter(TimeParameter.TimeTypeEnum.DAILY));
        Executable executable = () -> initiativeValidationService.checkRefundRuleDiscountInitiative(step5Initiative.getInitiativeRewardType().name(),
                refundRule);
        assertDoesNotThrow(executable);
    }
    @Test
    void checkRefundRuleDiscountInitiative_discountType_withAccumulatedAmount(){
        Initiative step5Initiative = createStep5Initiative(false);
        step5Initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.DISCOUNT);
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        accumulatedAmount.setAccumulatedType(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED);
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(accumulatedAmount);
        try {
            initiativeValidationService.checkRefundRuleDiscountInitiative(step5Initiative.getInitiativeRewardType().name(),
                    refundRule);
        } catch (InvalidRefundRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REFUND_RULES_NOT_VALID, e.getCode());
            assertEquals("Refund rules is not valid", e.getMessage());
        }
    }
    @Test
    void checkRefundRuleDiscountInitiative_discountType_noTimeParameter(){
        Initiative step4Initiative = createStep4Initiative(false);
        step4Initiative.setInitiativeRewardType(InitiativeDTO.InitiativeRewardTypeEnum.DISCOUNT);
        try {
            initiativeValidationService.checkRefundRuleDiscountInitiative(step4Initiative.getInitiativeRewardType().name(),
                    new InitiativeRefundRule());
        } catch (InvalidRefundRuleException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_REFUND_RULES_NOT_VALID, e.getCode());
            assertEquals("Refund rules is not valid", e.getMessage());
        }
    }
    @ParameterizedTest
    @MethodSource("rangeDate")
    void checkStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        Initiative step2Initiative = createStep2Initiative(false);
        InitiativeGeneral initiativeGeneral = createInitiativeGeneral(false);
        initiativeGeneral.setStartDate(startDate);
        initiativeGeneral.setEndDate(endDate);
        step2Initiative.setGeneral(initiativeGeneral);

        try {
            initiativeValidationService.checkStartDateAndEndDate(step2Initiative);
        } catch (InitiativeDateInvalidException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_START_DATE_AND_END_DATE_NOT_VALID, e.getCode());
            assertEquals("In the initiative [%s] the startDate and endDate cannot be less than today".formatted(step2Initiative.getInitiativeId()), e.getMessage());
        }
    }
    @ParameterizedTest
    @MethodSource("fieldsAndDate")
    void checkValuesWhenCodeIsBirthdateAndFieldIsYear(String code, String field, String value) {
        Initiative fullInitiative = createFullInitiative(false);
        InitiativeGeneral generalInfoInitiative = createInitiativeGeneralFamilyUnitComposition();
        fullInitiative.setGeneral(generalInfoInitiative);
        InitiativeBeneficiaryRule beneficiaryInfoInitiative = createInitiativeBeneficiaryRule();
        beneficiaryInfoInitiative.getAutomatedCriteria().get(0).setCode(code);
        beneficiaryInfoInitiative.getAutomatedCriteria().get(0).setField(field);
        beneficiaryInfoInitiative.getAutomatedCriteria().get(0).setValue(value);
        fullInitiative.setBeneficiaryRule(beneficiaryInfoInitiative);
        List<AutomatedCriteria> automatedCriteriaList = fullInitiative.getBeneficiaryRule().getAutomatedCriteria();

        try {
            initiativeValidationService.checkFieldYearLengthAndValues(automatedCriteriaList);
        } catch (InitiativeYearValueException e) {
            assertEquals(InitiativeConstants.Exception.BadRequest.INITIATIVE_YEAR_VALUE_NOT_VALID, e.getCode());
            assertEquals("In the initiative the value must contain 4 numbers and the year cannot be less than 150 years", e.getMessage());
        }
    }

    /*
     * ############### Step 1 ###############
     */

    private Initiative createStep1Initiative () {
        Initiative initiative = new Initiative();
        initiative.setInitiativeId(INITIATIVE_ID);
        initiative.setInitiativeName("initiativeName1");
        initiative.setOrganizationId(ORGANIZATION_ID);
        initiative.setStatus("DRAFT");
        initiative.setAdditionalInfo(createInitiativeAdditional());
        return initiative;
    }

    private InitiativeAdditional createInitiativeAdditional() {
        InitiativeAdditional initiativeAdditional = new InitiativeAdditional();
        initiativeAdditional.setServiceIO(true);
        initiativeAdditional.setServiceId(SERVICE_ID);
        initiativeAdditional.setServiceName("serviceName");
        initiativeAdditional.setServiceScope(InitiativeAdditional.ServiceScope.LOCAL);
        initiativeAdditional.setDescription("Description");
        initiativeAdditional.setPrivacyLink("https://www.google.it");
        initiativeAdditional.setTcLink("https://www.google.it");
        Channel channel = new Channel();
        channel.setType(Channel.TypeEnum.EMAIL);
        channel.setContact("contact");
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        initiativeAdditional.setChannels(channels);
        return initiativeAdditional;
    }

    InitiativeDTO createStep1InitiativeDTO () {
        return InitiativeDTO.builder()
                .initiativeId(INITIATIVE_ID)
                .initiativeName("initiativeName1")
                .organizationId(ORGANIZATION_ID)
                .status("DRAFT")
                .autocertificationCheck(true)
                .beneficiaryRanking(true)
                .additionalInfo(createInitiativeAdditionalDTO()).build();
    }

    InitiativeAdditionalDTO createStep1InitiativeAdditionalDTO() {
        return createInitiativeAdditionalDTO();
    }

    private InitiativeAdditionalDTO createInitiativeAdditionalDTO() {
        InitiativeAdditionalDTO initiativeAdditionalDTO = new InitiativeAdditionalDTO();
        initiativeAdditionalDTO.setServiceIO(true);
        initiativeAdditionalDTO.setServiceId("serviceId");
        initiativeAdditionalDTO.setServiceName("serviceName");
        initiativeAdditionalDTO.setServiceScope(InitiativeAdditionalDTO.ServiceScope.LOCAL);
        initiativeAdditionalDTO.setDescription("Description");
        initiativeAdditionalDTO.setPrivacyLink("https://www.google.it");
        initiativeAdditionalDTO.setTcLink("https://www.google.it");
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setType(ChannelDTO.TypeEnum.EMAIL);
        channelDTO.setContact("contact");
        List<ChannelDTO> channelDTOS = new ArrayList<>();
        channelDTOS.add(channelDTO);
        initiativeAdditionalDTO.setChannels(channelDTOS);
        return initiativeAdditionalDTO;
    }

    /*
     * ############### Step 2 ###############
     */

    private Initiative createStep2Initiative (Boolean rankingEnabled) {
        Initiative initiative = createStep1Initiative();
        initiative.setGeneral(createInitiativeGeneral(rankingEnabled));
        return initiative;
    }

    private InitiativeGeneral createInitiativeGeneral(Boolean rankingEnabled) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudgetCents(1000L);
        initiativeGeneral.setBeneficiaryKnown(false);
        initiativeGeneral.setBeneficiaryType(PF);
        initiativeGeneral.setBudgetCents(100000000000L);
        LocalDate rankingStartDate = LocalDate.now();
        LocalDate rankingEndDate = rankingStartDate.plusDays(1);
        LocalDate startDate = rankingEndDate.plusDays(1);
        LocalDate endDate = startDate.plusDays(1);
        initiativeGeneral.setRankingStartDate(rankingStartDate);
        initiativeGeneral.setRankingEndDate(rankingEndDate);
        initiativeGeneral.setStartDate(startDate);
        initiativeGeneral.setEndDate(endDate);
        initiativeGeneral.setRankingEnabled(rankingEnabled);
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }

    private InitiativeDTO createStep2InitiativeDTO (Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep1InitiativeDTO();
        initiativeDTO.setGeneral(createInitiativeGeneralDTO(rankingEnabled));
        return initiativeDTO;
    }

    private InitiativeGeneralDTO createInitiativeGeneralDTO(Boolean rankingEnabled) {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneralDTO initiativeGeneralDTO = new InitiativeGeneralDTO();
        initiativeGeneralDTO.setBeneficiaryBudget(new BigDecimal(10));
        initiativeGeneralDTO.setBeneficiaryKnown(false);
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
        initiativeGeneralDTO.setRankingEnabled(rankingEnabled);
        initiativeGeneralDTO.setDescriptionMap(language);
        return initiativeGeneralDTO;
    }

    /*
     * ############### Step 3 ###############
     */

    private Initiative createStep3Initiative (Boolean rankingEnabled) {
        Initiative initiative = createStep2Initiative(rankingEnabled);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule());
        return initiative;
    }

    private Initiative createStep3Initiative_EQ () {
        Initiative initiative = createStep2Initiative(true);
        initiative.setBeneficiaryRule(createInitiativeBeneficiaryRule_EQ());
        return initiative;
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
        AutomatedCriteria automatedCriteriaLocal = new AutomatedCriteria();
        automatedCriteriaLocal.setAuthority("INPS");
        automatedCriteriaLocal.setCode(ISEE);
        automatedCriteriaLocal.setField("true");
        automatedCriteriaLocal.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteriaLocal.setValue("value");
        automatedCriteriaLocal.setIseeTypes(List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.DOTTORATO, IseeTypologyEnum.RESIDENZIALE));
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaLocal);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }
    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRuleWithoutISEE() {
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
        AutomatedCriteria automatedCriteriaLocal = new AutomatedCriteria();
        automatedCriteriaLocal.setCode("BIRTHDATE");
        automatedCriteriaLocal.setField("Year");
        automatedCriteriaLocal.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteriaLocal.setValue("value");
        automatedCriteriaLocal.setIseeTypes(List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.DOTTORATO, IseeTypologyEnum.RESIDENZIALE));
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaLocal);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        return initiativeBeneficiaryRule;
    }

    private InitiativeBeneficiaryRule createInitiativeBeneficiaryRule_EQ() {
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
        AutomatedCriteria automatedCriteriaLocal = new AutomatedCriteria();
        automatedCriteriaLocal.setAuthority("INPS");
        automatedCriteriaLocal.setCode(ISEE);
        automatedCriteriaLocal.setField("true");
        automatedCriteriaLocal.setOperator(FilterOperatorEnumModel.EQ);
        automatedCriteriaLocal.setValue("value");
        automatedCriteriaLocal.setOrderDirection(AutomatedCriteria.OrderDirection.ASC);
        automatedCriteriaLocal.setIseeTypes(List.of(IseeTypologyEnum.CORRENTE, IseeTypologyEnum.MINORENNE));
        List<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        automatedCriteriaList.add(automatedCriteriaLocal);
        initiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        initiativeBeneficiaryRule.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRule.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRule;
    }

    private InitiativeDTO createStep3InitiativeDTO (Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep2InitiativeDTO(rankingEnabled);
        initiativeDTO.setBeneficiaryRule(createInitiativeBeneficiaryRuleDTO());
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
        initiativeBeneficiaryRuleDTO.setApiKeyClientId(API_KEY_CLIENT_ID);
        initiativeBeneficiaryRuleDTO.setApiKeyClientAssertion(API_KEY_CLIENT_ASSERTION);
        return initiativeBeneficiaryRuleDTO;
    }

    @Test
    void testCheckPermissionBeforeInsert2() {
        assertThrows(AdminPermissionException.class,
                () -> initiativeValidationService.checkPermissionBeforeInsert("pagopa_admin"));
    }

    /*
     * ############### Step 4 ###############
     */

    private Initiative createStep4Initiative (Boolean rankingEnabled) {
        return createStep3Initiative(rankingEnabled);
    }

    private InitiativeDTO createStep4InitiativeDTO (Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep3InitiativeDTO(rankingEnabled);
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = createInitiativeRewardAndTrxRulesDTO();
        initiativeDTO.setRewardRule(initiativeRewardAndTrxRulesDTO.getRewardRule());
        initiativeDTO.setTrxRule(initiativeRewardAndTrxRulesDTO.getTrxRule());
        return initiativeDTO;
    }

    private InitiativeRewardAndTrxRulesDTO createInitiativeRewardAndTrxRulesDTO() {
        InitiativeRewardAndTrxRulesDTO initiativeRewardAndTrxRulesDTO = new InitiativeRewardAndTrxRulesDTO();
        initiativeRewardAndTrxRulesDTO.setRewardRule(createInitiativeRewardRuleDTO_RewardValueDTO());
        initiativeRewardAndTrxRulesDTO.setTrxRule(createInitiativeTrxConditionsDTOValid());
        return initiativeRewardAndTrxRulesDTO;
    }

    private InitiativeRewardRuleDTO createInitiativeRewardRuleDTO_RewardValueDTO(){
        return RewardValueDTO.builder()
                .rewardValue(BigDecimal.valueOf(50))
                .type("rewardValue")
                .rewardValueType(RewardValueDTO.RewardValueTypeEnum.PERCENTAGE)
                .build();
    }

    private InitiativeTrxConditionsDTO createInitiativeTrxConditionsDTOValid(){
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


    /*
     * ############### Step 5 ###############
     */

    private Initiative createStep5Initiative (Boolean rankingEnabled) {
        Initiative initiative = createStep4Initiative(rankingEnabled);
        initiative.setRefundRule(createRefundRuleValidWithTimeParameter());
        return initiative;
    }

    private InitiativeRefundRule createRefundRuleValidWithTimeParameter(){
        InitiativeRefundRule refundRule = new InitiativeRefundRule();
        refundRule.setAccumulatedAmount(null);
        refundRule.setTimeParameter(createTimeParameter_Valid());
        refundRule.setAdditionalInfo(createAdditionalInfo_Valid());
        return refundRule;
    }

    private TimeParameter createTimeParameter_Valid(){
        TimeParameter timeParameter = new TimeParameter();
        timeParameter.setTimeType(TimeParameter.TimeTypeEnum.CLOSED);
        return timeParameter;
    }

    private AdditionalInfo createAdditionalInfo_Valid(){
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setIdentificationCode("B002");
        return additionalInfo;
    }

    private InitiativeDTO createStep5InitiativeDTO () {
        InitiativeDTO initiativeDTO = createStep5InitiativeDTO(false);
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }

    private InitiativeRefundRuleDTO createRefundRuleDTOValidWithTimeParameter(){
        InitiativeRefundRuleDTO refundRuleDTO = new InitiativeRefundRuleDTO();

        refundRuleDTO.setOrganizationName(ORGANIZATION_NAME);
        refundRuleDTO.setOrganizationUserRole(ADMIN_ROLE);

        refundRuleDTO.setAccumulatedAmount(null);
        refundRuleDTO.setTimeParameter(createTimeParameterDTO_Valid());
        refundRuleDTO.setAdditionalInfo(createAdditionalInfoDTOValid());
        return refundRuleDTO;
    }

    private TimeParameterDTO createTimeParameterDTO_Valid(){
        TimeParameterDTO timeParameterDTO = new TimeParameterDTO();
        timeParameterDTO.setTimeType(TimeParameterDTO.TimeTypeEnum.CLOSED);
        return timeParameterDTO;
    }

    private RefundAdditionalInfoDTO createAdditionalInfoDTOValid(){
        RefundAdditionalInfoDTO refundAdditionalInfoDTO = new RefundAdditionalInfoDTO();
        refundAdditionalInfoDTO.setIdentificationCode("B002");
        return refundAdditionalInfoDTO;
    }

    private InitiativeDTO createStep5InitiativeDTO (Boolean rankingEnabled) {
        InitiativeDTO initiativeDTO = createStep4InitiativeDTO(rankingEnabled);
        initiativeDTO.setRefundRule(createRefundRuleDTOValidWithTimeParameter());
        return initiativeDTO;
    }
    Initiative createFullInitiative(Boolean rankingEnabled) {
        return createStep5Initiative(rankingEnabled);
    }
    private InitiativeGeneral createInitiativeGeneralFamilyUnitComposition() {
        Map<String, String> language = new HashMap<>();
        language.put(Locale.ITALIAN.getLanguage(), "it");
        InitiativeGeneral initiativeGeneral = new InitiativeGeneral();
        initiativeGeneral.setBeneficiaryBudgetCents(1000L);
        initiativeGeneral.setBeneficiaryKnown(true);
        initiativeGeneral.setBeneficiaryType(InitiativeGeneral.BeneficiaryTypeEnum.NF);
        initiativeGeneral.setFamilyUnitComposition(InitiativeConstants.FamilyUnitCompositionConstant.INPS);
        initiativeGeneral.setBudgetCents(100000000000L);
        initiativeGeneral.setEndDate(LocalDate.of(2022, 9, 8));
        initiativeGeneral.setStartDate(LocalDate.of(2022, 8, 8));
        initiativeGeneral.setRankingStartDate(LocalDate.of(2022, 9, 18));
        initiativeGeneral.setRankingEndDate(LocalDate.of(2022, 8, 18));
        initiativeGeneral.setDescriptionMap(language);
        return initiativeGeneral;
    }
    private static Stream<Arguments> rangeDate() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(5),LocalDate.now().minusDays(2)),
                Arguments.of(LocalDate.now(), LocalDate.now().minusDays(2)),
                Arguments.of(LocalDate.now().minusDays(2),LocalDate.now()),
                Arguments.of(LocalDate.now(),LocalDate.now().plusDays(5)));
    }

    private static Stream<Arguments> fieldsAndDate() {
        return Stream.of(
                Arguments.of("BIRTHDATE","Year", Year.now().toString()),
                Arguments.of("BIRTHDATE","Year", Year.now().minusYears(180).toString()),
                Arguments.of("BIRTHDATE","Year", "120"),
                Arguments.of("BIRTHDATE","Year", Year.now().plusYears(10).toString()),
                Arguments.of("ISEE","Year", Year.now().toString()),
                Arguments.of("ISEE","Year", Year.now().minusYears(180).toString()),
                Arguments.of("ISEE","Year", "120"),
                Arguments.of("BIRTHDATE","Age", Year.now().toString()),
                Arguments.of("BIRTHDATE","Age", Year.now().minusYears(180).toString()),
                Arguments.of("BIRTHDATE","Age", "120")
        );
    }
}

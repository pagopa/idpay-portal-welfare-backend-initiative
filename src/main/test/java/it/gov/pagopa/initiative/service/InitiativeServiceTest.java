package it.gov.pagopa.initiative.service;


import it.gov.pagopa.initiative.repository.InitiativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {
        InitiativeService.class})
@Slf4j
class InitiativeServiceTest {

    @Autowired
    InitiativeService initiativeService;

    @MockBean
    InitiativeRepository initiativeRepository;

}

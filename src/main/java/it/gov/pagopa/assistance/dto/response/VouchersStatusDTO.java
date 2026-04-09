package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class VouchersStatusDTO {

    private String name;
    private String surname;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private String status;

    private List<Operation> operations;
    private List<TransactionDTO> transactions;
}

package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Builder
@Data
public class VouchersStatusDTO {

    private String name;
    private String surname;
    private Instant issueDate;
    private Instant expirationDate;
    private String status;

    private List<Operation> operations;
    private List<TransactionDTO> transactions;
}

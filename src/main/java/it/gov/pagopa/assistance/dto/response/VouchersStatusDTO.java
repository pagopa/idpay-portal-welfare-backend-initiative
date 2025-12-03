package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.dto.request.Operation;
import it.gov.pagopa.assistance.dto.request.TimelineDTO;
import it.gov.pagopa.assistance.dto.request.TransactionDTO;
import it.gov.pagopa.assistance.enums.PointOfSaleTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class VouchersStatusDTO {

    private String name;
    private String surname;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private String status;

    private List<Operation> operations;
    private List<TransactionDTO> transactions;
}

package it.gov.pagopa.assistance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionProcessed {
    private String id;

    private String idTrxAcquirer;

    private String acquirerCode;

    private Instant trxDate;

    private String operationType;

    private String acquirerId;

    private String userId;

    private String correlationId;

    private Long amountCents;

    private Map<String, Reward> rewards;

    private Long effectiveAmountCents;

    private Instant trxChargeDate;
    private String operationTypeTranscoded;

    private Instant timestamp;
    private Map<String, String> additionalProperties;
    private InvoiceFile invoiceFile;
}


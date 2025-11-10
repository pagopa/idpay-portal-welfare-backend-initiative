package it.gov.pagopa.assistance.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.assistance.enums.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WalletDTO {

    private String familyId;
    private String initiativeId;
    private String initiativeName;
    private String status;
    private String voucherStatus;
    private String iban;
    private LocalDate initiativeEndDate;
    private LocalDate voucherStartDate;
    private LocalDate voucherEndDate;
    @JsonProperty("nInstr")
    private int nInstr;
    private Long amountCents;
    private Long accruedCents;
    private Long refundedCents;
    private LocalDateTime lastCounterUpdate;
    private String initiativeRewardType;
    private String logoURL;
    private String organizationName;
    private Long nTrx;
    private Long maxTrx;
    private Long counterVersion;
    private List<Long> counterHistory;
    private String serviceId;
    private String userMail;
    private Channel channel;
    private String name;
    private String surname;
}

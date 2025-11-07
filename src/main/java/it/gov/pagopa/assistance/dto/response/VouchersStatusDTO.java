package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.enums.PointOfSaleTypeEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class VouchersStatusDTO {

    private PointOfSaleTypeEnum typeOfStore;
    private String name;
    private String surname;
    private String status;
    private String merchant;
    private String merchantAddress;
    private String merchantCity;
    private String phoneNumber;
    private String goodDescription;
    private Long goodAmount;
    private Long amountUsed;
    private Long maxDiscountAmount;
    private LocalDateTime dateOfUse;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;

}

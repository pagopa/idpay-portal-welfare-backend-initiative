package it.gov.pagopa.assistance.dto.response;

import it.gov.pagopa.assistance.enums.PointOfSaleTypeEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class VouchersStatusDTO {


    private String name;
    private String surname;
    private LocalDateTime dateOfBirth;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private Long maxDiscountAmount;
    private String status;
    private LocalDateTime dateOfUse;
    private Long amountUsed;
    private PointOfSaleTypeEnum typeOfStore;
    private String merchant;
    private String merchantAddress;
    private String merchantCity;
    private String phoneNumber;
    private Long goodAmount;
    private String goodDescription;

}

package it.gov.pagopa.initiative.dto.email_notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {

    private String templateName;
    private Map<String, String> templateValues;
    private String subject;
    private String content;
    private String senderEmail;
    private String recipientEmail;

}

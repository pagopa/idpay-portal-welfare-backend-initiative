package it.gov.pagopa.assistance.utlis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

public class Utils {
    private Utils(){}

    public static String extractMessageFromFeignException(FeignException feignException) {
        try {
            String content = feignException.contentUTF8();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            JsonNode messageNode = root.path("message");
            return messageNode.isMissingNode() ? "Unknown error" : messageNode.asText();
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}

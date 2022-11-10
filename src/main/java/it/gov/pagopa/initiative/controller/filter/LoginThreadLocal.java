package it.gov.pagopa.initiative.controller.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LoginThreadLocal {
    private ThreadLocal<Map<String, String>> myThreadLocal = new ThreadLocal<>();
}
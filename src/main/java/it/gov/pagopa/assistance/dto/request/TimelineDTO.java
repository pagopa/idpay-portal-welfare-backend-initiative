package it.gov.pagopa.assistance.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimelineDTO {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private LocalDateTime lastUpdate;
  private List<Operation> operationList;
  private int pageNo;
  private int pageSize;
  private int totalElements;
  private int totalPages;

}


package in.ashokit.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EligResponse {

	private Integer caseNum;
	private String holderName;
	private Long holderSsn;
	private String planName;
	private String planStatus;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double benefitAmount;
	private String denialReason;

}

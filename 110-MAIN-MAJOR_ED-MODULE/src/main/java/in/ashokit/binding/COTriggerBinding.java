package in.ashokit.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class COTriggerBinding {

	private Integer coTriggerId;
	private Integer caseNum;
	private byte[] coPdf;
	private String triggerStatus;

}

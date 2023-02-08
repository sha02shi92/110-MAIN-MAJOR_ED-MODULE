package in.ashokit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "CO_TRIGGERS")
@Entity
public class COTriggerEntity {

	@Id
	@GeneratedValue
	private Integer coTriggerId;
	private Long caseNum;
	private byte[] coPdf;
	private String triggerStatus;

}

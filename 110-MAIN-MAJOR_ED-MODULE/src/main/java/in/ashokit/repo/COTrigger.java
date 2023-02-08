package in.ashokit.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.COTriggerEntity;

public interface COTrigger extends JpaRepository<COTriggerEntity, Serializable> {

}

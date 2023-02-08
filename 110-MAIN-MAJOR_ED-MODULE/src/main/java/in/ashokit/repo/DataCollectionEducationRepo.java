package in.ashokit.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.EducationDetailsEntity;

public interface DataCollectionEducationRepo extends JpaRepository<EducationDetailsEntity, Serializable> {

	public EducationDetailsEntity findByCaseNum(Long caseNum);
}

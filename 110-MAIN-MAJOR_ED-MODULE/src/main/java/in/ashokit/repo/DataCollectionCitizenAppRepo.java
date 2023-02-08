package in.ashokit.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.CitizenAppEntity;

public interface DataCollectionCitizenAppRepo extends JpaRepository<CitizenAppEntity, Serializable> {

}

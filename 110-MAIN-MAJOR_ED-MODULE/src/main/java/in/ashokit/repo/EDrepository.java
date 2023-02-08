package in.ashokit.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.EDEntity;

public interface EDrepository extends JpaRepository<EDEntity, Serializable> {

}

package io.molnarsandor.trelloclone.projectTask;

import io.molnarsandor.trelloclone.projectTask.model.BacklogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<BacklogEntity, Long> {

    BacklogEntity findByProjectIdentifierIgnoreCase(String identifier);
}

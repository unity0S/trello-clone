package io.molnarsandor.trelloclone.project_task;

import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<BacklogEntity, Long> {

    BacklogEntity findByProjectIdentifierIgnoreCase(String identifier);
}

package io.molnarsandor.trelloclone.projectTask;

import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository  extends CrudRepository<ProjectTaskEntity, Long> {

    List<ProjectTaskEntity> findByProjectIdentifierIgnoreCaseOrderByPriority(String id);

    ProjectTaskEntity findByProjectSequence(String sequence);
}

package io.molnarsandor.trelloclone.project_task;

import io.molnarsandor.trelloclone.project_task.model.ProjectTaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository  extends CrudRepository<ProjectTaskEntity, Long> {

    List<ProjectTaskEntity> findByProjectIdentifierIgnoreCaseOrderByPriority(String id);

    ProjectTaskEntity findByProjectSequence(String sequence);
}

package io.molnarsandor.trelloclone.projectTask;

import io.molnarsandor.trelloclone.projectTask.model.ProjectTaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTaskRepository  extends CrudRepository<ProjectTaskEntity, Long> {

    List<ProjectTaskEntity> findByProjectIdentifierIgnoreCaseOrderByPriority(String id);

    @Override
    Optional<ProjectTaskEntity> findById(@NonNull Long id);
}

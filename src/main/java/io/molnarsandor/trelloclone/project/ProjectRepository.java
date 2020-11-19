package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {

    ProjectEntity findByProjectIdentifierIgnoreCase(String projectId);

    @Override
    List<ProjectEntity> findAll();

    List<ProjectEntity> findAllByProjectLeader(String username);

    List<ProjectEntity> findAllByCollaborators(CollaboratorEntity collaboratorEntity);
}

package io.molnarsandor.pmtool.repositories;

import io.molnarsandor.pmtool.domain.entity.Collaborator;
import io.molnarsandor.pmtool.domain.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifierIgnoreCase(String projectId);

    @Override
    List<Project> findAll();

    List<Project> findAllByProjectLeader(String username);

    List<Project> findAllByCollaborators(Collaborator collaborator);
}

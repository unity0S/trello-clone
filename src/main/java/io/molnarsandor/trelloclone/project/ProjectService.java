package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;

import java.util.List;

public interface ProjectService {

    ProjectEntity findProjectByIdentifier(final String projectId, final String username);
    ProjectDTO findProjectByIdentifierDTO(final String projectId, final String username);
    ProjectDTO saveOrUpdateProject(final ProjectDTO projectDTO, final String username);
    List<ProjectDTO> findAllProject(final String username);
    DeleteDTO deleteProjectByIdentifier(final String projectId, final String username);
}

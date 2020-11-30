package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.collaborator.CollaboratorRepository;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project.exceptions.ProjectAlreadyExistsException;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.user.UserRepository;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final CollaboratorRepository collaboratorRepository;

    private final ModelConverter modelConverter;

    public ProjectEntity findProjectByIdentifier(final String projectId, final String username) {

        ProjectEntity projectEntity = getProjectById(projectId);

        validateProjectWithCollaborators(projectEntity, projectId, username);

        return projectEntity;
    }

    public ProjectDTO findProjectByIdentifierDTO(final String projectId, final String username) {

        ProjectEntity projectEntity = getProjectById(projectId);

        validateProjectWithCollaborators(projectEntity, projectId, username);

        return modelConverter.projectEntityToDto(projectEntity);
    }

    public ProjectDTO saveOrUpdateProject(final ProjectDTO projectDTO, final String username) {

        ProjectEntity projectEntity;
        ProjectEntity existingProjectEntity;

        if (projectDTO.getId() == null) {
            // new project
            projectEntity = modelConverter.projectDtoToEntity(projectDTO);
            existingProjectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier());

            if (existingProjectEntity != null) {
                throw new ProjectAlreadyExistsException("Project with Identifier: " + existingProjectEntity.getProjectIdentifier() + " already exists. Must be unique");
            }

            UserEntity userEntity = userRepository.findByEmail(username);
            projectEntity.setUser(userEntity);
            projectEntity.setProjectLeader(userEntity.getEmail());
            projectEntity.setProjectIdentifier(projectEntity.getProjectIdentifier().toUpperCase());

        } else {
            // update project
            existingProjectEntity = projectRepository.findById(projectDTO.getId()).orElse(null);

            validateProject(existingProjectEntity, projectDTO.getProjectIdentifier(), username);

            projectEntity = projectRepository.findById(projectDTO.getId()).orElse(null);

            validateProject(projectEntity, projectDTO.getProjectIdentifier(), username);

            projectEntity.setProjectName(projectDTO.getProjectName());
            projectEntity.setDescription(projectDTO.getDescription());
        }

        return modelConverter.projectEntityToDto(
                projectRepository.save(projectEntity));
    }

    public List<ProjectDTO> findAllProject(final String username) {

        List<ProjectEntity> byLeader = projectRepository.findAllByProjectLeader(username);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByEmail(username);

        List<ProjectEntity> byCollaborator;

        if (collaboratorEntity != null) {
            byCollaborator = projectRepository.findAllByCollaborators(collaboratorEntity);
        } else {
            byCollaborator = null;
        }

        if (byCollaborator != null) {
            byLeader.addAll(byCollaborator);
        }

        return modelConverter.projectEntityListToDto(byLeader);
    }

    public DeleteDTO deleteProjectByIdentifier(final String projectId, final String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));

        return new DeleteDTO("Project " + projectId + " deleted");
    }

    private void validateProject(final ProjectEntity projectEntity, final String projectIdentifier, final String username) {

        if (projectEntity != null && !projectEntity.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        } else if (projectEntity == null) {
            throw new ProjectNotFoundException("Project with ID: '" + projectIdentifier + "' cannot be updated because it does not exist");
        }
    }

    private void validateProjectWithCollaborators(final ProjectEntity projectEntity, final String projectId, final String username) {

        if (projectEntity == null) {
            throw new ProjectNotFoundException("Project ID '" + projectId + "' does not exists");
        }

        Set<CollaboratorEntity> collaboratorEntities = projectEntity.getCollaborators();

        boolean collaboratorExists;
        if (!collaboratorEntities.isEmpty()) {
            collaboratorExists = collaboratorEntities
                    .stream()
                    .anyMatch(collaborator ->
                                collaborator.getProjectIdentifier().equalsIgnoreCase(projectId) &&
                                collaborator.getEmail().equals(username));
        } else {
            collaboratorExists = false;
        }

        if (!projectEntity.getProjectLeader().equals(username) && !collaboratorExists) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
    }

    private ProjectEntity getProjectById(final String projectId) {

        return projectRepository.findByProjectIdentifierIgnoreCase(projectId);
    }
}

package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.collaborator.CollaboratorRepository;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectDTO;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project_task.BacklogRepository;
import io.molnarsandor.trelloclone.project_task.model.BacklogEntity;
import io.molnarsandor.trelloclone.user.UserRepository;
import io.molnarsandor.trelloclone.user.model.UserEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final BacklogRepository backlogRepository;

    private final UserRepository userRepository;

    private final CollaboratorRepository collaboratorRepository;

    private final ModelConverter modelConverter;

    // == PUBLIC METHODS ==
    public ProjectEntity findProjectByIdentifier(String projectId, String username) {

        return getProjectById(projectId, username);
    }

    public ProjectDTO findProjectByIdentifierDTO(String projectId, String username) {

        return modelConverter.projectEntityToDto(
                getProjectById(projectId, username));
    }

    public ProjectDTO saveOrUpdateProject(ProjectDTO projectDTO, String username) {

        ProjectEntity projectEntity = modelConverter.projectDtoToEntity(projectDTO);

        if (projectEntity.getId() != null) {
            ProjectEntity existingProjectEntity;

            existingProjectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier());

            validateProject(existingProjectEntity, projectEntity.getProjectIdentifier(), username);
        }


        UserEntity userEntity = userRepository.findByEmail(username);
        projectEntity.setUser(userEntity);
        projectEntity.setProjectLeader(userEntity.getEmail());
        projectEntity.setProjectIdentifier(projectEntity.getProjectIdentifier().toUpperCase());

        if (projectEntity.getId() == null) {
            BacklogEntity backlogEntity = new BacklogEntity();
            projectEntity.setBacklog(backlogEntity);
            backlogEntity.setProject(projectEntity);
            backlogEntity.setProjectIdentifier(projectEntity.getProjectIdentifier().toUpperCase());
        }

        if (projectEntity.getId() != null) {
            projectEntity.setBacklog(backlogRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier().toUpperCase()));
        }

        return modelConverter.projectEntityToDto(
                projectRepository.save(projectEntity));

    }

    public List<ProjectDTO> findAllProject(String username) {

        List<ProjectEntity> byLeader = projectRepository.findAllByProjectLeader(username);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByEmail(username);

        List<ProjectEntity> byCollaborator = projectRepository.findAllByCollaborators(collaboratorEntity);

        byLeader.addAll(byCollaborator);

        return modelConverter.projectEntityListToDto(byLeader);
    }

    public DeleteDTO deleteProjectByIdentifier(String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));

        return new DeleteDTO("Project " + projectId + " deleted");
    }

    // == PRIVATE METHODS ==
    private void validateProject(ProjectEntity projectEntity, String projectIdentifier, String username) {
        if (projectEntity != null && !projectEntity.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        } else if (projectEntity == null) {
            throw new ProjectNotFoundException("Project with ID: '" + projectIdentifier + "' cannot be updated because it does not exist");
        }
    }

    private void validateProjectWithCollaborators(ProjectEntity projectEntity, String projectId, String username) {
        if (projectEntity == null) {
            throw new ProjectNotFoundException("Project ID '" + projectId + "' does not exists");
        }

        Set<CollaboratorEntity> collaboratorEntities = projectEntity.getCollaborators();

        Predicate<CollaboratorEntity> collaboratorPredicate = collaborator ->
                collaborator.getProjectIdentifier().equalsIgnoreCase(projectId) &&
                        collaborator.getEmail().equals(username);


        boolean collaboratorExists = false;
        if (!collaboratorEntities.isEmpty()) {
            collaboratorExists = collaboratorEntities
                    .stream()
                    .anyMatch(collaboratorPredicate);
        }

        if (!projectEntity.getProjectLeader().equals(username) && !collaboratorExists) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
    }

    private ProjectEntity getProjectById(String projectId, String username) {
        ProjectEntity projectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectId);

        validateProjectWithCollaborators(projectEntity, projectId, username);

        return projectEntity;
    }
}

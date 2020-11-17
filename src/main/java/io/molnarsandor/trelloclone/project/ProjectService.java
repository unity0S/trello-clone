package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.collaborator.CollaboratorEntity;
import io.molnarsandor.trelloclone.collaborator.CollaboratorRepository;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project_task.BacklogEntity;
import io.molnarsandor.trelloclone.project_task.BacklogRepository;
import io.molnarsandor.trelloclone.user.UserEntity;
import io.molnarsandor.trelloclone.user.UserRepository;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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

    // == PUBLIC METHODS ==
    public ProjectEntity findProjectByIdentifier(String projectId, String username) {

        ProjectEntity projectEntity;

        try {
            projectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectId);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }

        validateProjectWithCollaborators(projectEntity, projectId, username);

        return projectEntity;
    }

    // == PROTECTED METHODS ==
    protected ProjectEntity saveOrUpdateProject(ProjectEntity projectEntity, String username) {

        // IF UPDATE
        if (projectEntity.getId() != null) {
            ProjectEntity existingProjectEntity;

            // GET FROM DB
            try {
                existingProjectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier());
            } catch (DataAccessException io) {
                throw new CustomInternalServerErrorException(io);
            }

            // THEN VALIDATE
            validateProject(existingProjectEntity, projectEntity.getProjectIdentifier(), username);
        }

        // IF VALID
        try {
            // GET USER FROM DB AND CONSTRUCT THE ENTITY
            UserEntity userEntity = userRepository.findByEmail(username);
            projectEntity.setUser(userEntity);
            projectEntity.setProjectLeader(userEntity.getEmail());
            projectEntity.setProjectIdentifier(projectEntity.getProjectIdentifier().toUpperCase());

            // IF ITS A NEW PROJECT CREATE AND SET A NEW BACKLOG
            if (projectEntity.getId() == null) {
                BacklogEntity backlogEntity = new BacklogEntity();
                projectEntity.setBacklog(backlogEntity);
                backlogEntity.setProject(projectEntity);
                backlogEntity.setProjectIdentifier(projectEntity.getProjectIdentifier().toUpperCase());
            }

            // IF ITS AN UPDATE GET AND SET EXISTING BACKLOG FROM DB
            if (projectEntity.getId() != null) {
                projectEntity.setBacklog(backlogRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier().toUpperCase()));
            }

            // SAVE TO DB
            return projectRepository.save(projectEntity);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException(io);
        }
    }


    protected List<ProjectEntity> findAllProject(String username) {

        List<ProjectEntity> byLeader = projectRepository.findAllByProjectLeader(username);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByEmail(username);

        List<ProjectEntity> byCollaborator = projectRepository.findAllByCollaborators(collaboratorEntity);

        byLeader.addAll(byCollaborator);

        return byLeader;
    }

    protected DeleteDTO deleteProjectByIdentifier(String projectId, String username) {

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
}

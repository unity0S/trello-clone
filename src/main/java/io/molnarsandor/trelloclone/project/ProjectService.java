package io.molnarsandor.trelloclone.project;

import io.molnarsandor.trelloclone.collaborator.CollaboratorEntity;
import io.molnarsandor.trelloclone.collaborator.CollaboratorRepository;
import io.molnarsandor.trelloclone.exceptions.CustomInternalServerErrorException;
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

    public ProjectEntity saveOrUpdateProject(ProjectEntity projectEntity, String username) {

        if (projectEntity.getId() != null) {
            ProjectEntity existingProjectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectEntity.getProjectIdentifier());

            if (existingProjectEntity != null && !existingProjectEntity.getProjectLeader().equals(username)) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProjectEntity == null) {
                throw new ProjectNotFoundException("Project with ID: '" + projectEntity.getProjectIdentifier() + "' cannot be updated because it does not exist");
            }

        }
        try {
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

            return projectRepository.save(projectEntity);
        } catch (DataAccessException io) {
            throw new CustomInternalServerErrorException("Internal Server Error", io);
        }
    }

    public ProjectEntity findProjectByIdentifier(String projectId, String username) {

        ProjectEntity projectEntity = projectRepository.findByProjectIdentifierIgnoreCase(projectId);

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

        return projectEntity;
    }

    public List<ProjectEntity> findAllProject(String username) {

        List<ProjectEntity> byLeader = projectRepository.findAllByProjectLeader(username);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByEmail(username);

        List<ProjectEntity> byCollaborator = projectRepository.findAllByCollaborators(collaboratorEntity);

        byLeader.addAll(byCollaborator);

        return byLeader;
    }

    public DeleteDTO deleteProjectByIdentifier(String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));

        return new DeleteDTO("Project " + projectId + " deleted");
    }
}

package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedException;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.global_exceptions.CustomInternalServerErrorException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    private final ProjectService projectService;

    // == PUBLIC METHODS ==
    public CollaboratorEntity addCollaborator(String projectIdentifier, CollaboratorEntity collaboratorEntity, String username) {

            ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

            checkIsCollaboratorAssigned(projectIdentifier, collaboratorEntity, projectEntity);

            collaboratorEntity.setProject(projectEntity);
            collaboratorEntity.setProjectIdentifier(projectEntity.getProjectIdentifier());
            collaboratorEntity.setCollaboratorSequence(projectEntity.getProjectIdentifier() + "-" + collaboratorEntity.getEmail());

            CollaboratorEntity savedCollaborator;

            try {
                savedCollaborator = collaboratorRepository.save(collaboratorEntity);
            } catch (DataAccessException io) {
                throw new CustomInternalServerErrorException(io);
            }

            return savedCollaborator;
    }

    public DeleteDTO deleteCollaborator(String projectIdentifier, String collaboratorSequence, String username) {

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

        checkCollaboratorBeforeDelete(projectEntity, username, projectIdentifier, collaboratorSequence);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByCollaboratorSequence(collaboratorSequence);
        collaboratorRepository.delete(collaboratorEntity);

        return new DeleteDTO("Collaborator with id " + collaboratorSequence + " deleted from Project " + projectIdentifier);
    }


    // == PRIVATE METHODS ==
    private void checkIsCollaboratorAssigned(String projectIdentifier, CollaboratorEntity collaboratorEntity, ProjectEntity projectEntity) {

        Predicate<CollaboratorEntity> collaboratorPredicate = collaborator1 -> collaborator1.getEmail().equals(collaboratorEntity.getEmail()) &&
                collaborator1.getProjectIdentifier().equals(projectIdentifier);

        boolean collaboratorAlreadyOnProject = projectEntity
                .getCollaborators()
                .stream()
                .anyMatch(collaboratorPredicate);

        if(collaboratorAlreadyOnProject) {
            throw new CollaboratorAlreadyAssignedException("Collaborator already assigned to this project");
        }
    }

    private void checkCollaboratorBeforeDelete(ProjectEntity projectEntity, String username, String projectIdentifier, String collaboratorSequence) {
        if(!projectEntity.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("You are not the owner of this project: '" + projectIdentifier + "'");
        }

        Set<CollaboratorEntity> collaboratorEntities = projectEntity.getCollaborators();

        Predicate<CollaboratorEntity> collaboratorPredicate = collaborator ->
                collaborator.getCollaboratorSequence().equalsIgnoreCase(collaboratorSequence) &&
                        collaborator.getProjectIdentifier().equalsIgnoreCase(projectIdentifier);
        boolean collaboratorExists = false;

        if(!collaboratorEntities.isEmpty()) {
            collaboratorExists = collaboratorEntities.stream()
                    .anyMatch(collaboratorPredicate);
        }

        if(!collaboratorExists) {
            throw new CollaboratorAlreadyAssignedException("Collaborator: '" + collaboratorSequence + "' does not assigned to this project");
        }
    }
}

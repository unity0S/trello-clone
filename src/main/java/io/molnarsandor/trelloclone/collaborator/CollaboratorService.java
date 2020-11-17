package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedException;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.project.ProjectEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    private final ProjectService projectService;


    public CollaboratorEntity addCollaborator(String projectIdentifier, CollaboratorEntity collaboratorEntity, String username) {

            ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

            Predicate<CollaboratorEntity> collaboratorPredicate = collaborator1 -> collaborator1.getEmail().equals(collaboratorEntity.getEmail()) &&
                                    collaborator1.getProjectIdentifier().equals(projectIdentifier);
            boolean collaboratorAlreadyOnProject = projectEntity
                    .getCollaborators()
                    .stream()
                    .anyMatch(collaboratorPredicate);

            if(collaboratorAlreadyOnProject) {
                throw new CollaboratorAlreadyAssignedException("Collaborator already assigned to this project");
            }

            collaboratorEntity.setProject(projectEntity);
            collaboratorEntity.setProjectIdentifier(projectEntity.getProjectIdentifier());
            collaboratorEntity.setCollaboratorSequence(projectEntity.getProjectIdentifier() + "-" + collaboratorEntity.getEmail());

            return collaboratorRepository.save(collaboratorEntity);

    }

    public DeleteDTO deleteCollaborator(String projectIdentifier, String collaboratorSequence, String username) {

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

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

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findByCollaboratorSequence(collaboratorSequence);

        collaboratorRepository.delete(collaboratorEntity);

        return new DeleteDTO("Collaborator with id " + collaboratorSequence + " deleted from Project " + projectIdentifier);
    }
}

package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.domain.Collaborator;
import io.molnarsandor.pmtool.domain.Project;
import io.molnarsandor.pmtool.dto.DeleteDTO;
import io.molnarsandor.pmtool.exceptions.CollaboratorAlreadyAssignedException;
import io.molnarsandor.pmtool.exceptions.ProjectNotFoundException;
import io.molnarsandor.pmtool.repositories.CollaboratorRepository;
import io.molnarsandor.pmtool.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    private final ProjectService projectService;


    public Collaborator addCollaborator(String projectIdentifier, Collaborator collaborator, String username) {

            Project project = projectService.findProjectByIdentifier(projectIdentifier, username);

            Predicate<Collaborator> collaboratorPredicate = collaborator1 -> collaborator1.getEmail().equals(collaborator.getEmail()) &&
                                    collaborator1.getProjectIdentifier().equals(projectIdentifier);
            boolean collaboratorAlreadyOnProject = project
                    .getCollaborators()
                    .stream()
                    .anyMatch(collaboratorPredicate);

            if(collaboratorAlreadyOnProject) {
                throw new CollaboratorAlreadyAssignedException("Collaborator already assigned to this project");
            }

            collaborator.setProject(project);
            collaborator.setProjectIdentifier(project.getProjectIdentifier());
            collaborator.setCollaboratorSequence(project.getProjectIdentifier() + "-" + collaborator.getEmail());

            return collaboratorRepository.save(collaborator);

    }

    public DeleteDTO deleteCollaborator(String projectIdentifier, String collaboratorSequence, String username) {

        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);

        if(!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("You are not the owner of this project: '" + projectIdentifier + "'");
        }

        Set<Collaborator> collaborators = project.getCollaborators();

        Predicate<Collaborator> collaboratorPredicate = collaborator ->
                collaborator.getCollaboratorSequence().equalsIgnoreCase(collaboratorSequence) &&
                        collaborator.getProjectIdentifier().equalsIgnoreCase(projectIdentifier);
        boolean collaboratorExists = false;

        if(!collaborators.isEmpty()) {
            collaboratorExists = collaborators.stream()
                    .anyMatch(collaboratorPredicate);
        }

        if(!collaboratorExists) {
            throw new CollaboratorAlreadyAssignedException("Collaborator: '" + collaboratorSequence + "' does not assigned to this project");
        }

        Collaborator collaborator = collaboratorRepository.findByCollaboratorSequence(collaboratorSequence);

        collaboratorRepository.delete(collaborator);

        return new DeleteDTO("Collaborator with id " + collaboratorSequence + " deleted from Project " + projectIdentifier);
    }
}

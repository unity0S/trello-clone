package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedException;
import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorDoesntExistsException;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import io.molnarsandor.trelloclone.project.ProjectService;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.model.ProjectEntity;
import io.molnarsandor.trelloclone.util.DeleteDTO;
import io.molnarsandor.trelloclone.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    private final ProjectService projectService;

    private final ModelConverter modelConverter;

    @Override
    public CollaboratorDTO addCollaborator(final String projectIdentifier, final CollaboratorDTO collaboratorDTO, final String username) {

        CollaboratorEntity collaboratorEntity = modelConverter.collaboratorDtoToEntity(collaboratorDTO);

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

        checkIsCollaboratorAssigned(projectIdentifier, collaboratorEntity, projectEntity);

        collaboratorEntity.setProject(projectEntity);
        collaboratorEntity.setProjectIdentifier(projectEntity.getProjectIdentifier());
        collaboratorEntity.setCollaboratorSequence(projectEntity.getProjectIdentifier() + "-" + collaboratorEntity.getEmail());

        return modelConverter.collaboratorEntityToDto(
                collaboratorRepository.save(collaboratorEntity));
    }

    @Override
    public DeleteDTO deleteCollaborator(final String projectIdentifier, final Long collaboratorId, final String username) {

        ProjectEntity projectEntity = projectService.findProjectByIdentifier(projectIdentifier, username);

        CollaboratorEntity collaboratorEntity = collaboratorRepository.findById(collaboratorId).orElse(null);

        checkCollaboratorBeforeDelete(projectEntity, username, projectIdentifier, collaboratorId);

        if (collaboratorEntity == null) {
            throw new CollaboratorDoesntExistsException("Collaborator does not exists");
        }

        collaboratorRepository.delete(collaboratorEntity);

        return new DeleteDTO("Collaborator with id " + collaboratorId + " deleted from Project " + projectIdentifier);
    }

    private void checkIsCollaboratorAssigned(final String projectIdentifier, final CollaboratorEntity collaboratorEntity, final ProjectEntity projectEntity) {

        boolean collaboratorAlreadyOnProject = projectEntity
                .getCollaborators()
                .stream()
                .anyMatch(collaborator1 ->
                        collaborator1.getEmail().equals(collaboratorEntity.getEmail()) &&
                        collaborator1.getProjectIdentifier().equalsIgnoreCase(projectIdentifier));

        if(collaboratorAlreadyOnProject) {
            throw new CollaboratorAlreadyAssignedException("Collaborator already assigned to this project");
        }
    }

    private void checkCollaboratorBeforeDelete(final ProjectEntity projectEntity, String username, final String projectIdentifier, final Long collaboratorId) {

        if(!projectEntity.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("You are not the owner of this project: '" + projectIdentifier + "'");
        }

        Set<CollaboratorEntity> collaboratorEntities = projectEntity.getCollaborators();

        boolean collaboratorExists;

        if(!collaboratorEntities.isEmpty()) {
            collaboratorExists = collaboratorEntities.stream()
                    .anyMatch(collaborator ->
                              collaborator.getId().equals(collaboratorId) &&
                              collaborator.getProjectIdentifier().equalsIgnoreCase(projectIdentifier));
        } else {
            collaboratorExists = false;
        }

        if (!collaboratorExists) {
            throw new CollaboratorAlreadyAssignedException("Collaborator: '" + collaboratorId + "' does not assigned to this project");
        }
    }
}

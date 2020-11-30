package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorDTO;
import io.molnarsandor.trelloclone.util.DeleteDTO;

public interface CollaboratorService {

    CollaboratorDTO addCollaborator(final String projectIdentifier, final CollaboratorDTO collaboratorDTO, final String username);
    DeleteDTO deleteCollaborator(final String projectIdentifier, final Long collaboratorId, final String username);
}

package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends CrudRepository<CollaboratorEntity, Long> {

    CollaboratorEntity findByCollaboratorSequence(String collaboratorSequence);

    CollaboratorEntity findByEmail(String email);
}

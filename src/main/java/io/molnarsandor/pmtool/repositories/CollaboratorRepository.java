package io.molnarsandor.pmtool.repositories;

import io.molnarsandor.pmtool.domain.Collaborator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends CrudRepository<Collaborator, Long> {

    Collaborator findByCollaboratorSequence(String collaboratorSequence);

    Collaborator findByEmail(String email);
}

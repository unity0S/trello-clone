package io.molnarsandor.trelloclone.collaborator;

import io.molnarsandor.trelloclone.collaborator.model.CollaboratorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollaboratorRepository extends CrudRepository<CollaboratorEntity, Long> {

    @Override
    Optional<CollaboratorEntity> findById(@NonNull Long id);

    CollaboratorEntity findByEmail(String email);
}

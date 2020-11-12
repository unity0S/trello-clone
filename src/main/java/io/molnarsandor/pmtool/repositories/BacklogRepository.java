package io.molnarsandor.pmtool.repositories;

import io.molnarsandor.pmtool.domain.entity.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    Backlog findByProjectIdentifierIgnoreCase(String identifier);
}

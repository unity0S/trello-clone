package io.molnarsandor.pmtool.repositories;

import io.molnarsandor.pmtool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String username);
    User getById(Long id);
    User findByActivation(String key);
}

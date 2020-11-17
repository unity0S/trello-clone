package io.molnarsandor.trelloclone.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmail(String username);
    UserEntity getById(Long id);
    UserEntity findByActivation(String key);
}

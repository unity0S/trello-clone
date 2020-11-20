package io.molnarsandor.trelloclone.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails loadUserById(Long id);
}

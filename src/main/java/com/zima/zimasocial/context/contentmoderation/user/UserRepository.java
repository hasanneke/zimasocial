package com.zima.zimasocial.context.contentmoderation.user;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface UserRepository {
     Optional<User> findById(Long id);
     void save(User user) throws AccountNotFoundException;
}

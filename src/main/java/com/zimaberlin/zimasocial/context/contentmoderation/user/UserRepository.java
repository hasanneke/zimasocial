package com.zimaberlin.zimasocial.context.contentmoderation.user;

import java.util.Optional;

public interface UserRepository {
     Optional<User> findById(Long id);
     void save(User user);
}

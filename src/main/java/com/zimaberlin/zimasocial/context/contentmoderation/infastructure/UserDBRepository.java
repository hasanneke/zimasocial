package com.zimaberlin.zimasocial.context.contentmoderation.infastructure;

import com.zimaberlin.zimasocial.context.contentmoderation.user.User;
import com.zimaberlin.zimasocial.context.contentmoderation.user.UserRepository;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDBRepository implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    @Autowired
    public UserDBRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> user = userJpaRepository.findById(id);
        return user.map(e-> new User(e.getId(), e.getTrustScore(), e.getIsDisabled()));
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = userJpaRepository.findById(user.getAuthorId()).orElseThrow(UserNotFoundException::new);
        userEntity.mergeUser(user);
        userJpaRepository.save(userEntity);
    }
}

package com.zima.zimasocial.context.contentmoderation.infastructure;

import com.zima.zimasocial.context.contentmoderation.user.User;
import com.zima.zimasocial.context.contentmoderation.user.UserRepository;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.UserJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.security.auth.login.AccountNotFoundException;
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
    public void save(User user) throws AccountNotFoundException {
        UserEntity userEntity = userJpaRepository.findById(user.getAuthorId()).orElseThrow(AccountNotFoundException::new);
        userEntity.mergeUser(user);
        userJpaRepository.save(userEntity);
    }
}

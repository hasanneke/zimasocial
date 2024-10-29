package com.zimaberlin.zimasocial.config;

import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity profile = userRepository.findById(Long.parseLong(username)).orElse(null);
       return CustomUserDetails.builder().profile(profile).build();
    }
}

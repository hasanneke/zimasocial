package com.zimaberlin.zimasocial.service;

import com.zimaberlin.zimasocial.dto.CustomUserDetails;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Profile profile = profileRepository.findById(Long.parseLong(username)).orElse(null);
       return CustomUserDetails.builder().profile(profile).build();
    }
}

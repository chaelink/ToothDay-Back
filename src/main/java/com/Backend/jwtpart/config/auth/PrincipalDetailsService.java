package com.Backend.jwtpart.config.auth;

import com.Backend.jwtpart.repository.UserRepository;
import com.Backend.jwtpart.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        User user = userRepository.findByUsername(username);

        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}
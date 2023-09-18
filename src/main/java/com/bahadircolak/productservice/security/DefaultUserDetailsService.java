package com.bahadircolak.productservice.security;

import com.bahadircolak.productservice.model.User;
import com.bahadircolak.productservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User maybeUser = userRepository.findByUsername(userName);
        if (maybeUser == null) {
            throw new UsernameNotFoundException(userName);
        }
        return new org.springframework.security.core.userdetails.User(maybeUser.getUsername(), maybeUser.getPassword(), emptyList());
    }
}
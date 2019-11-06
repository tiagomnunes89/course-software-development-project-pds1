package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CredentialsDTO;
import com.tiagomnunes.aulapds1.dto.TokenDTO;
import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.repositories.UserRepository;
import com.tiagomnunes.aulapds1.security.JWTUtil;
import com.tiagomnunes.aulapds1.services.exceptions.JWTAuthenticationException;
import com.tiagomnunes.aulapds1.services.exceptions.JWTAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public TokenDTO authenticate(CredentialsDTO credentialsDTO) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail(), credentialsDTO.getPassword());
            authenticationManager.authenticate(authToken);
            String token = jwtUtil.generateToken(credentialsDTO.getEmail());
            return new TokenDTO(credentialsDTO.getEmail(), token);
        } catch (AuthenticationException e) {
            throw new JWTAuthenticationException("Bad credentials");
        }
    }

    public User authenticated() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername());
        } catch (Exception e) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public void validateSelfOrAdmin(Long userId) {
        User user = authenticated();
        if(user == null || (!user.getId().equals(userId)) && !user.hasRole("ROLE_ADMIN")){
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public void validateOwnOrderOrAdmin(Order order) {
        User user = authenticated();
        if(user == null || (!user.getId().equals(order.getClient().getId())) && !user.hasRole("ROLE_ADMIN")){
            throw new JWTAuthorizationException("Access denied");
        }
    }
}

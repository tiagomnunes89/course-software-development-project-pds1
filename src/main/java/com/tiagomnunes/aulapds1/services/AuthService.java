package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CredentialsDTO;
import com.tiagomnunes.aulapds1.dto.TokenDTO;
import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.repositories.UserRepository;
import com.tiagomnunes.aulapds1.security.JWTUtil;
import com.tiagomnunes.aulapds1.services.exceptions.JWTAuthenticationException;
import com.tiagomnunes.aulapds1.services.exceptions.JWTAuthorizationException;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class AuthService {

    public static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        if (user == null || (!user.getId().equals(userId)) && !user.hasRole("ROLE_ADMIN")) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public void validateOwnOrderOrAdmin(Order order) {
        User user = authenticated();
        if (user == null || (!user.getId().equals(order.getClient().getId())) && !user.hasRole("ROLE_ADMIN")) {
            throw new JWTAuthorizationException("Access denied");
        }
    }

    public TokenDTO refreshToken() {
        User user = authenticated();
        return new TokenDTO(user.getEmail(), jwtUtil.generateToken(user.getEmail()));
    }

    @Transactional
    public void sendNewPassword (String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new ResourceNotFoundException("Email not found");
        }

        String newPassword = newPassword();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        LOG.info("New password: " + newPassword);
    }

    private String newPassword() {
        char[] vect = new char[10];
        for (int i = 0; i < 10; i++) {
            vect[i] = randomChar();
        }
        return new String(vect);
    }

    private char randomChar() {
        Random random = new Random();
        int opt = random.nextInt(3);
        if(opt == 0){ //generate digit
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) { //generate uppercase letter
            return (char) (random.nextInt(26) + 65);
        } else { //generate lowercase letter
            return (char) (random.nextInt(26) + 97);
        }
    }
}

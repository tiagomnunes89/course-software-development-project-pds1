package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.UserDTO;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.repositories.UserRepository;
import com.tiagomnunes.aulapds1.services.exceptions.DatabaseException;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        Optional<User> user = repository.findById(id);
        User entity = user.orElseThrow(() -> new ResourceNotFoundException(id));
        return new UserDTO(entity);
    }

    public User insert(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update(Long id, User user) {
        try {
            User entity = repository.getOne(id);
            updateData(entity, user);
            return repository.save(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(User entity, User user) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
    }
}
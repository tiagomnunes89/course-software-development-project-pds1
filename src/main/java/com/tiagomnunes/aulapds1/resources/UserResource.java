package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value="/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> userList = service.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<User> findById(@PathVariable  Long id){
        User user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }


    @PostMapping
    public ResponseEntity<User> insert (@RequestBody User user){
        user = service.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }
}

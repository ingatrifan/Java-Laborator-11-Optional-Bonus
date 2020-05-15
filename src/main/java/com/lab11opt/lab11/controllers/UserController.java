package com.lab11opt.lab11.controllers;

import com.lab11opt.lab11.models.User;
import com.lab11opt.lab11.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAll(){
        return userRepository.findAll();
    }
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping(path = "/delete")
    public @ResponseBody String delete(){
        String username =  getAuthenticatedUsername();
        Optional<User> user = userRepository.findByUsername(username);
        User editUser = user.map(User::new).get();
        userRepository.delete(editUser);
        return "Deleted";
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping(path = "/edit")
    public @ResponseBody String edit(@RequestBody String password){
        String username =  getAuthenticatedUsername();
        Optional<User> user = userRepository.findByUsername(username);
        User editUser = user.map(User::new).get();
        editUser.setPassword(password);
        userRepository.save(editUser);
        return "Edited";
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

}

package com.lab11opt.lab11.controllers;

import com.lab11opt.lab11.models.User;
import com.lab11opt.lab11.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/register")
    public @ResponseBody
    String register(@RequestBody User user){
        System.out.println(user);
        userRepository.save(user);
        return "Successfully registered";
    }
}

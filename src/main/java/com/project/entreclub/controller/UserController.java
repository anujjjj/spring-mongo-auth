package com.project.entreclub.controller;

import com.project.entreclub.model.User;
import com.project.entreclub.service.TokenService;
import com.project.entreclub.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private TokenService tokenService;

    @Autowired
    UserController(UserService userService,TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public String regiterUser(@RequestBody User user) {
        String token="";
        User user1 =  userService.saveUser(user);

        if(user1!=null){
           token =  tokenService.createToken(user1.getId());
            user1.setToken(token);
            userService.saveUser(user1);
        }

        return token;
    }

//    @PostMapping("/signin")
//    public String signin(@RequestBody User user) {
//        // 1. Fetch User
//        2. Verify Password
//        create token
//        save token to database
//        return json
//        user
//        String token =  tokenService.createToken(user1.getId());
//        user1.setToken(token);
//        userService.saveUser(user1);
//        return token;
//    }

    @GetMapping("/get")
    public User getUser(HttpServletRequest request) {
        ObjectId userId = (ObjectId) request.getAttribute("userId");
        return userService.getUser(userId);
    }



}

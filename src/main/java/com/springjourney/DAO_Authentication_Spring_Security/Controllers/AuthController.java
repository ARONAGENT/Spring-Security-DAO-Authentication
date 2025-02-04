package com.springjourney.DAO_Authentication_Spring_Security.Controllers;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import com.springjourney.DAO_Authentication_Spring_Security.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserServices services;

    @GetMapping("/welcome")
    public String welcomePage(){
        return "Welcome to DAO Authentication project";
    }

    @PostMapping("/register")
    public SpringUser addUser(@RequestBody SpringUser user){
        return services.addUser(user);
    }
}

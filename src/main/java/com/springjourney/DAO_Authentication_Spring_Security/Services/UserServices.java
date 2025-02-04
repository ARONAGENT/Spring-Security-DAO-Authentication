package com.springjourney.DAO_Authentication_Spring_Security.Services;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import com.springjourney.DAO_Authentication_Spring_Security.Repositories.SpringUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final SpringUserRepository springUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServices(SpringUserRepository springUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.springUserRepository = springUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public SpringUser addUser(SpringUser user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return springUserRepository.save(user);
    }

}

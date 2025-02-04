package com.springjourney.DAO_Authentication_Spring_Security.Services;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.CustomerUserDetails;
import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import com.springjourney.DAO_Authentication_Spring_Security.Repositories.SpringUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomerUserDetailService implements UserDetailsService {

    private final SpringUserRepository userRepository;

    public CustomerUserDetailService(SpringUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SpringUser user=userRepository.findByUsername(username);
        if(user==null)
            throw  new UsernameNotFoundException("User Not Found with username"+username);
        return new CustomerUserDetails(user);
    }
}

# DAO Authentication in Spring Security

## Step 1: Create a Spring Boot Project

1. Go to [start.spring.io](https://start.spring.io/)
2. Configure the project:
   - **Project:** Maven
   - **Language:** Java
   - **Spring Boot Version:** Latest stable version
   - **Dependencies:**
     - Spring Web
     - Spring Security
     - Spring Data JPA
     - MySQL Driver
     - Spring Boot DevTools
3. Click **Generate** and **Extract** the ZIP file into your workspace.
4. Open the project in your IDE (IntelliJ IDEA, Eclipse, or VS Code).

## Step 2: Configure Database in `application.properties`

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

## Step 3: Create Project Structure (Packages)

Inside `src/main/java/com/example/demo`, create the following packages:

- `controllers`
- `services`
- `repositories`
- `configurations`
- `entities`

## Step 4: Create the `User` Entity

Inside `entities` package, create a class **`SpringUser`**:

```java
package com.springjourney.DAO_Authentication_Spring_Security.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="springusers")
public class SpringUser {

    @Id
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

## Step 5: Create `UserRepository`

Inside `repositories` package, create an interface `SpringUserRepository`:

```java
package com.springjourney.DAO_Authentication_Spring_Security.Repositories;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringUserRepository extends JpaRepository<SpringUser,String> {
    SpringUser findByUsername(String username);
}
```

## Step 6: Create `UserService`

Inside `services` package:

```java
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
```

## Step 7: Create `WebSecurityConfig` Class

Inside `configurations` package:

```java
package com.example.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("register").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }
}
```

## Step 8: Create `AuthController`

Inside `controllers` package:

```java
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
```

## Step 9: Create `CustomerUserDetails`

Inside `entities` package:

```java
package com.springjourney.DAO_Authentication_Spring_Security.Entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomerUserDetails implements UserDetails {

    private final SpringUser springUser;

    public CustomerUserDetails(SpringUser springUser) {
        this.springUser = springUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return springUser.getPassword();
    }

    @Override
    public String getUsername() {
        return springUser.getUsername();
    }
}
```

## Step 10: Create `CustomerUserDetailService`

Inside `services` package:

```java
package com.springjourney.DAO_Authentication_Spring_Security.Services;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import com.springjourney.DAO_Authentication_Spring_Security.Repositories.SpringUserRepository;
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
        SpringUser user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User Not Found with username " + username);
        return new CustomerUserDetails(user);
    }
}
```

### Ouputs -> 

**Registration Image**

![00DAO add user ](https://github.com/user-attachments/assets/db77aacb-bca1-4164-a80d-1b9dac3478f9)

**Authenticate with valid Credentials**

![DAO01](https://github.com/user-attachments/assets/4513774f-8afb-4b49-bbec-ad8127b2a650)

**If Credentials  is Invalid**

![DAO2 invalid Credentials ](https://github.com/user-attachments/assets/0267c3d2-f010-4a77-8719-b05fdc6ab50f)



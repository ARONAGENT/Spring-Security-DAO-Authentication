package com.springjourney.DAO_Authentication_Spring_Security.Repositories;

import com.springjourney.DAO_Authentication_Spring_Security.Entities.SpringUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringUserRepository extends JpaRepository<SpringUser,String> {

    SpringUser findByUsername(String username);
}

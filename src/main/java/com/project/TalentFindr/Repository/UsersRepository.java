package com.project.TalentFindr.Repository;

import java.util.Optional;

import com.project.TalentFindr.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRepository extends JpaRepository<Users, Integer> {


     Optional<Users> findByEmail(String email);


}
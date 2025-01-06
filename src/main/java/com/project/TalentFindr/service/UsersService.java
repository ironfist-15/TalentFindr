package com.project.TalentFindr.service;

//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    public UsersRepository usersRepository;


    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void addUser(Users user){
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        usersRepository.save(user);

    }

    public Optional<Users> getuserbyEmailid(String email){
        return usersRepository.findbyEmail(email);
    }

}

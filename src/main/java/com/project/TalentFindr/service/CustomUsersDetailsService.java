package com.project.TalentFindr.service;


import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUsersDetailsService implements UserDetailsService {

    @Autowired
    public UsersRepository usersRepository;


    public CustomUsersDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("oops! couldn't find you"));
        return new CustomUserDetails(users);
    }
}

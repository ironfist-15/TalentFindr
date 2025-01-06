package com.project.TalentFindr.service;

import java.util.List;

import com.project.TalentFindr.Repository.UsersTypeRepository;

import com.project.TalentFindr.entity.UsersType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersTypeService {

    @Autowired
    private UsersTypeRepository usersTypeRepository;

    public List<UsersType> getallusersType(){
        return usersTypeRepository.findAll();
    }



}

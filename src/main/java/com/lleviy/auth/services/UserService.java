package com.lleviy.auth.services;


import com.lleviy.auth.dto.UserDTO;
import com.lleviy.auth.models.Role;
import com.lleviy.auth.models.User;
import exceptions.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lleviy.auth.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User registerNewUserAccount(UserDTO userDto){

        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: "
                            +  userDto.getEmail());
        }
        final User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(new HashSet<>(Arrays.asList(Role.USER)));
        repository.save(user);
        return user;

        // the rest of the registration operation
    }

    private boolean emailExist(String email) {
        return repository.findByEmail(email) != null;
    }
}

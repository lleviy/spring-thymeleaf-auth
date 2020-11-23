package com.lleviy.auth.services;

import com.lleviy.auth.dto.EditUserDto;
import com.lleviy.auth.dto.UserDto;
import com.lleviy.auth.models.User;
import com.lleviy.auth.models.VerificationToken;

import java.io.IOException;

public interface IUserService {
    User registerNewUserAccount(UserDto accountDto);

    User getUser(String token);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);

    EditUserDto getCurrentEditUserDto();

    User editUser(EditUserDto editUserDto);
}

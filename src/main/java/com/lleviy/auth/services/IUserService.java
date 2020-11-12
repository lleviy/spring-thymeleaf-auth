package com.lleviy.auth.services;

import com.lleviy.auth.dto.UserDTO;
import com.lleviy.auth.models.User;

public interface IUserService {
    User registerNewUserAccount(UserDTO accountDto);
}

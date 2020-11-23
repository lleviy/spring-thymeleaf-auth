package com.lleviy.auth.services;


import com.lleviy.auth.dto.EditUserDto;
import com.lleviy.auth.dto.UserDto;
import com.lleviy.auth.models.Role;
import com.lleviy.auth.models.User;
import com.lleviy.auth.models.VerificationToken;
import com.lleviy.auth.repositories.VerificationTokenRepository;
import exceptions.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lleviy.auth.repositories.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;
    private IPictureProfileService pictureProfileService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       VerificationTokenRepository tokenRepository, IPictureProfileService pictureProfileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.pictureProfileService = pictureProfileService;

        // Инициализируем юзера на время разработки
        User user = new User();
        user.setFirstName("A");
        user.setLastName("H");
        user.setEmail("admin@admin.com");
        user.setPassword(passwordEncoder.encode("aaaaaaaa"));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public User registerNewUserAccount(UserDto userDto){
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
        userRepository.save(user);
        return user;
    }

    public User findById(long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return user;
    }

    @Override
    public User editUser(EditUserDto editUserDto) {
        Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) userObject;
        User user = findById(userDetails.getId());
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        if (!editUserDto.getPhoto().isEmpty()){
            try {
                pictureProfileService.store(user, editUserDto.getPhoto());
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        userRepository.save(user);
        return user;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public EditUserDto getCurrentEditUserDto() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) user;
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setFirstName(userDetails.getFirstName());
        editUserDto.setLastName(userDetails.getLastName());
        return editUserDto;
    }
}

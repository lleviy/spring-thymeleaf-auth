package com.lleviy.auth.services;

import com.lleviy.auth.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPictureProfileService {
    void store(User user, MultipartFile photo) throws IOException;
    void deletePhoto(User user) throws IOException;
}

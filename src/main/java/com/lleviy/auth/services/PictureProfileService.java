package com.lleviy.auth.services;

import com.lleviy.auth.models.User;
import com.lleviy.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PictureProfileService implements IPictureProfileService{

    private UserRepository userRepository;

    @Autowired
    public PictureProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void store(User user, MultipartFile multipartFile) throws IOException {
        String photo = user.getPhotoPath();
        if (photo != null){
            try{
                deletePhoto(user);
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhoto(fileName);
        User savedUser = userRepository.save(user);
        String uploadDir = "user-photos/" + savedUser.getId();

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    @Override
    public void deletePhoto(User user) throws IOException {
        String photo = user.getPhotoPath();
        Path uploadPath = Paths.get(photo);
        if (Files.exists(uploadPath)) {
            Files.delete(uploadPath);
        }
        user.setPhoto(null);
        userRepository.save(user);
    }
}

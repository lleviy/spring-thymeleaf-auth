package com.lleviy.auth.dto;

import com.sun.istack.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EditUserDto {
    @NotNull
    @NotBlank
    @Size(max=20)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max=20)
    private String lastName;

    private MultipartFile photo;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}

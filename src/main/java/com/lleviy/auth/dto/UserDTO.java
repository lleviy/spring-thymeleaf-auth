package com.lleviy.auth.dto;

import com.lleviy.auth.validation.PasswordMatches;
import com.lleviy.auth.validation.ValidEmail;
import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {
    @NotNull
    @NotBlank
    @Size(max=20)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max=20)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min=8, max=20)
    private String password;
    private String matchingPassword;

    @NotNull
    @NotBlank
    @ValidEmail
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
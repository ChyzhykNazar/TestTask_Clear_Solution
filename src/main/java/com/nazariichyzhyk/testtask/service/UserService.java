package com.nazariichyzhyk.testtask.service;

import com.nazariichyzhyk.testtask.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

//    void updateUserLastName(User user);
//
//    void updateUserFirstName(User user);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    User updateUserFields(Long id, Map<String, Object> objectMap);

    List<User> searchUserByDateBirth(LocalDate from, LocalDate to);
}

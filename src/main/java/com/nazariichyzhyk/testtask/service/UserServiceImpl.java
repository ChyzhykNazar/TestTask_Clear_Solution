package com.nazariichyzhyk.testtask.service;

import com.nazariichyzhyk.testtask.UserNotFoundException;
import com.nazariichyzhyk.testtask.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Value("${user.age.min}")
    private int minAge;

    private List<User> users = new ArrayList<>();
    @Override
    public User createUser(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = user.getBirthDate();
        long age = ChronoUnit.YEARS.between(birthDate, currentDate);
        if (age < minAge) {
            throw new IllegalArgumentException("User must be at least " + minAge + " years old.");
        }
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        for(User user : users){
            if(user.getId() == id){
                user.setEmail(updatedUser.getEmail());
                user.setFirstName(updatedUser.getFirstName());
                user.setLastName(updatedUser.getLastName());
                user.setBirthDate(updatedUser.getBirthDate());
                user.setPhoneNumber(updatedUser.getPhoneNumber());
                user.setAddress(updatedUser.getAddress());
                return user;
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found");
    }

    @Override
    public void deleteUser(Long id) {
        for(User user : users){
            if(user.getId() == id){
                users.remove(user);
                return;
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found");
    }

    @Override
    public User updateUserFields(Long id, Map<String, Object> updatedField) {
        for(User user : users){
            if(user.getId() == id){
                if(updatedField.containsKey("email")){
                    user.setEmail((String) updatedField.get("email"));
                }
                if(updatedField.containsKey("firstName")){
                    user.setFirstName((String) updatedField.get("firstName"));
                }
                if(updatedField.containsKey("lastName")){
                    user.setLastName((String) updatedField.get("lastName"));
                }
                if (updatedField.containsKey("birthDate")) {
                    String birthDateStr = (String) updatedField.get("birthDate");
                    LocalDate birthDate = LocalDate.parse(birthDateStr);
                    user.setBirthDate(birthDate);
                }
                if(updatedField.containsKey("address")){
                    user.setAddress((String) updatedField.get("address"));
                }
                if(updatedField.containsKey("phoneNumber")){
                    user.setPhoneNumber((String) updatedField.get("phoneNumber"));
                }
                return user;
            }
        }
        throw new UserNotFoundException("User with ID " + id + " not found");
    }

    @Override
    public List<User> searchUserByDateBirth(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isBefore(toDate)) {
            List<User> result = new ArrayList<>();
            for (User user : users) {
                LocalDate userBirthDate = user.getBirthDate();
                if (userBirthDate != null && !userBirthDate.isBefore(fromDate) && !userBirthDate.isAfter(toDate)) {
                    result.add(user);
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid date range: 'From' must be less than 'To'");
        }
    }
}

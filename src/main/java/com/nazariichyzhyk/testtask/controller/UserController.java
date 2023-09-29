package com.nazariichyzhyk.testtask.controller;

import com.nazariichyzhyk.testtask.UserNotFoundException;
import com.nazariichyzhyk.testtask.entity.User;
import com.nazariichyzhyk.testtask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testTask")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody @Valid User user) {
        try {
            User user1 = userService.createUser(user);
            return new ResponseEntity<User>(user1, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ResponseEntity.ok(users);
        return users;
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody @Valid User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PatchMapping("updateUserFields/{id}")
    public ResponseEntity<? extends Object> updateUserFields(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updatedFields) {
        try {
            User user = userService.updateUserFields(id, updatedFields);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/searchUserByBirthDateRange")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            List<User> userList = userService.searchUserByDateBirth(from, to);
            if (userList.isEmpty()) {
                // Handle the case where no users were found for the given date range
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                // Return the list of users with a 200 OK status
                return ResponseEntity.ok(userList);
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid date range or other input validation errors
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Handle unexpected server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





}

package com.nazariichyzhyk.testtask;

import com.nazariichyzhyk.testtask.controller.UserController;
import com.nazariichyzhyk.testtask.entity.User;
import com.nazariichyzhyk.testtask.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        // Mock user data
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));

        // Mock UserService behavior
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/testTask/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\", " +
                                "\"birthDate\": \"1990-01-01\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json("{\"email\":\"test@example.com\"," +
                        "\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\"}"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Mock user data
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1)));
        users.add(new User(1L, "test2@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15)));

        // Mock UserService behavior
        when(userService.getAllUsers()).thenReturn(users);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/testTask/getAllUsers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"email\":\"test1@example.com\"," +
                        "\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\"}," +
                        "{\"email\":\"test2@example.com\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"birthDate\":\"1985-05-15\"}]"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Mock UserService behavior
        doNothing().when(userService).deleteUser(1L); // Assuming you are using Mockito's doNothing() for void methods

        // Perform DELETE request to delete the user with ID 1
        mockMvc.perform(delete("/testTask/deleteUser/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserFields() throws Exception {
        // Mock user data
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));

        // Mock UserService behavior
        when(userService.updateUserFields(1L, getUpdatedFields())).thenReturn(user);

        // Perform PATCH request
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/testTask/updateUserFields/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\",\"firstName\":\"Updated\",\"lastName\":\"User\",\"birthDate\":\"1995-02-02\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    private Map<String, Object> getUpdatedFields() {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("email", "updated@example.com");
        updatedFields.put("firstName", "Updated");
        updatedFields.put("lastName", "User");
        updatedFields.put("birthDate", "1995-02-02");
        return updatedFields;
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        // Mock user data
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1)));
        users.add(new User(2L, "test2@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15)));

        // Mock UserService behavior
        LocalDate fromDate = LocalDate.of(1980, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 1, 1);
        when(userService.searchUserByDateBirth(fromDate, toDate)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/testTask/searchUserByBirthDateRange")
                        .param("from", "1980-01-01")
                        .param("to", "1995-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"email\":\"test1@example.com\"," +
                        "\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\"}," +
                        "{\"email\":\"test2@example.com\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"birthDate\":\"1985-05-15\"}]"));

    }
}
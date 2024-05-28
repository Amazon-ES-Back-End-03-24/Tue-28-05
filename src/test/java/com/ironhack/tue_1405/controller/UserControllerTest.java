package com.ironhack.tue_1405.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.tue_1405.dtos.UserEmailRequest;
import com.ironhack.tue_1405.dtos.UserRequest;
import com.ironhack.tue_1405.dtos.UserUpdateRequest;
import com.ironhack.tue_1405.model.User;
import com.ironhack.tue_1405.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    // an instance of our application
    @Autowired
    private WebApplicationContext webApplicationContext;

    // just necessary to create users in db for tests
    @Autowired
    private UserRepository userRepository;

    // provides a way to call HTTP requests against our controllers
    private MockMvc mockMvc;

    // allows to convert Java objects to JSON and vice versa
    private final ObjectMapper objectMapper = new ObjectMapper();


    private User user;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("Luisa", 12, "email@email.com"));
        user1 = userRepository.save(new User("Luisito", 57, "luis@ito.com"));
        user2 = userRepository.save(new User("Pepito", 33, "pepito@example.com"));
        //    List<User> savedUsers = userRepository.saveAll(List.of(user, user1, user2));
        //    user = savedUsers.get(0);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        // as it's a testing db I can delete
        userRepository.deleteAll();
    }

    @Test
    void getUserById_id_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users/id/{id}", user2.getId()))
                .andExpect(status().isOk())
                //.andExpect(result -> result.getResponse().getContentAsString().contains("Pepito"))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pepito"));
    }


    @Test
    void getUserById_invalidId_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users/id/{id}", 0))
                .andExpect(status().isNotFound())
                //.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
        //assertInstanceOf(ResponseStatusException.class, mvcResult.getResolvedException());
    }

    @Test
    void getUsersByNameAndAge_age_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users?age=57"))
                .andExpect(status().isOk())
                .andReturn();
        //.andDo(print());

        assertFalse(mvcResult.getResponse().getContentAsString().contains("email@email.com"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("luis@ito.com"));
    }

    @Test
    void getUsersByNameAndAge_name_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users?name=Lui"))
                .andExpect(status().isOk())
                .andReturn();
        //.andDo(print());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("email@email.com"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("luis@ito.com"));
    }

    @Test
    void getUsersByNameAndAge_nameAndAge_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users?name=Luisa&age=12"))
                .andExpect(status().isOk())
                .andReturn();
        //.andDo(print());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("email@email.com"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("luis@ito.com"));
    }

    @Test
    void getUsersByNameAndAge_noParams_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();
        //.andDo(print());


        System.out.println("--------");
        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("email@email.com"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("luis@ito.com"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pepito"));
    }

    @Test
    void createUser_validUser_createdUser() throws Exception {
        UserRequest newUser = new UserRequest("Gabriela", 29, "gaby@email.es");
        String userJson = objectMapper.writeValueAsString(newUser);

        System.out.println("-----");
        System.out.println(userJson);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Gabriela"));
    }

    @Test
    void createUser_invalidUser_badRequest() throws Exception {
        UserRequest newUser = new UserRequest(null, -1, "gaby");
        String userJson = objectMapper.writeValueAsString(newUser);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateUser_validUser_updatedUser() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Gabriela", 29, "gaby@email.es");
        String userJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        assertEquals("Gabriela", userRepository.findById(user.getId()).get().getName());
    }

    @Test
    void updateUser_validUserEmail_updatedUserEmail() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setEmail("gaby@email.es");
        String userJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        assertEquals("gaby@email.es", userRepository.findById(user.getId()).get().getEmail());
    }

    @Test
    void updateUser_invalidUserId_notFound() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        String userJson = objectMapper.writeValueAsString(updateRequest);

        System.out.println("USER JSON: " + userJson);

        mockMvc.perform(put("/users/{id}", 0)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateUser_invalidUserAge_notFound() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Gabriela", -111, "gaby@email.es");
        String userJson = objectMapper.writeValueAsString(updateRequest);

        System.out.println("USER JSON: " + userJson);

        mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void updateUserEmail_validEmail_updatedUserEmail() throws Exception {
        UserEmailRequest updateEmailRequest = new UserEmailRequest("gaby@email.es");
        String emailJson = objectMapper.writeValueAsString(updateEmailRequest);

        System.out.println(emailJson);

        mockMvc.perform(patch("/users/{id}/email", user1.getId())
                        .contentType("application/json")
                        .content(emailJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        assertEquals("gaby@email.es", userRepository.findById(user1.getId()).get().getEmail());
    }

    @Test
    void updateUserEmail_invalidUserId_notFound() throws Exception {
        UserEmailRequest updateEmailRequest = new UserEmailRequest();
        String emailJson = objectMapper.writeValueAsString(updateEmailRequest);

        mockMvc.perform(patch("/users/{id}/email", 0)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateUserEmail_invalidUserEmail_badRequest() throws Exception {
        UserEmailRequest updateEmailRequest = new UserEmailRequest("badEmail");
        String emailJson = objectMapper.writeValueAsString(updateEmailRequest);

        mockMvc.perform(patch("/users/{id}/email", user2.getId())
                        .contentType("application/json")
                        .content(emailJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void deleteUserById_validUserId_deletesUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/users/{id}/delete", user2.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("pepito@example.com"));

        // this should be tested in the service:
        assertFalse(userRepository.existsById(user2.getId()));
    }

    @Test
    void deleteUserById_invalidUserId_NotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/users/{id}/delete", 0))
                .andExpect(status().isNotFound())
                //.andExpect(result ->  assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }
}
package com.mindhub.ToDoList.controllers.admin;


import com.mindhub.ToDoList.config.JwtUtils;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.UserNotFoundException;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.controllers.admin.AdminController;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.repositories.UserRepository;
import com.mindhub.ToDoList.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;

import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//@WebMvcTest(AdminController.class)

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "usuarioprueba",authorities="ADMIN")
public class AdminControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;


    private final String uri = "/api/admin/users";
    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private EntityUser user1;
    private EntityUser user2;
    private List<UserDTO> users;

    @BeforeEach
    public void setUp(){
        user1 = new EntityUser(); //creo un usuario
        user1.setId(1L);
        user1.setUsername("usuario1"); //setteo valores
        user1.setPassword("usuariopassword1");
        user1.setEmail("usuario1@gmail.com");
        user1.setRoleType(RoleType.USER);
        userRepository.save(user1);

        user2 = new EntityUser(); //creo un usuario
        user2.setId(2L);
        user2.setUsername("usuario2"); //setteo valores
        user2.setPassword("usuariopassword2");
        user2.setEmail("usuario2@gmail.com");
        user2.setRoleType(RoleType.USER);
        userRepository.save(user2);
        userDTO1 = new UserDTO(user1);
        userDTO2 = new UserDTO(user2);

        users = new ArrayList<>();
        users.add(userDTO1);
        users.add(userDTO2);
    }

    @Test
    public void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get(uri+"/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("usuario1"))
                .andExpect(jsonPath("$[1].username").value("usuario2"));
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO1);

        mockMvc.perform(get(uri+"/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$.email").value("usuario1@gmail.com"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(9L)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get(uri+"/{id}", 9L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete(uri + "/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).deleteUser(9L);

        mockMvc.perform(delete(uri + "/{id}", 9L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }



}



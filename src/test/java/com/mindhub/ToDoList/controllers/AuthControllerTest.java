package com.mindhub.ToDoList.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.ToDoList.config.JwtUtils;
import com.mindhub.ToDoList.dtos.authDTOs.LoginUserDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTORequest;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtils jwtUtil;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginUserDTO loginRequest;
    private final String username = "usuarioprueba";
    private final RoleType roleType = RoleType.USER;
    private String uri = "/api/auth";


    @BeforeEach
    public void setUp() {
        loginRequest = new LoginUserDTO(username,roleType.toString());
        loginRequest.setUsername("usuarioprueba");
        loginRequest.setPassword("password");
    }


    @Test
    public void testAuthenticateUser() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("usuarioprueba");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(anyString(), any(RoleType.class))).thenReturn("token-jwt-ejemplo");


        LoginUserDTO loginRequest = new LoginUserDTO("usuarioprueba","password");

        mockMvc.perform(post(uri+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("token-jwt-ejemplo"));
    }


    @Test
    public void testAuthenticateUser_Unauthorized() throws Exception {
        LoginUserDTO loginRequest = new LoginUserDTO("usuarioprueba", "incorrectPassword");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        mockMvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}

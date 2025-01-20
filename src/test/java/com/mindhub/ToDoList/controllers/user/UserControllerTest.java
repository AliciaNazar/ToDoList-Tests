package com.mindhub.ToDoList.controllers.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.ToDoList.config.CustomUserDetailsService;
import com.mindhub.ToDoList.config.SecurityConfig;
import com.mindhub.ToDoList.config.JwtUtils;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.controllers.user.UserController;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTORequest;
import com.mindhub.ToDoList.dtos.authDTOs.LoginUserDTO;
import com.mindhub.ToDoList.exceptions.UserNotFoundException;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.repositories.UserRepository;
import com.mindhub.ToDoList.services.TaskService;
import com.mindhub.ToDoList.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;


@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "usuarioprueba",authorities="USER")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private SecurityConfig securityConfig;


    private final String username = "usuarioprueba";
    private EntityUser user;
    private String uri = "/api/user/users";
    private UserDTO userDTO = new UserDTO();
    private UserDTORequest userDTORequest = new UserDTORequest();
    private UserDTO updatedUserDTO = new UserDTO();


    @BeforeEach
    public void setUp() {
        user = new EntityUser(); //creo un usuario
        user.setId(1L);
        user.setUsername(username); //setteo valores
        user.setPassword("usuariopassword");
        user.setEmail("user@gmail.com");
        user.setRoleType(RoleType.USER);
        userRepository.save(user); //guardo el usuario

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoleType(user.getRoleType());


//        UserDTORequest userDTORequest = new UserDTORequest();
        userDTORequest.setId(user.getId());
        userDTORequest.setUsername(user.getUsername());
        userDTORequest.setEmail("algo@");
        userDTORequest.setPassword("asd");
        userDTORequest.setRoleType(user.getRoleType());


        updatedUserDTO.setId(user.getId());
        updatedUserDTO.setUsername(user.getUsername());
        updatedUserDTO.setEmail(userDTORequest.getEmail());
        updatedUserDTO.setRoleType(user.getRoleType());


    }


    @Test
    public void testGetUser() throws Exception {
        when(userService.getUserByUsername(user.getUsername())).thenReturn(userDTO); //simulo que el servicio de usuarios devuelve un UserDTO cuando busco por nombre de usuario
        when(customUserDetailsService.loadUserByUsername(user.getUsername())) //simulo que el servicio de detalles del usuario carga correctamente un usuario con sus credenciales y roles
                .thenReturn(org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoleType().toString())
                        .build());

        mockMvc.perform(get(uri) //Realiza una solicitud HTTP GET al endpoint /api/user/users con el encabezado Accept: application/json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Valida que el código de estado sea 200 (OK)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //que el contenido de la respuesta sea JSON
                .andExpect(jsonPath("$.username").value(user.getUsername())) //que el JSON de la respuesta contenga el username, email y roleType esperados
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.roleType").value(user.getRoleType().toString()));
    }


    @Test
    public void testUpdateUser() throws Exception {
        when(userService.getUserByUsername(user.getUsername())).thenReturn(userDTO);
//        when(userService.updateUser(user.getId(), userDTORequest)).thenReturn(updatedUserDTO); //si hago esto dependo del servicio y yo me tengo que abstraer de eso ahora
        when(userService.updateUser(anyLong(), any(UserDTORequest.class)))
                .thenReturn(updatedUserDTO); //pruebo el comportamiento del controlador sin depender de la implementación real del servicio

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTORequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(userDTORequest.getEmail()))
                .andExpect(jsonPath("$.roleType").value(user.getRoleType().toString()));
    }


    @Test
    public void testDeleteUser() throws Exception {
        when(userService.getUserByUsername(user.getUsername())).thenReturn(userDTO);

        mockMvc.perform(delete(uri))
                .andExpect(status().isNoContent());
    }




}
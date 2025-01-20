package com.mindhub.ToDoList.services.impl;


import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTORequest;
import com.mindhub.ToDoList.exceptions.ConflictException;
import com.mindhub.ToDoList.exceptions.UserNotFoundException;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private EntityUser user;
    private UserDTORequest userDTORequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new EntityUser();
        user.setUsername("usuario");
        user.setEmail("usuario@gmail.com");
        user.setPassword("usuario1234");

        userDTORequest = new UserDTORequest();
        userDTORequest.setUsername("usuario");
        userDTORequest.setEmail("usuario@gmail.com");
        userDTORequest.setPassword("usuario1234");
        userDTORequest.setRoleType(RoleType.USER);
    }



    @Test
    public void testGetUserById() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("usuario", result.getUsername());
        assertEquals("usuario@gmail.com", result.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }


    @Test
    public void testRegisterUser() {
        when(userRepository.existsByUsername("usuario")).thenReturn(false);
        when(userRepository.save(any(EntityUser.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserDTO result = userService.registerUser(userDTORequest);

        assertNotNull(result);
        assertEquals("usuario", result.getUsername());
        assertEquals("usuario@gmail.com", result.getEmail());

        verify(userRepository, times(1)).existsByUsername("usuario");
        verify(userRepository, times(1)).save(any(EntityUser.class));
        verify(passwordEncoder, times(1)).encode("usuario1234");
    }

    @Test
    public void testRegisterUserConflict() {
        when(userRepository.existsByUsername("usuario")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.registerUser(userDTORequest));
    }



    @Test
    public void testDeleteUser() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}


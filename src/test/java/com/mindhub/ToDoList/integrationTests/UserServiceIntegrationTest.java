package com.mindhub.ToDoList.integrationTests;


import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTORequest;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.repositories.UserRepository;
import com.mindhub.ToDoList.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
@SpringBootTest
@ActiveProfiles("test")
@Transactional // Sirve para que cada test se ejecute en una transacción independiente y se deshaga al finalizar.
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterAndFindUser() {
        // Registro un usuario
        EntityUser user = new EntityUser();
        user.setUsername("usuarioprueba");
        user.setEmail("usuario@gmail.com");
        user.setPassword("usuario1234");
        user.setRoleType(RoleType.USER);

        UserDTORequest userDTORequest = new UserDTORequest(user);
        UserDTO savedUser = userService.registerUser(userDTORequest); // el metodo registerUser interactúa con el repositorio

        // Me fijo si se guardó correctamente
        assertNotNull(savedUser.getId());
        assertEquals("usuarioprueba", savedUser.getUsername());

        // Busco el usuario por ID
        UserDTO retrievedUser = userService.getUserById(savedUser.getId()); // tambien interactua con el UserRepository
        assertNotNull(retrievedUser); // me fijo que no sea nulo
        assertEquals(savedUser.getUsername(), retrievedUser.getUsername()); // aquí comparo
    }
}
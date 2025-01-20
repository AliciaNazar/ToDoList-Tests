package com.mindhub.ToDoList.controllers.user;

import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTORequest;
import com.mindhub.ToDoList.services.TaskService;
import com.mindhub.ToDoList.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get authenticated user", description = "Retrieves the currently authenticated user's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated.")
    })
    @GetMapping(value = "/users") //, produces = MediaType.APPLICATION_JSON_VALUE
    public ResponseEntity<UserDTO> getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = this.userService.getUserByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Update authenticated user", description = "Updates the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated.")
    })
    @PutMapping(value = "/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTORequest userDTORequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = this.userService.getUserByUsername(username).getId();
        UserDTO updatedUser = this.userService.updateUser(id, userDTORequest);
        return ResponseEntity.ok(updatedUser);

    }

    @Operation(summary = "Delete authenticated user", description = "Deletes the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = this.userService.getUserByUsername(username).getId();
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


package com.mindhub.ToDoList.controllers.user;


import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;
import com.mindhub.ToDoList.services.TaskService;
import com.mindhub.ToDoList.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class TaskUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get all tasks for authenticated user", description = "Retrieves all tasks associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated.")
    })
    @GetMapping("/tasks/getAll")
    public ResponseEntity<List<TaskDTO>>getTasks(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = this.userService.getUserByUsername(username);
        List<TaskDTO> tasks = userDTO.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task by ID for authenticated user", description = "Retrieves a specific task by ID associated with the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) throws TaskNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = this.userService.getUserByUsername(username);
        List<TaskDTO> tasks = userDTO.getTasks();
        TaskDTO taskDTO = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException());
        return ResponseEntity.ok(taskDTO);
    }

    @Operation(summary = "Create task for authenticated user", description = "Creates a new task for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated.")
    })
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = this.userService.getUserByUsername(username);
        TaskDTO newTask = this.taskService.createTaskByUser(taskDTO,userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @Operation(summary = "Update task by ID for authenticated user", description = "Updates a specific task by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable("id") Long id,
            @RequestBody TaskDTO taskDTO) throws TaskNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = this.userService.getUserByUsername(username);
        List<TaskDTO> tasks = userDTO.getTasks();
        tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException());

        TaskDTO updatedTask = this.taskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete task by ID for authenticated user", description = "Deletes a specific task by ID for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) throws TaskNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); //con esto puedo ir al SecurityContext
        UserDTO userDTO = this.userService.getUserByUsername(username);             //y ver detalles de la autenticación actual
        List<TaskDTO> tasks = userDTO.getTasks();
        TaskDTO task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException());
        this.taskService.deleteTask(task.getId());
        return ResponseEntity.noContent().build();
    }




}
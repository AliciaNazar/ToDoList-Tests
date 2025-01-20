package com.mindhub.ToDoList.controllers.admin;

import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTORequest;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;
import com.mindhub.ToDoList.exceptions.UserNotFoundException;
import com.mindhub.ToDoList.services.TaskService;
import com.mindhub.ToDoList.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class TaskAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated or the provided credentials are invalid.")
    })
    @GetMapping("/tasks/getAll")
    public ResponseEntity<List<TaskDTO>> getTasks(){
        List<TaskDTO> taskDTOS = this.taskService.getTasks();
        return ResponseEntity.ok(taskDTOS);
    }

    @Operation(summary = "Get a task by ID", description = "Retrieves a task by the specified id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated or the provided credentials are invalid."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) throws TaskNotFoundException {
        TaskDTO taskDTO = this.taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @Operation(summary = "Create a task", description = "Creates a new task and assigns it to a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "403", description = "Admins cannot create tasks for other admins."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated or the provided credentials are invalid.")
    })
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTORequest taskDTORequest) throws UserNotFoundException {

        UserDTO targetUser = this.userService.getUserById(taskDTORequest.getUser().getId());
        if (targetUser.getRoleType().toString().contains("ADMIN")) { // un ADMIN no puede crear tareas a un admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        TaskDTO taskDTO = this.taskService.createTaskByAdmin(taskDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @Operation(summary = "Update a task by ID", description = "Updates the task corresponding to the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully."),
            @ApiResponse(responseCode = "404", description = "Task not found."),
            @ApiResponse(responseCode = "403", description = "Admins cannot update tasks assigned to other admins."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated or the provided credentials are invalid.")
    })
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable("id") Long id,
            @RequestBody TaskDTO taskDTO) throws TaskNotFoundException{
        UserDTO targetUser = this.userService.getUserById(id);
        if (targetUser.getRoleType().toString().contains("ADMIN")) { // un ADMIN no puede modificar tareas a un admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        TaskDTO updatedTask = this.taskService.updateTask(id,taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete a task by ID", description = "Deletes an existing task by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: The user is not authenticated or the provided credentials are invalid."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) throws TaskNotFoundException{
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}

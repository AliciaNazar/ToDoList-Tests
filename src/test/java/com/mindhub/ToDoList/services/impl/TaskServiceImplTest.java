package com.mindhub.ToDoList.services.impl;

import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.models.enums.TaskStatus;
import com.mindhub.ToDoList.models.Task;
import com.mindhub.ToDoList.repositories.TaskRepository;
import com.mindhub.ToDoList.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private EntityUser user;
    private TaskDTO taskDTO;
    private Task task;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new EntityUser();
        //user.setId(1L);
        user.setUsername("usuario");
        user.setEmail("usuario@gmail.com");
        user.setPassword("usuario1234");
        user.setRoleType(RoleType.USER);

        when(userRepository.findByUsername("usuario")).thenReturn(java.util.Optional.of(user));


        taskDTO = new TaskDTO();
        taskDTO.setTitle("Tarea importante");
        taskDTO.setDescription("Descripción de la tarea");
        taskDTO.setStatus(TaskStatus.PENDING);

        task = new Task();
        task.setTitle("Tarea 1");
        task.setDescription("Descripción de la tarea");
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);

    }


    @Test
    public void testGetTaskById() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Tarea 1", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskDTO> result = taskService.getTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tarea 1", result.get(0).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testCreateTaskByUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("usuario");

        when(userRepository.findByUsername("usuario")).thenReturn(java.util.Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(new Task("Tarea importante", "Descripción de la tarea", TaskStatus.PENDING, user));

        TaskDTO result = taskService.createTaskByUser(taskDTO, userDTO);

        assertNotNull(result);
        assertEquals("Tarea importante", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());

        verify(userRepository, times(1)).findByUsername("usuario");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() throws TaskNotFoundException {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Tarea Actualizada");
        taskDTO.setDescription("Descripción Actualizada");
        taskDTO.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.updateTask(1L, taskDTO);

        assertNotNull(result);
        assertEquals("Tarea Actualizada", result.getTitle());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    public void testDeleteTask() throws TaskNotFoundException {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }



}

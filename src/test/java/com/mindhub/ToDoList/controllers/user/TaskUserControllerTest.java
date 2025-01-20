package com.mindhub.ToDoList.controllers.user;


import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;
import com.mindhub.ToDoList.models.Task;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.services.TaskService;
import com.mindhub.ToDoList.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "usuarioprueba",authorities="USER")
public class TaskUserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private EntityUser user;
    private UserDTO userDTO;
    private List<TaskDTO> tasks;
    private Task task1;
    private Task task2;
    private String uri = "/api/user/tasks";

    @BeforeEach
    public void setUp() {

        user = new EntityUser();
        user.setId(1L);
        user.setUsername("usuarioprueba");
        user.setEmail("usuario@gmail.com");
        user.setPassword("usuario1234");

        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Estudiar");
        task1.setDescription("Estudiar Java");
        task1.setUser(user);

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Emails");
        task2.setDescription("Revisar y responder emails");
        task2.setUser(user);

        TaskDTO taskDTO1 = new TaskDTO(task1);
        TaskDTO taskDTO2 = new TaskDTO(task2);

        tasks = List.of(taskDTO1,taskDTO2);

        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setTasks(tasks);
    }

    @Test
    public void testGetTasks() throws Exception {
        when(userService.getUserByUsername("usuarioprueba")).thenReturn(userDTO);

        mockMvc.perform(get(uri+"/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(tasks.size()))
                .andExpect(jsonPath("$[0].id").value(tasks.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(tasks.get(0).getTitle()));
    }

    @Test
    public void testGetTaskById() throws Exception {
        when(userService.getUserByUsername("usuarioprueba")).thenReturn(userDTO);

        mockMvc.perform(get(uri+"/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(userDTO.getTasks().get(0).getTitle()));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        when(userService.getUserByUsername("usuarioprueba")).thenReturn(userDTO);

        mockMvc.perform(get(uri+"/22")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleteTask() throws Exception {
        when(userService.getUserByUsername("usuarioprueba")).thenReturn(userDTO);

        mockMvc.perform(delete(uri+"/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        when(userService.getUserByUsername("usuarioprueba")).thenReturn(userDTO);
        doThrow(new TaskNotFoundException()).when(taskService).deleteTask(99L);

        mockMvc.perform(delete(uri+"/99"))
                .andExpect(status().isNotFound());
    }








}

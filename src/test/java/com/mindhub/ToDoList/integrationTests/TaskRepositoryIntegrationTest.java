package com.mindhub.ToDoList.integrationTests;

import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.Task;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.models.enums.TaskStatus;
import com.mindhub.ToDoList.repositories.TaskRepository;
import com.mindhub.ToDoList.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private Task task1;
    private EntityUser user;

    @BeforeEach
    public void setUp(){
        user = new EntityUser();
        user.setUsername("usuario");
        user.setEmail("usuario@gmail.com");
        user.setPassword("usuario1234");
        user.setRoleType(RoleType.USER);
        userRepository.save(user);

        task1 = new Task();
        task1.setTitle("Curso");
        task1.setDescription("Empezar curso de scrum");
        task1.setStatus(TaskStatus.PENDING);
        task1.setUser(user);
        taskRepository.save(task1);
    }

    // C: crear una nueva tarea
    @Test
    public void testCreateNewTask(){
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Leer documentación sobre test unitarios");

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
    }

    //R: obtener una tarea
    @Test
    public void testGetTaskById(){
        Optional<Task> retrievedTask = taskRepository.findById(task1.getId());
        assertTrue(retrievedTask.isPresent());
        assertEquals("Empezar curso de scrum", retrievedTask.get().getDescription());
    }

    //U: modificar una tarea
    @Test
    public void testUpdateTask(){
        task1.setTitle("Nuevo título");
        Task updatedTask = taskRepository.save(task1);
        assertEquals("Nuevo título", updatedTask.getTitle());
    }

    //D: eliminar una tarea
    @Test
    public void testDeleteTask(){
        taskRepository.delete(task1);
        assertFalse(taskRepository.findById(task1.getId()).isPresent());
    }



    @Test
    public void testExistsById(){
        boolean exists = taskRepository.existsById(task1.getId());
        assertTrue(exists);
    }

    @Test
    public void testFindById() { //verifico que la tarea se pueda encontrar y recuperar correctamente de la base de datos
        Task foundTask = taskRepository.findById(task1.getId()).orElse(null);
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo("Curso");
        assertThat(foundTask.getDescription()).isEqualTo("Empezar curso de scrum");
        assertThat(foundTask.getStatus()).isEqualTo(TaskStatus.PENDING);
    }
}





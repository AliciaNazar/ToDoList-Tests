package com.mindhub.ToDoList.services.impl;

import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTORequest;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.BusinessException;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;
import com.mindhub.ToDoList.exceptions.UserNotFoundException;
import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.Task;
import com.mindhub.ToDoList.repositories.TaskRepository;
import com.mindhub.ToDoList.repositories.UserRepository;
import com.mindhub.ToDoList.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskDTO getTaskById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException());
        return new TaskDTO(task);
    }

    @Override
    public List<TaskDTO> getTasks() {
        List<Task> tasks= taskRepository.findAll();
        List<TaskDTO> tasksDTOS = tasks.stream()
                .map(task -> new TaskDTO(task))
                .toList();
        return tasksDTOS;
    }

    @Override
    public TaskDTO createTaskByAdmin(TaskDTORequest taskDTORequest) {
        if (taskDTORequest.getTitle().isBlank()){throw new BusinessException("Title is required");}
        EntityUser user = this.userRepository.findById(taskDTORequest.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException());
        Task task = TaskDTORequest.toEntity(taskDTORequest,user); //creo una tarea y le asigno los valores de taskDTORequest
        task = this.taskRepository.save(task);
        return new TaskDTO(task);
    }

    @Override
    public TaskDTO createTaskByUser(TaskDTO taskDTO, UserDTO userDTO) {
        if (taskDTO.getTitle().isBlank()){throw new BusinessException("Title is required");}
        EntityUser user = this.userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new UserNotFoundException()); ;
        Task task = new Task(taskDTO.getTitle(),taskDTO.getDescription(),taskDTO.getStatus(),user);
        task = this.taskRepository.save(task);
        return new TaskDTO(task);
    }


    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) throws TaskNotFoundException{
        if (taskDTO.getTitle().isBlank()){throw new BusinessException("Title is required");}
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setUser(task.getUser());
        task = this.taskRepository.save(task);
        return new TaskDTO(task);

    }


    @Override
    public void deleteTask(Long id) throws TaskNotFoundException{
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException());
        taskRepository.deleteById(id);
    }

}

package com.mindhub.ToDoList.services;

import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTO;
import com.mindhub.ToDoList.dtos.taskDTOs.TaskDTORequest;
import com.mindhub.ToDoList.dtos.userDTOs.UserDTO;
import com.mindhub.ToDoList.exceptions.TaskNotFoundException;

import java.util.List;

public interface TaskService {

    TaskDTO getTaskById(Long id) throws TaskNotFoundException;
    TaskDTO createTaskByAdmin (TaskDTORequest taskDTORequest);
    TaskDTO createTaskByUser (TaskDTO taskDTO, UserDTO userDTO);

    List<TaskDTO> getTasks();
    TaskDTO updateTask (Long id,TaskDTO taskDTO) throws TaskNotFoundException;
    void deleteTask(Long id) throws TaskNotFoundException;
}

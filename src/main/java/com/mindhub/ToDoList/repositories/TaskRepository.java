package com.mindhub.ToDoList.repositories;

import com.mindhub.ToDoList.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    boolean existsById(Long id);
}

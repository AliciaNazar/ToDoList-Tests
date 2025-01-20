package com.mindhub.ToDoList;

import com.mindhub.ToDoList.models.EntityUser;
import com.mindhub.ToDoList.models.enums.RoleType;
import com.mindhub.ToDoList.models.Task;
import com.mindhub.ToDoList.models.enums.TaskStatus;
import com.mindhub.ToDoList.repositories.TaskRepository;
import com.mindhub.ToDoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ToDoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoListApplication.class, args);
	}

//	@Autowired
//	PasswordEncoder passwordEncoder; //en este caso para encriptar la password en la bd
//
//
//	@Bean
//	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository){
//		return args -> {
//			EntityUser user1 = new EntityUser("ali",passwordEncoder.encode("ali1234"),"ali@gmail.com");
//			user1.setRoleType(RoleType.USER);
//			userRepository.save(user1);
//			EntityUser user2 = new EntityUser("admin",passwordEncoder.encode("admin"),"admin@gmail.com");
//			user2.setRoleType(RoleType.ADMIN);
//			userRepository.save(user2);
//			EntityUser user3 = new EntityUser("usuario",passwordEncoder.encode("ali1234"),"usuario@gmail.com");
//			user1.setRoleType(RoleType.USER);
//			userRepository.save(user3);
//
//			Task task1 = new Task("Estudiar","estudiar html",TaskStatus.PENDING,user1);
//			Task task2 = new Task("Leer","leer la documentación",TaskStatus.PENDING,user2);
//			Task task3 = new Task("Leer","leer la documentación",TaskStatus.PENDING,user3);
//			taskRepository.save(task1);
//			taskRepository.save(task2);
//			taskRepository.save(task3);
//		};
//	}

}

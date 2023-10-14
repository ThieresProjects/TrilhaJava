package br.com.thieres.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskViewModel, UUID> {
    List<TaskViewModel> findByCodUser(UUID codUser);
    TaskViewModel findByCodTaskAndCodUser(UUID codTask,UUID codUser);
}

package br.com.thieres.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thieres.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskContorller {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> created ( @RequestBody TaskViewModel ent, HttpServletRequest request ) {

        ent.setCodUser( (UUID) request.getAttribute("codUser") );

        var currentDate = LocalDateTime.now();
        if(
             currentDate.isAfter( ent.getStartAt() ) ||
             currentDate.isAfter( ent.getEndAt() ) 
        )
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de inicio / término deve ser maior que a atual.");
        }
        if (
            ent.getStartAt().isAfter( ent.getEndAt() ) 
        )
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de inicio deve ser menor do que a data de término.");
        }

        this.taskRepository.save(ent);
        return ResponseEntity.status(201).body(ent);

    }

    @GetMapping("/list")
    public List<TaskViewModel> list( HttpServletRequest request ) {
        var codUser = request.getAttribute("codUser");
        var tasks =this.taskRepository.findByCodUser((UUID) codUser);
        return tasks;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update (

        @RequestBody TaskViewModel ent,
        @PathVariable UUID id,
        HttpServletRequest request

    ) {

        var task = this.taskRepository.findById((UUID) id).orElse(null);

        var codUser = request.getAttribute("codUser");

        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Tarefa não encontrada.");            
        }

        if(! task.getCodUser().equals(codUser) ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Usuario não tem permisão para alterar essa tarefa.");
        }

        Utils.copyNonNullProperties(ent, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok()
            .body(taskUpdated);
    }
}

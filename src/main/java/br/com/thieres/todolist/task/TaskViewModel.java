package br.com.thieres.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "db_Tasks")
public class TaskViewModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID codTask;

    @Column(length = 50)
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    
    private UUID codUser;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {

        if(title.length() > 50){
            throw new Exception("O campo title deve conter no maximo 50 caracteres");
        }
        
        this.title = title;
    }
}

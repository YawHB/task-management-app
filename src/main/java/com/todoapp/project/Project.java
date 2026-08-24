package com.todoapp.project;


import com.todoapp.tasks.model.TodoTask;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Project name must be specified")
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    //TODO - the Organisation entity is not accessible in other packages. consider using the org's ID instead
    /*@ManyToOne
    @JoinColumn(name ="organisation_id", nullable = false)
    private Organisation organisation;
     */

    @OneToMany(mappedBy = "project")
    private List<TodoTask> tasks;


}

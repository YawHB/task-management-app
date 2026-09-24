package com.todoapp.task.internal.model;

import com.todoapp.task.internal.Priority;
import com.todoapp.task.internal.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
// Data = getters + setters
@AllArgsConstructor
// creates a constructor with all arguments
@NoArgsConstructor
// no argument / default constructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "todo_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String title;

    private String description;

    //TODO - Can i point to parentTaks, just like this?
   private UUID parentTask;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private UUID project;

    private LocalDateTime dueDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "task")
    private List<TaskComment> comments;

    @Enumerated(EnumType.STRING) // Stores the enum name as text in the database
    @Column(nullable = false)
    private Status status = Status.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @ManyToMany(mappedBy = "tasks")
    private List<Label> labels;
}








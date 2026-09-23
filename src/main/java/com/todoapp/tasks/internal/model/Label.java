package com.todoapp.tasks.internal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Label {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

@NotBlank
private String name;

@NotBlank
private String color;

@ManyToMany
@JoinTable(
        name = "task_label",
        joinColumns = @JoinColumn(name = "label_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
)
    private List<TodoTask> tasks;

}


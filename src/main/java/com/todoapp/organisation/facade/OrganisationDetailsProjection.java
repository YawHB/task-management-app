package com.todoapp.organisation.facade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class OrganisationDetailsProjection {

    private UUID organisationId;
    private String organisationName;
    private UUID projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime activeSince;
}

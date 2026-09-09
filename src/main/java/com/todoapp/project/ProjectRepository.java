package com.todoapp.project;

import com.task_management_app.api.model.ProjectDTO;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String name);

    @Query(value = "SELECT created_at, id, organisation_id, name " +
                   "FROM project " +
                   "WHERE organisation_id = :organisationId " +
                   "AND name = :name", nativeQuery = true
    )
    Optional<Project> findProjectByOrganisationIdAndName(UUID organisationId, String name);


    @Modifying
    @Query(value = "INSERT INTO projects_users (project_id, user_id)" +
                   " VALUES (:projectId, :userId)",
                   nativeQuery = true)
    void saveProjectUser(UUID projectId, UUID userId);
}

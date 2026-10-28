package com.todoapp.project.internal.repository;

import com.todoapp.project.facade.ProjectProjection;
import com.todoapp.project.facade.ProjectSummaryProjection;
import com.todoapp.project.internal.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {


    @Query(value = "SELECT p.id AS projectId, p.organisation_id AS organisationId, p.name AS projectName, p.created_at AS createdAt, " +
            "u.id AS userId, u.first_name AS firstName, u.last_name AS lastName, u.email, u.username " +
            "FROM project AS p " +
            "LEFT JOIN projects_users AS pu ON pu.project_id = p.id " +
            "LEFT JOIN users AS u ON u.id = pu.user_id " +
            "WHERE p.id = :projectId",
            nativeQuery = true)
    ProjectProjection findProjectWithMembersById(UUID projectId);


    @Query(value = "SELECT PROJECT_FILTERED_BY_ORG.project_id, " +
                   "PROJECT_FILTERED_BY_ORG.name " +
            "FROM (" +
                     "SELECT project.id AS project_id, " +
                             "project.name " +
                     "FROM project " +
                     "WHERE organisation_id = :organisationId " +
            ") AS PROJECT_FILTERED_BY_ORG " +

                      "INNER JOIN ( " +
                   "SELECT pu.project_id " +
                   "FROM projects_users AS pu " +
                   "WHERE user_id = :userId " +
            ") AS PROJECT_FILTERED_BY_USER " +

                      "ON PROJECT_FILTERED_BY_ORG.project_id = " +
                      "PROJECT_FILTERED_BY_USER.project_id",
            nativeQuery = true)
    List<ProjectSummaryProjection> findProjectsForMemberInOrganisation(UUID organisationId, UUID userId);

    Optional<Project> findByName(String name);

    @Query(value = "SELECT * FROM project WHERE project.id = :projectId ",
            nativeQuery = true)
    Optional<Project> findById(UUID projectId);


    @Query(value = "SELECT p.created_at, p.id, organisation_id, p.name " +
                   "FROM project AS p " +
                   "WHERE organisation_id = :organisationId " +
                   "AND p.name = :name", nativeQuery = true
    ) Optional<Project> findProjectByOrganisationIdAndName(UUID organisationId, String name);


    @Modifying
    @Query(value = "INSERT INTO projects_users (project_id, user_id)" +
                   " VALUES (:projectId, :userId)",
                   nativeQuery = true)
    void saveProjectUser(UUID projectId, UUID userId);


    @Modifying
    @Query(value = "DELETE FROM projects_users " +
                   "WHERE project_id = :projectId " +
                   "AND user_id = :userId",
                    nativeQuery = true)
    void removeUserFromProject(UUID projectId, UUID userId);

    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM projects_users " +
            "WHERE project_id = :projectId " +
            "AND user_id = :userId",
            nativeQuery = true)
    boolean userExistInProject(UUID projectId, UUID userId);

    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM project " +
            "WHERE name = :name",
            nativeQuery = true)
    boolean existsByName(String name );
}

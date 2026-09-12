package com.todoapp.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {




    @Query(value = "SELECT COUNT(*) > 0 " +
                   "FROM organisations_users " +
                   "WHERE organisation_id = :organisationId " +
                   "AND user_id = :userId",
                    nativeQuery = true)
    boolean userExistInOrganisation(UUID organisationId, UUID userId);


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

}

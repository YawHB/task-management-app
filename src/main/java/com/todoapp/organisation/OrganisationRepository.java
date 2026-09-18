package com.todoapp.organisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
 interface OrganisationRepository extends JpaRepository<Organisation, UUID> {

 @Query(value = "SELECT PROJECT_FILTERED_BY_ORG.organisation_id, " +
                "o.name, " +
                "PROJECT_FILTERED_BY_ORG.id AS project_id, " +
                "PROJECT_FILTERED_BY_ORG.name AS project_name, " +
                "PROJECT_FILTERED_BY_ORG.created_at, " +


                "ou.user_id, " +
                "u.first_name AS first_name, " +
                "u.last_name AS last_name, " +
                "u.email AS email, " +
                "ou.active_since " +
                "FROM(" +
                      "SELECT " +
                      "p.id, " +
                      "p.name, " +
                      "p.created_at, " +
                      "p.organisation_id " +
                "FROM project AS p " +
                "WHERE organisation_id = :id) " +
                "AS PROJECT_FILTERED_BY_ORG " +
                "INNER JOIN organisation AS o " +
                "ON PROJECT_FILTERED_BY_ORG.organisation_id = o.id " +
                "INNER JOIN organisations_users AS ou " +
                "ON o.id = ou.organisation_id " +
                "INNER JOIN users AS u " +
                "ON ou.user_id = u.id " +
                "ORDER BY PROJECT_FILTERED_BY_ORG.name ASC",
                  nativeQuery = true)
 List<OrganisationDetailsProjection> findOrganisationWithProjectsAndMembers(UUID id);
}

package com.todoapp.organisation;

import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
 interface OrganisationRepository extends JpaRepository<Organisation, UUID> {


 @Modifying
 @Query(value = "INSERT INTO organisations_users (active_since, id, organisation_id, user_id) " +
                " VALUES (:activeSince, :id, :organisationId, :userId)", nativeQuery = true)
 void addUserToOrganisation(LocalDateTime activeSince, UUID id, UUID organisationId, @Valid UUID userId);

 @Query(value = "SELECT COUNT (*) > 0 FROM organisations_users " +
                "WHERE organisations_users.organisation_id = :orgId " +
                "AND organisations_users.user_id = :userId",
                nativeQuery = true)
 boolean userExistInOrganisation(@Param("orgId") UUID orgId,@Param("userId") UUID userId);

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

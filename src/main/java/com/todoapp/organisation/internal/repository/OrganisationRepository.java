package com.todoapp.organisation.internal.repository;

import com.todoapp.organisation.facade.OrganisationDetailsProjection;
import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.user.facade.OrganisationProjection;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {

    @Modifying
    @Query(value = "INSERT INTO organisations_users (active_since, id, organisation_id, user_id) " +
                    "VALUES (:activeSince, :id, :organisationId, :userId)",
                     nativeQuery = true)
    void addUserToOrganisation(LocalDateTime activeSince, UUID id, UUID organisationId, @Valid UUID userId);

    @Query(value = "SELECT " +
            "o.id AS organisation_id, o.name AS organisaion_name, " +
            "p.id AS project_id, p.name AS project_name, p.created_at AS createdAt, " +
            "ou.user_id AS user_id, ou.active_since AS active_since, u.first_name AS first_name, " +
            "u.last_name AS last_name, u.email AS email " +
            "FROM organisation AS o " +
            "LEFT JOIN project AS p " +
            "ON o.id = p.organisation_id " +
            "LEFT JOIN organisations_users AS ou " +
            "ON o.id = ou.organisation_id " +
            "LEFT JOIN users AS u " +
            "ON ou.user_id = u.id " +
            "WHERE o.id = :id " +
            "ORDER BY p.name ASC ",
            nativeQuery = true)
    List<OrganisationDetailsProjection> findOrganisationWithProjectsAndMembers(UUID id);

    @Query(value = "SELECT o.id AS organisationId, o.name AS organisationName, " +
            "o.creator_id AS creatorId " +
            "FROM organisation AS o " +
            "INNER JOIN organisations_users ou ON o.id = ou.organisation_id " +
            "WHERE ou.user_id = :userId",
            nativeQuery = true)
    List<OrganisationProjection> findOrganisationsByUserId(UUID userId);

    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM organisations_users " +
            "WHERE organisation_id = :organisationId " +
            "AND user_id = :userId",
            nativeQuery = true)
    boolean userExistInOrganisation(UUID organisationId, UUID userId);

    boolean existsByName(String name);



}

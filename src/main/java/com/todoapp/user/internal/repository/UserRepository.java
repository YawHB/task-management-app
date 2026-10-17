package com.todoapp.user.internal.repository;

import com.todoapp.user.facade.OrganisationProjection;
import com.todoapp.user.internal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT o.id AS organisationId, o.name AS organisationName, " +
                   "o.creator_id AS creatorId " +
                   "FROM organisation AS o " +
                   "INNER JOIN organisations_users ou ON o.id = ou.organisation_id " +
                   "WHERE ou.user_id = :userId",
            nativeQuery = true)
    List<OrganisationProjection> findOrganisationsByUserId(@Param("userId")UUID userId);

    Optional<User> findByEmail(String userEmail);

    @Query(value = "SELECT COUNT(*) > 0 FROM organisations_users " +
                   "WHERE user_id = :userId "+
                   "AND organisation_id = :organisationId",
                    nativeQuery = true)
    boolean existsByOrganisationIdAndUserId(UUID userId, UUID organisationId);
}

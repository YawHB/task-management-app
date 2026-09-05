package com.todoapp.organisation;

import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

package com.todoapp.users;

import com.todoapp.users.repository.OrganisationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {




    @Query(value = "SELECT o.id AS organisationId, o.name AS organisationName " +
            "FROM organisation o " +
            "INNER JOIN organisations_users ou ON o.id = ou.organisation_id " +
            "WHERE ou.user_id = :userId",
            nativeQuery = true)

    List<OrganisationProjection> findOrganisationsByUserId(@Param("userId")UUID userId);
    Optional<User> findByEmail(String userEmail);

}

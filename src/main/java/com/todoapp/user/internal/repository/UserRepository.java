package com.todoapp.user.internal.repository;

import com.task_management_app.api.model.UserDTO;
import com.task_management_app.api.model.UserSummaryDTO;
import com.todoapp.user.facade.OrganisationProjection;
import com.todoapp.user.facade.UserSummaryProjection;
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

    Optional<User> findByEmail(String userEmail);

    @Query(value = "SELECT pu.user_id AS id, u.first_name AS first_name, u.last_name, u.username, u.email " +
            "FROM projects_users AS pu " +
            "INNER JOIN users AS u ON u.id = pu.user_id " +
            "WHERE pu.project_id = :projectId",
            nativeQuery = true)
    List<UserSummaryProjection> findMembersInProject(UUID projectId);

    @Query(value = "SELECT ou.user_id AS id, u.first_name AS first_name, u.last_name, u.username, u.email " +
            "FROM organisations_users AS ou " +
            "INNER JOIN users AS u ON u.id = ou.user_id " +
            "WHERE ou.organisation_id = :organisationId",
            nativeQuery = true)
    List<UserSummaryProjection> findMembersInOrganisation(UUID organisationId);

    @Query(value = "SELECT COUNT(*) > 0 FROM projects_users " +
                   "WHERE user_id = :assigneeId " +
                   "AND project_id = :projectId",
                    nativeQuery = true)
    boolean userMemberOfProject(UUID assigneeId, UUID projectId);

    @Query(value = "SELECT COUNT(*) > 0 FROM organisations_users " +
                   "WHERE user_id = :userId "+
                   "AND organisation_id = :organisationId",
                    nativeQuery = true)
    boolean userMemberOfOrganisation(UUID userId, UUID organisationId);

}

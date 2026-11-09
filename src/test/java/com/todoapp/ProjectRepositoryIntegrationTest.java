package com.todoapp;

import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.project.facade.ProjectSummaryProjection;
import com.todoapp.project.internal.model.Project;
import com.todoapp.project.internal.repository.ProjectRepository;
import com.todoapp.user.internal.model.User;
import com.todoapp.user.internal.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:file:./h2-test-db",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@ContextConfiguration(classes =
        ProjectRepositoryIntegrationTest.JpaOnlyConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class ProjectRepositoryIntegrationTest {
    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = {Project.class, User.class, Organisation.class})
    @EnableJpaRepositories(basePackageClasses = {ProjectRepository.class, UserRepository.class})
    static class JpaOnlyConfig {
    }

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    private static final String CREATE_PROJECT_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS projects_users (
                id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                user_id UUID NOT NULL,
                project_id UUID NOT NULL,
                CONSTRAINT fk_project_users_user
                    FOREIGN KEY (user_id)
                    REFERENCES users(id)
                    ON DELETE CASCADE,
                CONSTRAINT fk_project_users_project
                    FOREIGN KEY (project_id)
                    REFERENCES project(id)
                    ON DELETE CASCADE
            )
            """;

    @Test
    public void findProjectsForMemberInOrganisation_returnsTheListOfProjectForMemberInOrganisation() {
        //ARRANGE

        Project expectedProject = projectRepository.saveAndFlush(FakeTestDataGenerator.
                createProjects(1).getFirst());

        User user = FakeTestDataGenerator.createUsers(1).getFirst();
        User expectedUser = userRepository.saveAndFlush(user);

        entityManager.clear();

        entityManager.createNativeQuery(CREATE_PROJECT_USERS_TABLE).executeUpdate();

        entityManager.createNativeQuery("INSERT INTO projects_users(user_id, project_id) " +
                        "VALUES(:userId, :projectId)")
                .setParameter("userId", expectedUser.getId())
                .setParameter("projectId", expectedProject.getId()).executeUpdate();

        entityManager.flush();
        entityManager.clear();

        UUID organisationId = expectedProject.getOrganisationId();

        //ACT

        List<ProjectSummaryProjection> actualProjects =
                projectRepository.findProjectsForMemberInOrganisation(organisationId, expectedUser.getId());

        //ASSERT
        assertEquals(1, actualProjects.size());

    }
}

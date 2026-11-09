package com.todoapp.integrationTest;

import com.todoapp.FakeTestDataGenerator;
import com.todoapp.UserRepositoryIntegrationTest;
import com.todoapp.organisation.internal.model.Organisation;
import com.todoapp.organisation.internal.repository.OrganisationRepository;
import com.todoapp.project.internal.model.Project;
import com.todoapp.project.internal.repository.ProjectRepository;
import com.todoapp.task.internal.model.Task;
import com.todoapp.task.internal.repository.TaskRepository;
import com.todoapp.user.internal.model.User;
import com.todoapp.user.internal.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:file:./h2-test-db",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@ContextConfiguration(classes =
        TaskRepositoryIntegrationTest.JpaOnlyConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class TaskRepositoryIntegrationTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = {
            Task.class,
            User.class,
            Project.class})

    @EnableJpaRepositories(basePackageClasses = {
            TaskRepository.class,
            UserRepository.class,
            ProjectRepository.class})
    static class JpaOnlyConfig {}

   @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void existByIdAndProjectId_returnsTrueWhenTaskExistsInProject(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);

        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        task = taskRepository.saveAndFlush(task);

        //ACT
        boolean taskExists = taskRepository.existByIdAndProjectId(task.getId(), project.getId());

        //ASSERT
        assertTrue(taskExists);
    }

    @Test
    public void existByIdAndProjectId_returnsFalseWhenTaskDoesNotExist(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);

        //ACT
        boolean taskExists = taskRepository.existByIdAndProjectId(UUID.randomUUID(),project.getId());

        //ASSERT
        assertFalse(taskExists);
    }

    @Test
    public void existByIdAndProjectId_returnsFalseWhenTaskBelongsToDifferentProject(){
        //ARRANGE

        UUID otherProjectId = UUID.randomUUID();
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        projectRepository.saveAndFlush(project);

        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        taskRepository.saveAndFlush(task);

        //ACT
        boolean taskExistsInProject = taskRepository.existByIdAndProjectId(task.getId(), otherProjectId);
        //ASSERT
        assertFalse(taskExistsInProject);
    }

    @Test
    public void taskTitleExistsInProject_returnsTrueWhenTaskTitleExistsInProject(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);

        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        task = taskRepository.saveAndFlush(task);

        //ACT
        boolean taskExists = taskRepository.taskTitleExistsInProject(task.getProjectId(), task.getTitle());

        //ASSERT
        assertTrue(taskExists);
    }

    @Test
    public void taskTitleExistsInProject_returnsFalseWhenTaskTitleDoesNotExist(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);

        String nonExistingTitle = "nonExistingTitle";
        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        task = taskRepository.saveAndFlush(task);

        //ACT
        boolean taskExists = taskRepository.taskTitleExistsInProject(task.getProjectId(), nonExistingTitle);

        //ASSERT
        assertFalse(taskExists);
    }

    @Test
    public void taskTitleExistsInProject_returnsFalseWhenProjectHasNoTasks(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);
        //ACT
        //ASSERT
    }

    //ARRANGE
    //ACT
    //ASSERT
}

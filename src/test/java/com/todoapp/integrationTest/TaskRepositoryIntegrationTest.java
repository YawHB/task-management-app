package com.todoapp.integrationTest;

import com.todoapp.FakeTestDataGenerator;
import com.todoapp.project.internal.model.Project;
import com.todoapp.project.internal.repository.ProjectRepository;
import com.todoapp.task.facade.TaskSummaryProjection;
import com.todoapp.task.internal.model.Task;
import com.todoapp.task.internal.repository.TaskRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

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

    @Test
    public void findByIdAndProjectId_returnsTaskWhenTaskExistsInProject(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        Task expectedTask = taskRepository.saveAndFlush(task);

        //ACT
        Optional<Task> actualTask = taskRepository.findByIdAndProjectId(expectedTask.getId(), expectedTask.getProjectId());

        //ASSERT
        assertTrue(actualTask.isPresent());
        assertEquals(expectedTask.getId(), actualTask.get().getId());
        assertEquals(expectedTask.getProjectId(), expectedProject.getId());

    }

    @Test
    public void findByIdAndProjectId_returnsEmptyWhenTaskDoesNotExist(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        //ACT
       Optional<Task> foundTask = taskRepository.findByIdAndProjectId(UUID.randomUUID(), expectedProject.getId());

        //ASSERT
        assertTrue(foundTask.isEmpty());
    }

    @Test
    public void findByIdAndProjectId_returnsEmptyWhenTaskBelongsToDifferentProject(){
        //ARRANGE
        UUID unrelatedProjectId = UUID.randomUUID();
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        Task task = FakeTestDataGenerator.createTasks(1).getFirst();
        task.setProjectId(project.getId());
        Task expectedTask = taskRepository.saveAndFlush(task);

        //ACT
         Optional<Task> taskFound = taskRepository.findByIdAndProjectId(expectedTask.getId(), unrelatedProjectId);

        //ASSERT
        assertTrue(taskFound.isEmpty());
    }

    @Test
    public void findTasksInProject_returnsTasksWhenTasksExistInProject(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task> expectedTasks = taskRepository.saveAllAndFlush(tasks);

        Sort defaultSorting = Sort.by(Sort.Direction.ASC, "created_at");

        //ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), defaultSorting);
        //ASSERT
        assertEquals(3, actualTasks.size());

        assertTrue(actualTasks.get(0).getCreatedAt()
                .isBefore(actualTasks.get(1).getCreatedAt()));

        assertTrue(actualTasks.get(1).getCreatedAt()
                .isBefore(actualTasks.get(2).getCreatedAt()));

        assertEquals(expectedTasks.get(0).getTitle(), actualTasks.get(0).getTitle());
        assertEquals(expectedTasks.get(1).getTitle(), actualTasks.get(1).getTitle());
        assertEquals(expectedTasks.get(2).getTitle(), actualTasks.get(2).getTitle());
    }

    @Test
    public void findTasksInProject_returnsEmptyListWhenProjectHasNoTasks(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        project = projectRepository.saveAndFlush(project);

        //ACT
        Sort defaultSorting = Sort.by(Sort.Direction.ASC, "created_at");
        List<TaskSummaryProjection> tasks = taskRepository.findTasksInProject(project.getId(), defaultSorting);

        //ASSERT
        assertTrue(tasks.isEmpty());

    }

    @Test
    public void findTasksInProject_returnsTasksWhenTaskHasNoAssignee(){
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> {
        task.setProjectId(expectedProject.getId());
        task.setAssigneeId(null);
        });

         taskRepository.saveAllAndFlush(tasks);

        //ACT
        Sort defaultSorting = Sort.by(Sort.Direction.ASC, "created_at");
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(project.getId(), defaultSorting);

        //ASSERT
        assertEquals(3, actualTasks.size());
        assertAll(actualTasks.stream()
                .map(task -> () -> assertNull(task.getFirstName())));

        assertAll(actualTasks.stream()
                .map(task -> () -> assertNull(task.getLastName())));
    }


    @Test
    public void findTasksInProject_returnsTasksSortedByCreatedAtAscending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task> expectedTasks = taskRepository.saveAllAndFlush(tasks);

        Sort createdAtAscending = Sort.by(Sort.Direction.ASC, "created_at");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), createdAtAscending);

        // ASSERT
        assertEquals(3, actualTasks.size());


        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {
                assertTrue(actualTasks.get(i).getCreatedAt()
                        .isBefore(actualTasks.get(i+1).getCreatedAt()));
            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getCreatedAt(),actualTasks.get(i).getCreatedAt());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByCreatedAtDescending() {
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
         tasks = taskRepository.saveAllAndFlush(tasks);
         tasks.sort(Comparator.comparing(task -> task.getCreatedAt()));
        List<Task> expectedTasks = tasks.reversed();

        Sort createdAtDescending = Sort.by(Sort.Direction.DESC, "created_at");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), createdAtDescending);

        // ASSERT
        assertEquals(3, actualTasks.size());

        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {
                assertTrue(actualTasks.get(i).getCreatedAt()
                        .isAfter(actualTasks.get(i+1).getCreatedAt()));
            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getCreatedAt(),actualTasks.get(i).getCreatedAt());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByDueDateAscending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task> expectedTasks = taskRepository.saveAllAndFlush(tasks);
        expectedTasks.sort(Comparator.comparing(task -> task.getDueDate()));

        Sort dueDateAscending = Sort.by(Sort.Direction.ASC, "due_date");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), dueDateAscending);

        // ASSERT
        assertEquals(3, actualTasks.size());

        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {
                assertTrue(actualTasks.get(i).getDueDate()
                        .isBefore(actualTasks.get(i+1).getDueDate()));
            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getDueDate(),actualTasks.get(i).getDueDate());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByDueDateDescending() {
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        tasks = taskRepository.saveAllAndFlush(tasks);
        tasks.sort(Comparator.comparing(task -> task.getDueDate()));
        List<Task> expectedTasks = tasks.reversed();

        Sort dueDateDescending = Sort.by(Sort.Direction.DESC, "due_date");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), dueDateDescending);

        // ASSERT
        assertEquals(3, actualTasks.size());

        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {
                assertTrue(actualTasks.get(i).getDueDate()
                        .isAfter(actualTasks.get(i+1).getDueDate()));
            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getDueDate(),actualTasks.get(i).getDueDate());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByTitleAscending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task> expectedTasks = taskRepository.saveAllAndFlush(tasks);
        expectedTasks.sort(Comparator.comparing(task -> task.getTitle()));

        Sort titleAscending = Sort.by(Sort.Direction.ASC, "title");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), titleAscending);

        // ASSERT
        assertEquals(3, actualTasks.size());

        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {

                assertTrue(actualTasks.get(i).getTitle()
                        .compareTo(actualTasks.get(i+1).getTitle()) < 0);
            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getTitle(),actualTasks.get(i).getTitle());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByTitleDescending() {
        //ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        tasks = taskRepository.saveAllAndFlush(tasks);
        tasks.sort(Comparator.comparing(task -> task.getTitle()));
        List<Task> expectedTasks = tasks.reversed();

        Sort titleDescending = Sort.by(Sort.Direction.DESC, "title");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), titleDescending);

        // ASSERT
        assertEquals(3, actualTasks.size());

        assertAll(() -> {
            for(int i = 0; i<actualTasks.size()-1; i++) {
                assertTrue(actualTasks.get(i).getTitle()
                        .compareTo(actualTasks.get(i+1).getTitle()) > 0);

            }
        });

        assertAll(() -> {
            for(int i = 0; i < actualTasks.size(); i++){
                assertEquals(expectedTasks.get(i)
                        .getTitle(),actualTasks.get(i).getTitle());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByPriorityAscending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task>  expectedTasks = taskRepository.saveAllAndFlush(tasks);
        expectedTasks.sort(Comparator.comparing(task -> task.getPriority().name())); // name() Sorts the Priority ENUMs as Strings, instead of the natural order I defined them

        Sort priorityAsc = Sort.by(Sort.Direction.ASC, "priority");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), priorityAsc);

        // ASSERT
        assertEquals(3, actualTasks.size());
        assertAll(()-> {
            for(int i = 0; i< actualTasks.size(); i++) {
                assertEquals(expectedTasks.get(i).getPriority(), actualTasks.get(i).getPriority());
                assertEquals(expectedTasks.get(i).getTitle(), actualTasks.get(i).getTitle());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByPriorityDescending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
          List<Task> foundTasks = taskRepository.saveAllAndFlush(tasks);
        foundTasks.sort(Comparator.comparing(task -> task.getPriority().name()));
        List<Task> expectedTasks = foundTasks.reversed();

        Sort priorityDesc = Sort.by(Sort.Direction.DESC, "priority");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), priorityDesc);

        // ASSERT
        assertEquals(3, actualTasks.size());
        assertAll(()-> {
            for(int i = 0; i< actualTasks.size(); i++) {
                assertEquals(expectedTasks.get(i).getPriority(), actualTasks.get(i).getPriority());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByStatusAscending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task>  expectedTasks = taskRepository.saveAllAndFlush(tasks);
        expectedTasks.sort(Comparator.comparing(task -> task.getStatus().name())); // name() Sorts the Status ENUMs as Strings, instead of the natural order I defined them

        Sort statusAsc = Sort.by(Sort.Direction.ASC, "status");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), statusAsc);

        // ASSERT
        assertEquals(3, actualTasks.size());
        assertAll(()-> {
            for(int i = 0; i< actualTasks.size(); i++) {
                assertEquals(expectedTasks.get(i).getStatus(), actualTasks.get(i).getStatus());
                assertEquals(expectedTasks.get(i).getTitle(), actualTasks.get(i).getTitle());
            }
        });
    }

    @Test
    public void findTasksInProject_returnsTasksSortedByStatusDescending() {
        // ARRANGE
        Project project = FakeTestDataGenerator.createProjects(1).getFirst();
        Project expectedProject = projectRepository.saveAndFlush(project);

        List<Task> tasks = FakeTestDataGenerator.createTasks(3);
        tasks.forEach(task -> task.setProjectId(expectedProject.getId()));
        List<Task> foundTasks = taskRepository.saveAllAndFlush(tasks);
        foundTasks.sort(Comparator.comparing(task -> task.getStatus().name()));

        List<Task> expectedTasks = foundTasks.reversed();

        Sort statusDesc = Sort.by(Sort.Direction.DESC, "status");

        // ACT
        List<TaskSummaryProjection> actualTasks = taskRepository.findTasksInProject(expectedProject.getId(), statusDesc);

        // ASSERT
        assertEquals(3, actualTasks.size());
        assertAll(()-> {
            for(int i = 0; i< actualTasks.size(); i++) {
                assertEquals(expectedTasks.get(i).getStatus(), actualTasks.get(i).getStatus());
            }
        });
    }
}

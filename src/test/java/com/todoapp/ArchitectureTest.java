package com.todoapp;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


public class ArchitectureTest {

    private final String ROOT_PATH = "com.todoapp";
    private final String ORGANISATION_PACKAGE = "..organisation..";
    private final String ORGANISATION_INTERNAL = "..organisation.internal..";
    private final String PROJECT_PACKAGE = "..project..";
    private final String PROJECT_INTERNAL = "..project.internal..";
    private final String TASK_PACKAGE = "..task..";
    private final String TASK_INTERNAL = "..task.internal..";
    private final String USER_PACKAGE = "..user..";
    private final String USER_INTERNAL = "..user.internal..";
    JavaClasses importedClasses = new ClassFileImporter().importPackages(ROOT_PATH);


    /**
     * ***** ORGANISATION.INTERNAL is ONLY accessible for organisation module ******
     * ORGANISATION.internal  - may not be accessed by (any other module)
     * project_cannot_access_organisation_internal
     * tasks_cannot_access_organisation_internal
     * user_cannot_access_organisation_internal
     * */


    @Test
    void project_cannot_access_organisation_internal() {
        noClasses()
                .that().resideInAnyPackage(PROJECT_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(ORGANISATION_INTERNAL)
                .check(importedClasses);
    }

    @Test
    void task_cannot_access_organisation_internal() {
        noClasses().that().resideInAnyPackage(TASK_PACKAGE)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(ORGANISATION_INTERNAL)
                .check(importedClasses);
    }

   @Test
   void user_cannot_access_organisation_internal(){
        noClasses().that().resideInAnyPackage(USER_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(ORGANISATION_INTERNAL)
                .check(importedClasses);
   }

     /**
      * PROJECT.internal  - may not be accessed by (any other module)
     * organisation_cannot_access_project_internal
     * tasks_cannot_access_project_internal
     * user_cannot_access_project_internal
     * */

     @Test
    void organisation_cannot_access_project_internal() {
         noClasses().that().resideInAnyPackage(ORGANISATION_PACKAGE)
                 .should().dependOnClassesThat()
                 .resideInAnyPackage(PROJECT_INTERNAL)
                 .check(importedClasses);
     }

     @Test
    void task_cannot_access_project_internal(){
         noClasses().that().resideInAnyPackage(TASK_PACKAGE)
                 .should().dependOnClassesThat()
                 .resideInAnyPackage(PROJECT_INTERNAL)
                 .check(importedClasses);
     }

     @Test
    void user_cannot_access_project_internal(){
         noClasses().that().resideInAnyPackage(USER_PACKAGE)
                 .should().dependOnClassesThat()
                 .resideInAnyPackage(PROJECT_INTERNAL)
                 .check(importedClasses);
     }

    /**
     * TASK.internal  - may not be accessed by (any other module)
     * organisation_cannot_access_task_internal
     * project_cannot_access_task_internal
     * user_cannot_access_task_internal
     * */

    @Test
    void organisation_cannot_access_task_internal(){
        noClasses().that().resideInAnyPackage(ORGANISATION_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(TASK_INTERNAL)
                .check(importedClasses);
    }

    @Test
    void project_cannot_access_task_internal(){
        noClasses().that().resideInAnyPackage(PROJECT_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(TASK_INTERNAL)
                .check(importedClasses);
    }

    @Test
    void user_cannot_access_task_internal(){
        noClasses().that().resideInAnyPackage(USER_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(TASK_INTERNAL)
                .check(importedClasses);
    }

    /**
     * USER.internal  - may not be accessed by (any other module)
     * organisation_cannot_access_task_internal
     * project_cannot_access_task_internal
     * task_cannot_access_task_internal
     * */

    @Test
    void organisation_cannot_access_user_internal(){
        noClasses().that().resideInAnyPackage(ORGANISATION_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(USER_INTERNAL)
                .check(importedClasses);
    }

    @Test
    void project_cannot_access_user_internal(){
        noClasses().that().resideInAnyPackage(PROJECT_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(USER_INTERNAL)
                .check(importedClasses);
    }


    @Test
    void task_cannot_access_user_internal(){
        noClasses().that().resideInAnyPackage(TASK_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(USER_INTERNAL)
                .check(importedClasses);
    }

}

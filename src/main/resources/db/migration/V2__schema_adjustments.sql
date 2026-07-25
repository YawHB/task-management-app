CREATE TABLE PROJECT_USERS
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    users_id    UUID NOT NULL,
    projects_id UUID NOT NULL,
    CONSTRAINT fk_project_users_id FOREIGN KEY (users_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT fk_projects_id FOREIGN KEY (projects_id) REFERENCES PROJECTS (id) ON DELETE CASCADE
);

CREATE TABLE ORGANISATIONS_USERS
(
    id               UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    users_id         UUID      NOT NULL,
    organisations_id UUID      NOT NULL,
    active_since     timestamp NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_organisation_users_id FOREIGN KEY (users_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT fk_organisations_id FOREIGN KEY (organisations_id) REFERENCES ORGANISATIONS (id) ON DELETE CASCADE
);

CREATE TABLE LABELS
(
    id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR(100) NOT NULL,
    color VARCHAR(7)   NOT NULL
);

CREATE TABLE TASKS
(
    id              UUID UNIQUE PRIMARY KEY,
    parent_task_id  UUID,
    project_id      UUID         NOT NULL,
    assignee_id     UUID,
    task_creator_id UUID         NOT NULL,
    status          VARCHAR(50)  NOT NULL,
    priority        VARCHAR(50)  NOT NULL,
    task_title      VARCHAR(255) NOT NULL,
    description     VARCHAR(100) NOT NULL,
    due_date        TIMESTAMP,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP,
    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES PROJECTS (id) ON DELETE CASCADE,
    CONSTRAINT fk_parent_task_id FOREIGN KEY (parent_task_id) REFERENCES TASKS (id) ON DELETE CASCADE,
    CONSTRAINT fk_assignee_id FOREIGN KEY (assignee_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_creator_id FOREIGN KEY (task_creator_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE TASK_LABELS
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tasks_id  UUID NOT NULL,
    labels_id UUID NOT NULL,
    CONSTRAINT fk_tasks_id FOREIGN KEY (tasks_id) REFERENCES TASKS (id) ON DELETE CASCADE,
    CONSTRAINT fk_labels_id FOREIGN KEY (labels_id) REFERENCES LABELS (id) ON DELETE CASCADE
);

CREATE TABLE TASK_COMMENTS
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id         UUID NOT NULL,
    task_id           UUID NOT NULL,
    comment           TEXT,
    parent_comment_id UUID,
    created_at        TIMESTAMP        DEFAULT NOW(),
    updated_at        TIMESTAMP,
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES TASKS (id) ON DELETE CASCADE,
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES USERS (id) ON DELETE CASCADE,
    CONSTRAINT fk_parent_comment_id FOREIGN KEY (parent_comment_id) REFERENCES TASK_COMMENTS (id) ON DELETE CASCADE
);
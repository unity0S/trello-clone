package io.molnarsandor.trelloclone.util;

import static io.molnarsandor.trelloclone.util.PathsConstants.*;

public final class Paths {

    public static final class Collaborator {

        public static final String PATH = COLLABORATOR;

        public static final class AddCollaboratorToProject {

            public static final String PATH = PROJECT_IDENTIFIER;

            private AddCollaboratorToProject() {}
        }

        public static final class DeleteCollaborator {

            public static final String PATH = PROJECT_IDENTIFIER + COLLABORATOR_SEQUENCE;

            private DeleteCollaborator() {}
        }

        private Collaborator() {}
    }

    public static final class Project {

        public static final String PATH = PROJECT;

        public static final class CreateNewProject {

            public static final String PATH = ROOT;

            private CreateNewProject() {}
        }

        public static final class GetProjectById {

            public static final String PATH = PROJECT_ID;

            private GetProjectById() {}
        }

        public static final class GetAllProjects {

            public static final String PATH = ALL;

            private GetAllProjects() {}
        }

        public static final class DeleteProject {

            public static final String PATH = PROJECT_ID;

            private DeleteProject() {}
        }

        private Project() {}
    }

    public static final class Backlog {

        public static final String PATH = BACKLOG;

        public static final class AddProjectTaskToBacklog {

            public static final String PATH = BACKLOG_ID;

            private AddProjectTaskToBacklog() {}
        }

        public static final class GetProjectBacklog {

            public static final String PATH = BACKLOG_ID;

            private GetProjectBacklog() {}
        }

        public static final class GetProjectTask {

            public static final String PATH = BACKLOG_ID + PROJECT_SEQUENCE;

            private GetProjectTask() {}
        }

        public static final class UpdateProjectTask {

            public static final String PATH =BACKLOG_ID + PROJECT_SEQUENCE;

            private UpdateProjectTask() {}
        }
        public static final class DeleteProjectTask {

            public static final String PATH = BACKLOG_ID + PROJECT_SEQUENCE;

            private DeleteProjectTask() {}
        }

        private Backlog() {}

    }

    public static final class User {

        public static final String PATH = USER;

        public static final class AuthenticateUser {

            public static final String PATH = LOGIN;

            private AuthenticateUser() {}
        }
        public static final class RegisterUser {

            public static final String PATH = REGISTER;

            private RegisterUser() {}
        }
        public static final class ActivateUser {

            public static final String PATH = ACTIVATION;

            private ActivateUser() {}
        }

        private User() {}
    }

    private Paths() {}
}

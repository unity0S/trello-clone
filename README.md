# Trello Clone Reference Project

## About
[#java](https://www.oracle.com/java/technologies/) [#spring-boot-2](https://spring.io/) [#spring-rest](https://spring.io/guides/gs/rest-service/) [#hibernate](https://hibernate.org/) [#spring-data-jpa](https://spring.io/projects/spring-data-jpa)
[#mysql](https://www.mysql.com/)

This project is about to represent my knowledge of these technologies.
The application itself is a simplified version of Trello with the basic features, such as:
- Registering a user with email verification
- Creating a project
- Adding tasks to a project
- Invite collaboratorEntities to a project

You can find the documentation [here](https://trello-clone-ms.herokuapp.com/swagger-ui.html) and you can check out and test the endpoints there and see the models. The application is deployed on Heroku, so you have to wait a few seconds for Heroku to wake up.

### IMPORTANT

For registering a user please use a valid email address, the application sends you an activation link, which you have to visit.
If you don't want to register use :

- username: test@test.com
- password: password

Please use the login endpoint with the above provided credentials,
then copy the token with the "Bearer" tag and paste it in the authentication pop up window, which you can reach by clicking the green Authorize button.
This token is valid for an hour.

From here on you can:
1. Add/Update/Delete a Project
2. Add/Update/Delete a Project Task to/from a Project
3. Add/Delete a Collaborator to/from a Project

## How this Application works

### Registering a new User
After sending a registration you will recieve an email with an activation link, which you have to visit to activate your new account, then you can log in.
If you miss this step you are not able to log in.

## Login
After Activation, you can log in with your email address and password. The backend sends back a Bearer token, which is valid for an hour and is saved to your browsers local storage. It's needed for further authentication and authorization for every endpoint, **except**:
- /login
- /register
- /activation/{token}
- /swagger-ui.html

## Project
You can use the project endpoints to create, update or delete a project. When you CREATE a project you became a project leader/owner.
Upon deletion project tasks and the added collaborators will be deleted too.

## Project Task
You can add Ptoject Tasks to an exising and only existing Project. Create, update, delete with the corresponding endpoints. Project Leaders and Collaborators can manipulate project tasks.

## Collaborators
You can add Collaborators to an existing Project. This Collaborator can be an existing user or a non-existing user as well. They will receive an email that states they have been added to specified Project. If you are already a User, you have nothing to do, once you Log in you will see the Project. If you are not yet registered, you need to register and use the email address which received the system notification about the invite.

## Project Leaders vs Collaborators
When you create a new Project you automatically became the Project Leader.
When you have been invited to a Project you became a Collaborator to that Project, while you can also have your own Projects where you are a Project Leader. 

Project Leaders can:
- Update/Delete a Project where they are the Project Leader
- Add/Remove Collaborators from a Project where they are the Project Leader

Collaborators can:
- Create/Update/Delete Project tasks


## TODO LIST
- [x] Updating Github READ.ME
- [x] Adding error codes to documentation
- [x] Refractor variable names
- [x] Check thrown Exceptions and fix them
- [x] Check field based Dependencies and change it to Constructor based Dep.
- [x] Add and use DTO's
- [x] Feed env variables from outside, not from application.properties
- [x] Delete other Repo
- [ ] Rework Docker file
- [x] UserValidator review (no changes, checks password and confirm password are the same)
- [ ] ProjectTaskService and the other services: code smell => check them
- [ ] Unit/Integration Tests
- [ ] Add interfaces to services
- [ ] Rework Collaborator entity
- [x] Rework exception responses to a global/unified response
- [ ] Rework DTO-s to request/response classes
- [x] Extract endpoints paths to Paths.class
- [ ] Create email message templates
- [ ] Add logging to controller and service layer
- [ ] Read about Clean and Hexagonal architecture
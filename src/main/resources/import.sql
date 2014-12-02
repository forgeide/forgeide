insert into ProjectTemplate (id, code, name, description, archetypeGroupId, archetypeArtifactId, archetypeVersion) values (1, 'javaee', 'Java EE App', 'Java EE application based on HTML5 and REST', 'org.jboss.tools.archetypes', 'jboss-forge-html5', '1.0.0-SNAPSHOT');

insert into TemplateService (id, PROJECT_TEMPLATE_ID, code, name, description, forgeCommand, steps) values (1, 1, 'jpa', 'JPA', 'Installs JPA (Java Persistence API), allowing your project to interact with a database.', 'JPA: Setup', 1);

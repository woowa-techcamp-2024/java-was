package codesquad.webserver.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleTemplateEngineTest {

    private SimpleTemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        templateEngine = new SimpleTemplateEngine();
    }

    @Test
    @DisplayName("변수를 올바르게 치환한다")
    void testProcessVariables() {
        String template = "Hello, {{name}}!";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "World");

        String rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("Hello, World!");
    }

    @Test
    @DisplayName("중첩된 변수를 올바르게 치환한다")
    void testProcessNestedVariables() {
        String template = "User: {{user.name}}, Email: {{user.email}}";
        Map<String, Object> model = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");
        model.put("user", user);

        String rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("User: John Doe, Email: john.doe@example.com");
    }

    @Test
    @DisplayName("if 조건을 올바르게 처리한다")
    void testProcessIfStatements() {
        String template = "{{#if isLoggedIn}}Welcome, {{username}}!{{else}}Please log in.{{/if}}";
        Map<String, Object> model = new HashMap<>();
        model.put("isLoggedIn", true);
        model.put("username", "John Doe");

        String rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("Welcome, John Doe!");

        model.put("isLoggedIn", false);
        rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("Please log in.");
    }

    @Test
    @DisplayName("for 루프를 올바르게 처리한다")
    void testProcessForStatements() {
        String template = "{{#for user in users}}<p>{{user.name}} - {{user.email}}</p>{{/for}}";
        Map<String, Object> model = new HashMap<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "John Doe");
        user1.put("email", "john.doe@example.com");
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Jane Doe");
        user2.put("email", "jane.doe@example.com");
        model.put("users", List.of(user1, user2));

        String rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("<p>John Doe - john.doe@example.com</p><p>Jane Doe - jane.doe@example.com</p>");
    }

    @Test
    @DisplayName("객체의 필드를 올바르게 처리한다")
    void testProcessObjectFields() {
        String template = "Name: {{user.name}}, Email: {{user.email}}";
        User user = new User("John Doe", "john.doe@example.com");
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);

        String rendered = templateEngine.render(template, model);
        assertThat(rendered).isEqualTo("Name: John Doe, Email: john.doe@example.com");
    }

    static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}

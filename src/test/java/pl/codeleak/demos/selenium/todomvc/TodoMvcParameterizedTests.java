package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.seljup.SingleSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SeleniumJupiter.class)
@SingleSession
@DisplayName("Managing Todos - Parameterized")
class TodoMvcParameterizedTests {

    private final WebDriver driver;
    private TodoMvc todoMvc;

    public TodoMvcParameterizedTests(WebDriver driver) {
        this.driver = driver;
        this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
    }

    @BeforeEach
    void navigateToPage() {
        this.todoMvc.navigateTo();
    }

    @AfterEach
    void storageCleanup() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/todos.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Creates Todo with given name")
    void createsTodo(String todo) {
        todoMvc.createTodo(todo);
        assertSingleTodoShown(todo);
    }

    @ParameterizedTest(name = "{index} - {0}, done = {1}")
    @CsvFileSource(resources = "/todos.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Creates and optionally removes Todo with given name")
    void createsAndRemovesTodo(String todo, boolean done) {

        todoMvc.createTodo(todo);
        assertSingleTodoShown(todo);

        todoMvc.showActive();
        assertSingleTodoShown(todo);

        if (done) {
            todoMvc.completeTodo(todo);
            assertNoTodoShown(todo);

            todoMvc.showCompleted();
            assertSingleTodoShown(todo);
        }

        todoMvc.removeTodo(todo);
        assertNoTodoShown(todo);
    }

    private void assertSingleTodoShown(String todo) {
        assertAll(
                () -> assertThat(todoMvc.getTodoCount()).isOne(),
                () -> assertThat(todoMvc.todoExists(todo)).isTrue()
        );
    }

    private void assertNoTodoShown(String todo) {
        assertAll(
                () -> assertThat(todoMvc.getTodoCount()).isZero(),
                () -> assertThat(todoMvc.todoExists(todo)).isFalse()
        );
    }
}

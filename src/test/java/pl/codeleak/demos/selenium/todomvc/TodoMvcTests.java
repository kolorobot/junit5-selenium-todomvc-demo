package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumExtension;
import io.github.bonigarcia.seljup.SingleSession;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SeleniumExtension.class)
@SingleSession
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Managing Todos")
class TodoMvcTests {

    private final WebDriver driver;
    private TodoMvc todoMvc;

    private final String buyTheMilk = "Buy the milk";
    private final String cleanupTheRoom = "Clean up the room";
    private final String readTheBook = "Read the book";

    public TodoMvcTests(WebDriver driver) {
        this.driver = driver;
        this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
        this.todoMvc.navigateTo();
    }

    @AfterEach
    void storageCleanup() {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
    }

    @Test
    @Order(1)
    @DisplayName("Creates Todo with given name")
    void createsTodo() {
        // act
        todoMvc.createTodo(buyTheMilk);
        // assert
        assertAll(
                () -> assertThat(todoMvc.getTodosLeft()).isOne(),
                () -> assertThat(todoMvc.todoExists(buyTheMilk)).isTrue()
        );
    }

    @Test
    @Order(2)
    @DisplayName("Creates Todos all with the same name")
    void createsTodosWithSameName() {
        // act
        todoMvc.createTodos(buyTheMilk, buyTheMilk, buyTheMilk);
        // assert
        assertThat(todoMvc.getTodosLeft()).isEqualTo(3);

        // act
        todoMvc.showActive();
        // assert
        assertThat(todoMvc.getTodoCount()).isEqualTo(3);
    }

    @Test
    @Order(3)
    @DisplayName("Edits inline double-clicked Todo")
    void editsTodo() {
        // arrange
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom);
        // act
        todoMvc.renameTodo(buyTheMilk, readTheBook);
        // assert
        assertAll(
                () -> assertThat(todoMvc.todoExists(buyTheMilk)).isFalse(),
                () -> assertThat(todoMvc.todoExists(readTheBook)).isTrue(),
                () -> assertThat(todoMvc.todoExists(cleanupTheRoom)).isTrue()
        );
    }

    @Test
    @Order(4)
    @DisplayName("Removes selected Todo")
    void removesTodo() {
        // arrange
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);
        // act
        todoMvc.removeTodo(buyTheMilk);
        // assert
        assertAll(
                () -> assertThat(todoMvc.todoExists(buyTheMilk)).isFalse(),
                () -> assertThat(todoMvc.todoExists(cleanupTheRoom)).isTrue(),
                () -> assertThat(todoMvc.todoExists(readTheBook)).isTrue()
        );
    }

    @Test
    @Order(5)
    @DisplayName("Toggles selected Todo as completed")
    void togglesTodoCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);

        todoMvc.completeTodo(buyTheMilk);
        assertThat(todoMvc.getTodosLeft()).isEqualTo(2);

        todoMvc.showCompleted();
        assertThat(todoMvc.getTodoCount()).isOne();

        todoMvc.showActive();
        assertThat(todoMvc.getTodoCount()).isEqualTo(2);
    }

    @Test
    @Order(6)
    @DisplayName("Toggles all Todos as completed")
    void togglesAllTodosCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);

        assertThat(todoMvc.getTodos())
                .hasSize(3)
                .containsSequence(buyTheMilk, cleanupTheRoom, readTheBook);

        todoMvc.completeAllTodos();
        assertThat(todoMvc.getTodosLeft()).isZero();

        todoMvc.showCompleted();
        assertThat(todoMvc.getTodoCount()).isEqualTo(3);

        todoMvc.showActive();
        assertThat(todoMvc.getTodoCount()).isZero();
    }

    @Test
    @Order(7)
    @DisplayName("Clears all completed Todos")
    void clearsCompletedTodos() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom);

        assertThat(todoMvc.getTodos())
                .hasSize(2)
                .containsSequence(buyTheMilk, cleanupTheRoom);

        todoMvc.completeAllTodos();
        todoMvc.createTodo(readTheBook);

        todoMvc.clearCompleted();
        assertThat(todoMvc.getTodosLeft()).isOne();

        todoMvc.showCompleted();
        assertThat(todoMvc.getTodoCount()).isZero();

        todoMvc.showActive();
        assertThat(todoMvc.getTodoCount()).isOne();
    }
}

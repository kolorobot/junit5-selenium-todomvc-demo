package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumExtension;
import io.github.bonigarcia.seljup.SingleSession;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertEquals(1, todoMvc.getTodosLeft()),
                () -> assertTrue(todoMvc.todoExists(buyTheMilk))
        );
    }

    @Test
    @Order(2)
    @DisplayName("Creates Todos all with the same name")
    void createsTodosWithSameName() {
        // act
        todoMvc.createTodos(buyTheMilk, buyTheMilk, buyTheMilk);
        // assert
        assertEquals(3, todoMvc.getTodosLeft());

        // act
        todoMvc.showActive();
        // assert
        assertEquals(3, todoMvc.getTodoCount());
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
                () -> assertFalse(todoMvc.todoExists(buyTheMilk)),
                () -> assertTrue(todoMvc.todoExists(readTheBook)),
                () -> assertTrue(todoMvc.todoExists(cleanupTheRoom))
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
                () -> assertFalse(todoMvc.todoExists(buyTheMilk)),
                () -> assertTrue(todoMvc.todoExists(cleanupTheRoom)),
                () -> assertTrue(todoMvc.todoExists(readTheBook))
        );
    }

    @Test
    @Order(5)
    @DisplayName("Toggles selected Todo as completed")
    void togglesTodoCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);

        todoMvc.completeTodo(buyTheMilk);
        assertEquals(2, todoMvc.getTodosLeft());

        todoMvc.showCompleted();
        assertEquals(1, todoMvc.getTodoCount());

        todoMvc.showActive();
        assertEquals(2, todoMvc.getTodoCount());
    }

    @Test
    @Order(6)
    @DisplayName("Toggles all Todos as completed")
    void togglesAllTodosCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);

        todoMvc.completeAllTodos();
        assertEquals(0, todoMvc.getTodosLeft());

        todoMvc.showCompleted();
        assertEquals(3, todoMvc.getTodoCount());

        todoMvc.showActive();
        assertEquals(0, todoMvc.getTodoCount());
    }

    @Test
    @Order(7)
    @DisplayName("Clears all completed Todos")
    void clearsCompletedTodos() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom);
        todoMvc.completeAllTodos();
        todoMvc.createTodo(readTheBook);

        todoMvc.clearCompleted();
        assertEquals(1, todoMvc.getTodosLeft());

        todoMvc.showCompleted();
        assertEquals(0, todoMvc.getTodoCount());

        todoMvc.showActive();
        assertEquals(1, todoMvc.getTodoCount());
    }
}

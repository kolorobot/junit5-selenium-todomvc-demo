package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SeleniumExtension.class)
@DisplayName("Managing Todos")
class TodoMvcTests {

    private TodoMvc todoMvc;

    private final String buyTheMilk = "Buy the milk";
    private final String cleanupTheRoom = "Clean up the room";
    private final String readTheBook = "Read the book";

    @BeforeEach
    void beforeEach(ChromeDriver driver) {
        this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
        this.todoMvc.navigateTo();
    }

    @Test
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

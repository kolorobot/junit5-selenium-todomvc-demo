package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
class SeleniumTest {

    @Test
    void projectIsConfigured(ChromeDriver driver) {
        assertThat(driver).isNotNull();
    }
}

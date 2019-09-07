package pl.codeleak.demos.selenium.todomvc;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;

@ExtendWith(SeleniumExtension.class)
class SeleniumTest {

    @Test
    void projectIsConfigured(ChromeDriver driver) {}
}
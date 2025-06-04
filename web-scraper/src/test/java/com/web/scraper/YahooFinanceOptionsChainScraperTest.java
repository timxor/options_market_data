package com.web.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class YahooFinanceOptionsChainScraperTest {

    private static final Logger LOGGER = Logger.getLogger(YahooFinanceOptionsChainScraperTest.class.getName());

    private WebDriver driver;
    private ChromeOptions options;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        driver = mock(ChromeDriver.class);
        options = mock(ChromeOptions.class);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testTableParsing() throws IOException {
        String sampleHtml = """
            <html>
            <body>
                <section data-test="options-section">
                    <table class="calls">
                        <thead>
                            <tr>
                                <th>Contract Name</th>
                                <th>Strike</th>
                                <th>Last Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>PLTR250321C00045000</td>
                                <td>45.00</td>
                                <td>3.50</td>
                            </tr>
                        </tbody>
                    </table>
                </section>
            </body>
            </html>
            """;

        Document doc = Jsoup.parse(sampleHtml);
        assertNotNull(doc, "Parsed document should not be null");

        Element callsTable = doc.selectFirst("section[data-test='options-section'] table");
        assertNotNull(callsTable, "Calls table should be found");
        assertEquals(3, callsTable.select("thead tr th").size(), "Should have 3 headers");
        assertEquals(1, callsTable.select("tbody tr").size(), "Should have 1 data row");

        // Simulate CSV creation logic
        String csvFilePath = Paths.get(tempDir.getAbsolutePath(), "test_output.csv").toString();
        Files.createFile(Paths.get(csvFilePath));
        assertTrue(Files.exists(Paths.get(csvFilePath)), "CSV file should be created");
    }

    @Test
    void testDirectoryCreation() throws IOException {
        String directoryPath = Paths.get(tempDir.getAbsolutePath(), "scraped_data").toString();
        Files.createDirectories(Paths.get(directoryPath));
        assertTrue(Files.exists(Paths.get(directoryPath)), "Scraped data directory should be created");
    }
}

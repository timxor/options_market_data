package com.web.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class YahooFinanceOptionsChainScraper {

    private static final Logger LOGGER = Logger.getLogger(YahooFinanceOptionsChainScraper.class.getName());

    public static void main(String[] args) {
        String baseUrl = "https://finance.yahoo.com/quote/";
        String palantirTickerSymbol = "PLTR";
        String postfixUrl = "/options/";
        String url = baseUrl + palantirTickerSymbol + postfixUrl;
        String chromeDriverPath = "/usr/local/bin/chromedriver";
        String directoryPath = Paths.get(System.getProperty("user.dir"), "scraped_data").toString();

        WebDriver driver = null; // Declare outside try block for finally access


        try {
            // Configure logging
            LOGGER.setLevel(Level.INFO);
            LOGGER.info("Starting Yahoo Finance Options Chain Scraper");

            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(directoryPath));

            // Create filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String csvFileName = "palantir_yahoo_options_chain_" + timestamp + ".csv";
            String csvFilePath = Paths.get(directoryPath, csvFileName).toString();

            // Set up Selenium WebDriver with enhanced configuration
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("detach", false);  // Ensures Chrome closes
            options.addArguments("--headless"); // Run in headless mode (no GUI)
            options.addArguments("--disable-gpu"); // Recommended for headless
            options.addArguments("--no-sandbox"); // Bypass OS security model
            options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
            options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--remote-allow-origins=*"); // Allow any origin
            options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.89 Safari/537.36");

            // Ignore the CDP version warnings - this is key
            System.setProperty("webdriver.chrome.silentOutput", "true");
            java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

            LOGGER.info("Initializing ChromeDriver");
            driver = new ChromeDriver(options);




            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Open the page
            LOGGER.info("Opening URL: " + url);
            driver.get(url);

            // Wait for page to load
            Thread.sleep(3000); // Give the page a moment to load fully

            // Take screenshot for debugging (optional)
            // File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            // Files.copy(screenshot.toPath(), Paths.get(directoryPath, "screenshot_" + timestamp + ".png"));

            // Get the page source and parse with Jsoup
            LOGGER.info("Retrieving page source");
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            // Enhanced debugging
            LOGGER.info("Document title: " + doc.title());
            LOGGER.info("Total tables found: " + doc.select("table").size());
            LOGGER.info("Page contains 'Contract Name': " + doc.text().contains("Contract Name"));
            LOGGER.info("Page contains 'Calls': " + doc.text().contains("Calls"));

            // Try multiple strategies to find the options table
            Element callsTable = null;

            // Strategy 1: Find by section data attribute
            Element optionsSection = doc.selectFirst("section[data-test='options-section']");
            if (optionsSection != null) {
                LOGGER.info("Found options section with data-test attribute");
                callsTable = optionsSection.select("table").first();
            }

            // Strategy 2: Find by specific table class or pattern
            if (callsTable == null) {
                LOGGER.info("Trying to find table by class patterns");
                callsTable = doc.select("table.calls, div.calls table, div[data-test='calls'] table").first();
            }

            // Strategy 3: Find by examining table content
            if (callsTable == null) {
                LOGGER.info("Examining all tables for calls content");
                Elements tables = doc.select("table");
                for (Element table : tables) {
                    String tableText = table.text();
                    LOGGER.info("Table text: " + tableText.substring(0, Math.min(tableText.length(), 100)) + "...");
                    if (tableText.contains("Contract Name") ||
                        tableText.contains("Call") ||
                        tableText.contains("Strike") && tableText.contains("Volume")) {
                        callsTable = table;
                        LOGGER.info("Found potential calls table by content");
                        break;
                    }
                }
            }

            // Strategy 4: Just take the first table if all else fails
            if (callsTable == null && !doc.select("table").isEmpty()) {
                LOGGER.warning("Using fallback: selecting first table found");
                callsTable = doc.select("table").first();
            }

            if (callsTable == null) {
                LOGGER.severe("No tables found in the document. Check if page loaded correctly.");
                // Try direct WebDriver element access as last resort
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement tableElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("table")
                    ));
                    if (tableElement != null) {
                        LOGGER.info("Found table directly via WebDriver");
                        // Extract HTML from WebElement and parse with Jsoup
                        callsTable = Jsoup.parse(tableElement.getAttribute("outerHTML")).select("table").first();
                    }
                } catch (Exception e) {
                    LOGGER.severe("Failed to find table via WebDriver: " + e.getMessage());
                    // Save page source for debugging
                    Files.write(Paths.get(directoryPath, "page_source_" + timestamp + ".html"),
                               pageSource.getBytes());
                    LOGGER.info("Page source saved for debugging");
                    return;
                }
            }

            if (callsTable == null) {
                LOGGER.severe("Calls table not found after multiple attempts. Check the HTML structure.");
                // Save page source for debugging
                Files.write(Paths.get(directoryPath, "page_source_" + timestamp + ".html"),
                           pageSource.getBytes());
                LOGGER.info("Page source saved for debugging");
                return;
            }

            // Extract table headers
            List<String> headers = new ArrayList<>();
            Elements headerRows = callsTable.select("thead tr");
            if (headerRows.isEmpty()) {
                // Try to find headers in first row if thead is missing
                Element firstRow = callsTable.select("tr").first();
                if (firstRow != null) {
                    Elements headerCells = firstRow.select("th, td");
                    for (Element headerCell : headerCells) {
                        headers.add(headerCell.text().trim());
                    }
                }
            } else {
                for (Element headerRow : headerRows) {
                    Elements headerCells = headerRow.select("th");
                    for (Element headerCell : headerCells) {
                        headers.add(headerCell.text().trim());
                    }
                }
            }

            if (headers.isEmpty()) {
                LOGGER.warning("No headers found in table. Using default headers.");
                // Add default headers
                headers.add("Contract Name");
                headers.add("Last Trade Date");
                headers.add("Strike");
                headers.add("Last Price");
                headers.add("Bid");
                headers.add("Ask");
                headers.add("Change");
                headers.add("% Change");
                headers.add("Volume");
                headers.add("Open Interest");
            }

            // Extract table rows
            List<String[]> dataRows = new ArrayList<>();
            Elements bodyRows;
            if (callsTable.select("tbody").isEmpty()) {
                // If no tbody, get all rows except first (which is headers)
                Elements allRows = callsTable.select("tr");
                bodyRows = new Elements();
                for (int i = 1; i < allRows.size(); i++) {
                    bodyRows.add(allRows.get(i));
                }
            } else {
                bodyRows = callsTable.select("tbody tr");
            }

            for (Element row : bodyRows) {
                Elements cells = row.select("td");
                if (cells.isEmpty()) {
                    // Try to get any cell if td is not used
                    cells = row.select("td, th");
                }

                if (!cells.isEmpty()) {
                    String[] rowData = new String[Math.max(cells.size(), headers.size())];
                    for (int i = 0; i < cells.size(); i++) {
                        rowData[i] = cells.get(i).text().trim();
                    }
                    dataRows.add(rowData);
                }
            }

            // Write to CSV file
            LOGGER.info("Saving CSV file to: " + csvFilePath);

            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
                writer.writeNext(headers.toArray(new String[0]));
                for (String[] rowData : dataRows) {
                    writer.writeNext(rowData);
                }
                LOGGER.info("Successfully wrote csv file to: " + csvFilePath);
            } catch (IOException e) {
                LOGGER.severe("Error writing CSV file: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            LOGGER.severe("Error with file operations: " + e.getMessage());
            e.printStackTrace();
        } catch (org.openqa.selenium.TimeoutException e) {
            LOGGER.severe("Timeout while loading the page: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                try {

                    LOGGER.info("Closing WebDriver");
                    driver.close();  // Close driver
                    LOGGER.info("Closed WebDriver");


                    LOGGER.info("Sleep for 1000 millis");
                    Thread.sleep(1000);  // Give some time before quitting
                    LOGGER.info("Done sleeping");


                    LOGGER.info("Quitting WebDriver");
                    driver.quit();  // Quit driver
                    Thread.sleep(3000); // Allow time for cleanup
                    LOGGER.info("Done quitting");

                    // Forcefully stop all remaining Selenium threads
                    LOGGER.info("Forcefully stopping lingering threads");
                    System.exit(0);

                } catch (Exception e) {
                    LOGGER.warning("Error during driver shutdown: " + e.getMessage());
                }
            }
        }
    }
}

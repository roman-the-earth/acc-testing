package helpers;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import enums.SupportedBrowsers;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebHelper {
    private static final Logger logger = Logger.getLogger("WebHelper");
    private static final String baseUrl = "https://www.trademe.co.nz/a/motors";
    private static final Duration timeoutInMillis = Duration.ofMillis(5000);

    public static WebDriver getWebDriver(SupportedBrowsers browser){
        WebDriver driver;
        switch (browser) {
            case IE -> {
                System.setProperty("webdriver.ie.driver", browser.path);
                driver = new InternetExplorerDriver();
            }
            case Edge -> {
                EdgeOptions options = new EdgeOptions();
                options.setBinary(browser.path);
                driver = new EdgeDriver(options);
            }
            case Firefox -> {
                System.setProperty("webdriver.gecko.driver", browser.path);
                driver = new FirefoxDriver();
            }
            default -> {
                System.setProperty("webdriver.chrome.driver", browser.path);
                driver = new ChromeDriver();
            }
        }
        Capabilities cap = ((RemoteWebDriver)driver).getCapabilities();
        logger.info(String.format("created WebDriver for: %s", cap.getBrowserName()));
        return driver;
    }

    public static Map<String, List<String>> getAvailableMakes(WebDriver wd) {
        logger.info(String.format("navigating to: %s", baseUrl));
        wd.get(baseUrl);
        logger.info(String.format("finding 'selectedMake' dropdown element"));
        WebElement makeSelector = wd.findElement(By.name("selectedMake"));
        logger.info(String.format("reading options in dropdown"));
        List<WebElement> options = makeSelector.findElements(By.tagName("option"));
        List<String> availableMakes = new ArrayList<>();
        List<String> nonMakesFound = new ArrayList<>();
        String nonMakeValues = "(Other|Any make)";
        options.forEach(option -> {
            String currentMake = option.getText();
            if (Pattern.matches(nonMakeValues, currentMake)) {
                nonMakesFound.add(currentMake);
            } else {
                availableMakes.add(currentMake);
            }
        });

        Map<String, List<String>> retVal = new HashMap<>();
        retVal.put("makes", availableMakes);
        retVal.put("nonMakes", nonMakesFound);
        return retVal;
    }

    public static Map<String, Integer> getCarsByMake(WebDriver wd, Set<String> makesToQuery) {
        Map<String, Integer> retVal = new HashMap<>();
        makesToQuery.forEach( make -> {
            wd.get(baseUrl);
            logger.info("finding 'selectedMake' dropdown element");
            WebElement makeSelector = wd.findElement(By.name("selectedMake"));
            logger.info(String.format("selecting '%s' from dropdown element", make));
            Select makeSelect = new Select(makeSelector);
            makeSelect.selectByVisibleText(make);
            logger.info("finding 'Search' button element");
            WebElement searchBtn = wd.findElement(By.cssSelector(".tm-motors-search-bar__keyword-button--wide.o-button2--primary.o-button2.o-button2--full-width.o-button2--compact"));
            logger.info("clicking 'Search' button element");
            searchBtn.click();
            logger.info(String.format("waiting for search response (up to %d millis)", timeoutInMillis.toMillis()));
            WebDriverWait wait = new WebDriverWait(wd, timeoutInMillis);
            wait.until(visibilityOfElementLocated(By.cssSelector(".tm-search-header-result-count__heading.ng-star-inserted")));
            WebElement resultElement = wd.findElement(By.cssSelector(".tm-search-header-result-count__heading.ng-star-inserted"));
            String resultElemText = resultElement.getText();
            logger.info(String.format("returned count of %s",resultElemText));
            Pattern p = Pattern.compile("[0-9,]+");
            logger.info("Parsing integer from textual result");
            Matcher m = p.matcher(resultElemText);
            int resultCount = 0;
            if(m.find()){
                resultCount = Integer.parseInt(m.group().replace(",",""));
            }
            retVal.put(make,resultCount);
        });
        return retVal;
    }
}

package ui;

import enums.SupportedBrowsers;
import helpers.WebHelper;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.logs.Log;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class TestMotorsPage {

    private final Logger logger = Logger.getLogger("TestMotorsPage");
    private final SoftAssert softAssert = new SoftAssert();

    @DataProvider(name = "BackendMakeData")
    public Object[][] availableMakeData() {
        JSONParser parser = new JSONParser();
        List<String> makes = new ArrayList<>();
        List<String> nonSpecificOptions = new ArrayList<>();
        try {
            String dataFile = new File("").getAbsolutePath().concat("/src/test/java/data/AvailableMakes.json");
            Object obj = parser.parse(new FileReader(dataFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray makesJson = (JSONArray) jsonObject.get("makes");
            makesJson.forEach(make -> makes.add(make.toString()));
            JSONArray nonSpecificJson = (JSONArray) jsonObject.get("non-specific");
            nonSpecificJson.forEach(nsOption -> nonSpecificOptions.add(nsOption.toString()));
        } catch (Exception e) {
            Log.error(e.getStackTrace().toString());
        }
        return new Object[][]{{makes, nonSpecificOptions}};
    }

    @DataProvider(name = "BackendDataByMake")
    public Object[][] availableCarsByMake() {
        JSONParser parser = new JSONParser();
        SortedMap<String, Integer> carsByMake = new TreeMap<>();
        try {
            String dataFile = new File("").getAbsolutePath().concat("/src/test/java/data/CarsByMake.json");
            JSONObject obj = (JSONObject) parser.parse(new FileReader(dataFile));
            obj.keySet().forEach(key -> {
                Integer dbCount = ((Long) obj.get(key)).intValue();
                carsByMake.put(key.toString(), dbCount);
            });
        } catch (Exception e) {
            Log.error(e.getStackTrace().toString());
        }
        return new Object[][]{{carsByMake}};
    }

    @Test(dataProvider = "BackendMakeData")
    public void VerifyAvailableMakesCount(List<String> dbMakeList, List<String> dbNonMakeOptions) {
        // verify that the webUI returns the same number of makes
        // as found in the backend db

        // get list from WebUI
        logger.info("Starting 'VerifyAvailableMakesCount' test");
        WebDriver driver = WebHelper.getWebDriver(SupportedBrowsers.Chrome);
        Map<String, List<String>> uiMakeData = WebHelper.getAvailableMakes(driver);

        List<String> uiMakes = uiMakeData.get("makes");
        List<String> uiNonMakes = uiMakeData.get("nonMakes");
        logger.info(String.format("Found %d available makes", uiMakes.size()));
        logger.info(String.format("Found %d available non-makes", uiNonMakes.size()));

        String message = String.format("UI available makes (%d) do not match count in db (%d).",
                uiMakes.size(), dbMakeList.size());
        Assert.assertEquals(uiMakes.size(), dbMakeList.size(), message);

        message = String.format("UI available non-makes (%d) do not match count in db (%d).",
                uiNonMakes.size(), dbNonMakeOptions.size());
        Assert.assertEquals(uiNonMakes.size(), dbNonMakeOptions.size(), message);
    }

    @Test(dataProvider = "BackendDataByMake")
    public void VerifyAvailableCountByMake(SortedMap<String, Integer> dbCarsByMake) {
        logger.info("Starting 'VerifyAvailableCountByMake' test");
        // verify that the webUI returns the same number for a given make
        // as found in the backend db

        // get count of cars by make from WebUI
        WebDriver driver = WebHelper.getWebDriver(SupportedBrowsers.Chrome);
        Map<String, Integer> uiMakeData = WebHelper.getCarsByMake(driver, dbCarsByMake.keySet());
        logger.info(String.format("Found %d available makes", uiMakeData.size()));

        // reconcile expected db counts against UI counts
        dbCarsByMake.keySet().forEach(dbKey -> {
            Integer dbCount = dbCarsByMake.get(dbKey);
            Integer uiCount = uiMakeData.get(dbKey);
            logger.info(String.format("Found %d cars for make '%s'", uiCount, dbKey));
            String errMsg = String.format("'%s' returns count of %d in UI, when %d found in db.",
                    dbKey, uiCount, dbCount);
            softAssert.assertEquals(uiCount, dbCount, errMsg);
        });
        softAssert.assertAll();
    }
}

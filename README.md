# Overview

In this sample Maven project you find Java/Selenium based automated tests that can used to verify behavior of the TradeMe Motors website.
There are two forms of testing examples:

- Web UI testing
  - TestNG tests located within the [src/test/java/api](./src/test/java/ui)
- Service API testing
  - Karate tests located within [src/test/java/api](./src/test/java/api)

# Prerequisites

* IDE
  * This project was developed in [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows) but any Java-based IDE (e.g. [eclipse](https://www.eclipse.org/downloads/)) should suffice <sup>*</sup>

    <sup><sup>*</sup> additional configuration changes may be needed.</sup>

* External libs
  * Selenium WebDrivers are needed for each flavor of browser to be tested. These drivers are packaged with the project for convenience within [test/resources](./src/test/resources)

# Setup
1. Download the project from the git repo
2. Open project using IntelliJ
3. Build the project within IntelliJ
   - ![Build -> Build Project (Ctrl-F9)](/src/test/resources/readme_artifacts/build_project.PNG)

# Running the Test
After the project has been successfully downloaded and built. You should be able to execute the automated test in a number of ways:

### UI Test (TestNG)
#### Option 1:
- Maven
  - From within the Maven window located to the left of the IDE, right-click on 'test' that is located under the 'Lifecycle' folder.
  - ![select "Run <b>'acc-spike[test]'</b>"](/src/test/resources/readme_artifacts/ui_run_from_idea.PNG)


#### Option 2:
- TestNG.xml
  - Locate the ['testng.xml'](./src/test/resources/testng.xml) file within the project
  - Right-click on the 'testng.xml' file and select 'Run '...\src\...\testng.xml'
  - ![TestNG.xml -> right-click -> Run](/src/test/resources/readme_artifacts/testngxml_startup.PNG)

#### Option 3:
- From IDEA code editor
  - Locate and open the [test class](./src/test/java/ui) within the code editor. Scroll to the test of interest.
  - Next to the test, right-click the green 'play' button and select 'Run'
  - ![Run from editor](/src/test/resources/readme_artifacts/texteditor_startup.PNG)

### API Test (Karate)
#### Option 1:
- From IDEA code editor
  - Locate and open the [test class](./src/test/java/api) within the code editor. Scroll to the test of interest.
  - Next to the test, right-click the green 'play' button and select 'Run'
  - ![Run from editor](/src/test/resources/readme_artifacts/karate_editor_startup.PNG)

#### Option 2:
- From RUN window
  - locate the top container (parent) or individual test name(s) located in the RUN window.
  - Right-click on the item(s) and select 'RUN'
  - ![start from RUN window](/src/test/resources/readme_artifacts/karate_run_individ.PNG)


# Improvements (TODOs)
- [ ] Refactor UI testing into a more common PageObject approach 
  - Separate BaseObject, HomePage (trademe home), MotorsPage and CarsPage objects. 
- [ ] Properly integrate ExtentReport into project to display both Karate/TestNG results
- [ ] Update 'helpers' with proper classes that retrieve data from backend DB ([not flat file](./src/test/java/data)), e.g. CarsDbHelper.java
- [ ] Use configuration files to allow for customized, and further automated execution.
  - This includes creation of groups and suites that can target specific testing, i.e. Check-in tests, functional tests, Perf test, Regression test and specific area testing, etc.
- [ ] Currently, the project doesn't support command line execution of TestNG due to a pathing issue of the .jar file.  This should be resolved and fully supported.
- [ ] Properly hook-in running of automation through a build task that can be accessed via Maven Lifecycle (in-IDE) (easier for devs to execute) and can be consumed by CI pipeline.
- [ ] Further Karate cases should be developed for all API exposed. Project layout should be structured to somewhat reflect the [API reference](https://developer.trademe.co.nz/api-reference) outline as located in the TM dev portal.
- [ ] Need to update to take advantage of multiple browser testing.  Currently, implementation supports it but is currently only running against Chrome.
- [ ] lots more...


# Additional Testcases
### UI
- Motors page
  - functional
    - There are 18 different filters on the Motors page.  Each should have a set of test cases around them.
    - There are 3 additional preset filters (Used,New,Classic).  Each of these only need to verify that selecting them populates the fields with correct search values.  Their functionality (data returned) should already be covered by other filter tests.  For example, the 'Used' filtered will populate the 'Odometer' filter with a 'from' value of 100Km.  Validation that the value is set to the expected 100Km. is all that is needed.  The result of a search using this filter should be covered in an 'Odometer' case where the filter is adjusted and results are reconciled against data found in the backend.
    - 'Keyword' filter will require a bit more consideration around 'how'
      - One approach would be to find validate records returned against test objects that have 'planted' unique keywords -- so only they should be returned in the search results.
    - Verify 'Make' dropdown contains all expected makes currently in backend
      - by count
      - by name (counts can be same, but make 'A' could be missing and make 'B' listed twice, giving same count but there's actually a missing Make)
    - Verify 'Model' select contains all expected (those associated with currently selected make) models currently in backend
      - by count
      - by name
      - Model is dependent on 'Make'. Should default back to ANY when Make is changed.
    - All other filters should be exercised, exploring mins/mids/max values.
    - Only first 8 filters are visible onLoad(). Additional filters shouldn't be exposed.
    - Selecting 'more options' renders the remaining filters visible.
    - Additional tabs ('Motorbikes','Caravans & Motorhomes', 'Boats' etc.) navigate to correct URL (each of those may be their own PageObjects for further verification)
    - Way, way more....
  - Non-functional
    - Performance of page load
    - Performance of search rendering (searching perf can be covered in API testing)
    - Keyword filter exposed potential security threat. 
      - Verify handling of special characters
      - Verify handling of cross-scripting


### API
- UsedCar API
  - functional
    - Verify expected counts, both by count and name.
    - Verify both XML and JSON are supported for GET
    - There are 3 query parameters that should be validated.
      - depth
        - Verify 0 (single category returned)
        - Verify 1 (all categories returned)
        - Verify default behavior when not present
      - region
        - Verify results are filtered based on the region ID provided.
        - Verify that this field is non-functional UNLESS with_counts is present AND true
      - with_counts
        - Verify number of listings in each category is returned when true (Note: this is currently broken on the dev site.  Works in prod)
          - Verify that the number of listing is expected based on that category and reconcile against backend.
          - Verify not counts are provided with field is present but false
          - Verify default behavior (no count field returned)
    - Non-200s responses
      - Verify invalid query parameters do not cause server issues (500)
      - Verify invalid request does not cause 500 (should give proper error message)
  - Non-Functional
    - Verify performance (latency).  There must be a set query associated with the API call.  As the DB grows does the performance slow? When is it unacceptable?
    - Verify rate
      - The documentation states it is not limited. What is the rate that could cause a DoS? Is the service capable of handling requests based on projected user demand?



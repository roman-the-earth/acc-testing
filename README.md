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
- [ ] Properly integrate ExtentReport into project to display both Karate/TestNG results
- [ ] Update 'helpers' with proper classes that retrieve data from backend DB ([not flat file](./src/test/java/data))

<sup>*</sup> additional configuration changes may be needed.
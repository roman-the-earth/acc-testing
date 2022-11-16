Feature: Category method - UsedCars

  Scenario: Retrieve all available car makes
    Given url 'https://api.tmsandbox.co.nz/v1/Categories/UsedCars.json'
    When method GET
    Then status 200
    # remove the 'Other' category from the response (it's not a 'make')
    * def allMakes = $.Subcategories[?(@.Name !='Other')]
    # validate we have the expected number of 'makes'
    * assert allMakes.length == 77


  # this test fails against sandbox --- works against prod site
  Scenario: Retrieve used cars by make (sandbox)
    Given url 'https://api.tmsandbox.co.nz/v1/Categories/UsedCars.json'
    And param with_counts = 'true'
    When method GET
    Then status 200


  # this test fails against sandbox --- works against prod site
  Scenario: Retrieve used cars by make (prod)
    Given url 'https://api.trademe.co.nz/v1/Categories/UsedCars.json'
    And param with_counts = 'true'
    When method GET
    Then status 200
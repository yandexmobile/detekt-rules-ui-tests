# Detekt rules for UI-tests

A set of [Detekt](https://detekt.dev) rules to help prevent common errors in UI-tests.  
[[RU] Detekt: How static analysis helps to improve autotest code](https://habr.com/p/779152)

# Rules description

* TestClassNamingRule:  
A test class name should fit the naming convention;

* TestMethodNamingRule:  
Test method name should not be long and contain unnecessary words;

* TestClassPrivateMemberRule:  
Members of test class must use private modifier;

* IsVisibleUsageRule:  
In general, 'Kaspresso isVisible' should not be used -> use 'isDisplayed';

* LargeScreenObjectRule:  
Split a large ScreenObject into PageElement's and combine them on this SO;

* RestrictedKeywordRule:  
Restricted keyword for test method, ScreenObject and Scenario.

# Installation and configuration

Add detekt rules in your `build.gradle.kts`

```
dependencies {
    detektPlugins("com.yandex.detekt:detekt-rules-ui-tests:0.1.1")
}
```

and then add this configuration section to your `detekt-config.yml` to activate the rules:
```
ui-tests:
  TestClassNamingRule:
    active: true
    includes: "**/androidTest/**"
  TestMethodNamingRule:
    active: true
    unexpectedWords: [ 'test' ]
    maxFullQualifierLength: 135
    includes: "**/androidTest/**"
  TestClassPrivateMemberRule:
    active: false
    baseTestClass: "BaseTestCase"
    includes: "**/androidTest/**"
  IsVisibleUsageRule:
    active: true
    includes: "**/androidTest/**"
  LargeScreenObjectRule:
    active: false
    allowedLinesOfCode: 110
    includes: "**/androidTest/**"
  RestrictedKeywordRule:
    active: true
    includes: "**/androidTest/**"
```

# Contributors
[primechord](https://github.com/primechord/)

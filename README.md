# Detekt rules for UI-tests

A set of [Detekt](https://detekt.dev) rules to help prevent common errors in UI-tests.

# Rules description

[[RU] Detekt: How static analysis helps to improve autotest code](https://habr.com/p/779152)

TestClassNamingRule: 
A test class name should fit the naming convention;

TestMethodNamingRule:
Test method name should not be long and contain unnecessary words;

TestClassPrivateMemberRule: 
Members of test class must use private modifier;

IsVisibleUsageRule:
In general, 'Espresso isVisible' should not be used -> use 'isDisplayed';

LargeScreenObjectRule:
Split a large ScreenObject into PageElement's and combine them on this SO;

RestrictedKeywordRule:
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

# License
```
Copyright 2023 Yandex LLC

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class TestMethodNamingTests {

    private val rule = TestMethodNamingRule(TestConfig(WORDS_PARAMETER_NAME to wordsParameterValue))

    private companion object {
        const val WORDS_PARAMETER_NAME = "unexpectedWords"
        val wordsParameterValue = listOf("test")
    }

    @Test
    fun containsRedundantWord() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomethingTest() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun lengthExceedsParameterValue() {
        val longPackage = ".folder".repeat(15)
        // language="kotlin"
        val case =
            """
            package com.yandex$longPackage
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun mainCaseWithoutViolations() {
        val case =
            """
            package com.yandex.tests
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }

    @Test
    fun consistsOfOneWord() {
        val case =
            """
            package com.yandex.tests
            class SomeTest: BaseTestCase() {
                @Test fun run() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }
}
/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class LargeScreenObjectTests {
    private companion object {
        const val THRESHOLD_PARAMETER_NAME = "allowedLinesOfCode"
        const val THRESHOLD_PARAMETER_VALUE = 6
    }

    private val rule = LargeScreenObjectRule(TestConfig(THRESHOLD_PARAMETER_NAME to THRESHOLD_PARAMETER_VALUE))

    @Test
    fun numberOfLinesNotExceedLimit() {
        // language="kotlin"
        val case =
            """
            object SomeScreen : KScreen<SomeScreen>() {

                override val layoutId = R.layout.fragment_some_screen
                override val viewClass = SomeScreenFragment::class.java

                val subtitle = KButton { withId(R.id.subtitle) }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }

    @Test
    fun numberOfLinesExceedLimit() {
        // language="kotlin"
        val case =
            """
            object SomeScreen : KScreen<SomeScreen>() {

                override val layoutId = R.layout.fragment_some_screen
                override val viewClass = SomeScreenFragment::class.java

                val subtitle = KButton { withId(R.id.subtitle) }
                val title = KButton { withId(R.id.title) }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun notScreenObject() {
        // language="kotlin"
        val case =
            """
            object SomeScreen
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }
}
/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class IsVisibleUsageTests {
    private val rule = IsVisibleUsageRule(Config.empty)

    @Test
    fun containsUnexpectedCall() {
        // language="kotlin"
        val case =
            """
            class SomeTest : BaseTestCase() {

                @Test
                fun shouldDoSomething() {
                    start {
                        step("should assert something") {
                            SomeScreen {
                                element.isVisible()
                            }
                        }
                    }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun notContainsUnexpectedCall() {
        // language="kotlin"
        val case =
            """
            class SomeTest : BaseTestCase() {

                @Test
                fun shouldDoSomething() {
                    start {
                        step("should assert something") {
                            SomeScreen {
                                element.isDisplayed()
                            }
                        }
                    }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }
}
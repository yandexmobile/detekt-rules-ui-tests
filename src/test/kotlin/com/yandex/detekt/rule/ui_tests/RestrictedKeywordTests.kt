/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class RestrictedKeywordTests {
    private val rule = RestrictedKeywordRule(Config.empty)

    @Test
    fun tryExpressionInTestMethod() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomething() {
                    try { } catch (e: NoMatchingViewException) { println(e) }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun tryExpressionInScreenObject() {
        // language="kotlin"
        val case =
            """
            object LocationDialog : UiScreen<LocationDialog>() {
                override val packageName: String = "com.google.android.gms"
                fun closeIfDisplayed() {
                    try { } catch (e: NoMatchingViewException) { println(e) }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun tryExpressionInScenario() {
        // language="kotlin"
        val case =
            """
            class SomeScenario<ScenarioData> : BaseScenario<ScenarioData>() {
                override val steps: TestContext<ScenarioData>.() -> Unit = {
                    try { } catch (e: NoMatchingViewException) { println(e) }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun ifExpressionInTestMethod() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomething() {
                    val x = true
                    if (x) { println(1) } else { println(2) }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun whenExpressionInTestMethod() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                @Test fun shouldDoSomething() {
                    val x = true
                    when {
                        x -> println(1)
                        else -> println(2)
                    }
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }
}
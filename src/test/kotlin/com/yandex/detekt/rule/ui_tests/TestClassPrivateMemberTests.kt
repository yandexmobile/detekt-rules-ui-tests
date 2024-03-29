/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
@file:Suppress("UnusedReceiverParameter")
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class TestClassPrivateMemberTests {
    private companion object {
        const val CLASS_PARAMETER_NAME = "baseTestClass"
        const val CLASS_PARAMETER_VALUE = "BaseTestCase"
    }

    private val rule = TestClassPrivateMemberRule(TestConfig(CLASS_PARAMETER_NAME to CLASS_PARAMETER_VALUE))

    @Test
    fun mainCaseWithoutViolations() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                private companion object {
                    const val INDEX_OF_ELEMENT = 1
                }
                private val queueResponses = emptyList()
                private fun TestContext<Unit>.checkSomething() { }

                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }

    @Test
    fun privateConstInCompanionObject() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                private companion object {
                    private const val INDEX_OF_ELEMENT = 1
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun privateVariableInCompanionObject() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                private companion object {
                    private val variable = emptyList()
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun nonPrivateCompanionObject() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                companion object {
                    const val INDEX_OF_ELEMENT = 1
                }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun nonPrivateFunction() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                fun TestContext<Unit>.checkPhotoDisplay() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun nonPrivateNonLocalVariable() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                val queueResponses = emptyList()
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun unspecifiedBaseClass() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTest() {
                val queueResponses = emptyList()
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }

    @Test
    fun containsRule() {
        // language="kotlin"
        val case =
            """
            class SomeTest: BaseTestCase() {
                @get:Rule
                val tmp = TemporaryFolder()
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }
}
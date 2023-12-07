/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test

class TestClassNamingTests {
    private val rule = TestClassNamingRule(Config.empty)

    @Test
    fun caseWhereIsNoSuffix() {
        // language="kotlin"
        val case =
            """
            class WithoutSuffix: BaseTestCase() {
                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.size == 1)
    }

    @Test
    fun caseWhereIsSuffixTest() {
        // language="kotlin"
        val case =
            """
            class SuffixTest : BaseTestCase() {
                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }

    @Test
    fun caseWhereIsSuffixTests() {
        // language="kotlin"
        val case =
            """
            class SuffixTests : BaseTestCase() {
                @Test fun shouldDoSomething() { }
            }
            """.trimIndent()

        val findings = rule.lint(case)

        assert(findings.isEmpty())
    }
}
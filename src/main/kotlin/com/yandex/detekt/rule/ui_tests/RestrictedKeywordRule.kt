/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtTryExpression
import org.jetbrains.kotlin.psi.KtWhenExpression

class RestrictedKeywordRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Restricted keyword for test method, ScreenObject or Scenario",
        debt = Debt.FIVE_MINS,
    )

    override fun visitTryExpression(expression: KtTryExpression) {
        super.visitTryExpression(expression)
        if (expression.inTestMethod()) {
            reportIssue(expression, "Test method must not contain 'try' expression")
            return
        }

        if (expression.inScenario()) {
            reportIssue(expression, "Scenario must not contain 'try' expression")
            return
        }

        if (expression.isOrInScreenObject()) {
            reportIssue(expression, "ScreenObject must not contain 'try' expression")
        }
    }

    override fun visitIfExpression(expression: KtIfExpression) {
        super.visitIfExpression(expression)
        if (expression.inTestMethod()) reportIssue(expression, "Test method must not contain 'if' expression")
    }

    override fun visitWhenExpression(expression: KtWhenExpression) {
        super.visitWhenExpression(expression)
        if (expression.inTestMethod()) reportIssue(expression, "Test method must not contain 'when' expression")
    }

    private fun reportIssue(element: PsiElement, message: String) =
        report(CodeSmell(issue, Entity.from(element), message))
}
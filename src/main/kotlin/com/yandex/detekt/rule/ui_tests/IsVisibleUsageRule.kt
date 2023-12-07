/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.resolve.calls.util.getCalleeExpressionIfAny

class IsVisibleUsageRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "In general, 'isVisible' should not be used. " +
                "Use 'isDisplayed', 'isCompletelyDisplayed', 'isNotCompletelyDisplayed'",
        debt = Debt.FIVE_MINS,
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        val isVisibleAssertion = expression.getCalleeExpressionIfAny()?.text?.equals("isVisible") ?: false
        if (isVisibleAssertion) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "In general, 'Espresso isVisible' should not be used -> use 'isDisplayed'"
                )
            )
        }
    }
}
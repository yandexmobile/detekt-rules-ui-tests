/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.identifierName
import org.jetbrains.kotlin.psi.KtClass

class TestClassNamingRule(config: Config) : Rule(config) {

    private companion object {
        const val REGULAR_EXPRESSION = "[A-Z][a-zA-Z0-9]*(Test|Tests)"
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "A test class name should fit the naming pattern $REGULAR_EXPRESSION",
        debt = Debt(mins = 1)
    )

    private val classPattern = REGULAR_EXPRESSION.toRegex()

    override fun visitClass(ktClass: KtClass) {
        super.visitClass(ktClass)

        /** copy-paste optimization */
        if (ktClass.nameAsSafeName.isSpecial || ktClass.nameIdentifier?.parent?.javaClass == null) {
            return
        }

        if (!ktClass.isTestClass()) return

        if (!ktClass.identifierName().removeSurrounding("`").matches(classPattern)) {
            report(
                CodeSmell(
                    issue,
                    Entity.atName(ktClass),
                    message = "Test class names should match the pattern: $classPattern"
                )
            )
        }
    }
}
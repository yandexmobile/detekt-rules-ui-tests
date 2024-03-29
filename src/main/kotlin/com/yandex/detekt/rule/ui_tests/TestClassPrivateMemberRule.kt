/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.isConstant
import io.gitlab.arturbosch.detekt.rules.isOverride
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.getNonStrictParentOfType
import org.jetbrains.kotlin.psi.psiUtil.isPrivate

class TestClassPrivateMemberRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Members of test class must use private modifier",
        debt = Debt(mins = 1)
    )

    private val baseTestClass: String by config(defaultValue = "TestCase")

    override fun visitObjectDeclaration(declaration: KtObjectDeclaration) {
        super.visitObjectDeclaration(declaration)

        if (!declaration.inTestClass(baseTestClass)) return

        if (declaration.isCompanion() &&
            !declaration.isPrivate()
        ) {
            reportIssue(declaration, "Add private modifier to companion object")
        }
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (!property.inTestClass(baseTestClass)) return
        if (property.annotationEntries.any { it.shortName?.asString() == "Rule" }) return

        val isNonLocalVariable = !property.isConstant() && !property.isLocal
        if (isNonLocalVariable &&
            !property.isCompanionObjectProperty() &&
            !property.isPrivate()
        ) {
            reportIssue(property, "Add private modifier to non-local variable")
        }

        if (!property.isConstant() &&
            property.isCompanionObjectProperty() &&
            property.isPrivate()
        ) {
            reportIssue(property, "Make property non-private and move it to private companion object")
        }

        if (property.isConstant() &&
            property.isPrivate()
        ) {
            reportIssue(property, "Make constant non-private and move it to private companion object")
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (!function.inTestClass(baseTestClass)) return

        if (function.annotationEntries.isEmpty() &&
            !function.isOverride() &&
            !function.isPrivate()
        ) {
            reportIssue(function, "Add private modifier to function")
        }
    }

    private fun KtProperty.isCompanionObjectProperty(): Boolean {
        return getNonStrictParentOfType<KtObjectDeclaration>()?.isCompanion() ?: false
    }

    private fun reportIssue(element: PsiElement, message: String) =
        report(CodeSmell(issue, Entity.from(element), message))
}
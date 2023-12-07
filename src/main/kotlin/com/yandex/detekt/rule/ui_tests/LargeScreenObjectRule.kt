/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.github.detekt.metrics.linesOfCode
import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtClassOrObject

class LargeScreenObjectRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Split a large ScreenObject into PageElement's and combine them on this ScreenObject",
        debt = Debt(hours = 4)
    )

    private val allowedLinesOfCode: Int by config(defaultValue = 100)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        if (!classOrObject.isOrInScreenObject()) return

        val lines = classOrObject.linesOfCode()
        if (lines >= allowedLinesOfCode) {
            report(
                ThresholdedCodeSmell(
                    issue,
                    Entity.atName(classOrObject),
                    Metric("SIZE", lines, allowedLinesOfCode),
                    "Split a large ScreenObject into PageElement's and combine them on this SO"
                )
            )
        }
    }
}
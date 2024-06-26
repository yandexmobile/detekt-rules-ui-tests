/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import org.jetbrains.kotlin.fir.lightTree.converter.nameAsSafeName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getNonStrictParentOfType
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames

private const val TEST_ANNOTATION = "Test"
private const val SCENARIO_BASE_CLASS = "BaseScenario"
private val screenBaseClasses = listOf("KScreen", "UiScreen", "ComposeScreen")

fun KtElement.inTestMethod(): Boolean = getNonStrictParentOfType<KtNamedFunction>()?.isTestMethod() ?: false

fun KtNamedFunction.isTestMethod(): Boolean = annotationEntries.any { it.shortName?.asString() == TEST_ANNOTATION }

fun KtElement.inTestClass(baseTestClass: String): Boolean {
    return getNonStrictParentOfType<KtClass>()
        ?.getSuperNames()
        ?.any {
            it.nameAsSafeName().asString() == baseTestClass
        } ?: false
}

fun KtClass.isTestClass(): Boolean = body?.functions.orEmpty().any(KtNamedFunction::isTestMethod)

fun KtElement.isOrInScreenObject(baseClasses: List<String> = screenBaseClasses): Boolean {
    return getNonStrictParentOfType<KtClassOrObject>()
        ?.getSuperNames()
        .orEmpty().any { it in baseClasses }
}

fun KtElement.inScenario(): Boolean {
    return getNonStrictParentOfType<KtClassOrObject>()
        ?.getSuperNames()
        .orEmpty().any { it == SCENARIO_BASE_CLASS }
}

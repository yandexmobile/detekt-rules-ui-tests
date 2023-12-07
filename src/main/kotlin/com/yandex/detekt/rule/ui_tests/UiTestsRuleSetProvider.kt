/*
 * Copyright 2023 Yandex LLC. Use of this source code is governed by the MIT license.
 */
package com.yandex.detekt.rule.ui_tests

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class UiTestsRuleSetProvider : RuleSetProvider {
    override val ruleSetId = "ui-tests"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                TestClassNamingRule(config),
                TestMethodNamingRule(config),
                TestClassPrivateMemberRule(config),
                IsVisibleUsageRule(config),
                LargeScreenObjectRule(config),
                RestrictedKeywordRule(config),
            )
        )
    }
}
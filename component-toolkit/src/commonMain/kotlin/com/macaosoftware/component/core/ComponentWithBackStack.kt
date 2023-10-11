package com.macaosoftware.component.core

import com.macaosoftware.component.stack.BackStack
import com.macaosoftware.component.stack.PushStrategy

interface ComponentWithBackStack : ComponentWithChildrenOneActive {
    val backStack: BackStack<Component>
    val navigator: Navigator
    var isFirstComponentInStackPreviousCache: Boolean

    fun createBackStack(pushStrategy: PushStrategy<Component>): BackStack<Component> {
        return BackStack(pushStrategy)
    }
}

package com.macaosoftware.component.topbar

import com.macaosoftware.component.core.Component
import com.macaosoftware.component.viewmodel.ComponentViewModel

abstract class TopBarComponentViewModel(
    protected val topBarComponent: TopBarComponent<out TopBarComponentViewModel>,
    open val showBackArrowStrategy: ShowBackArrowStrategy = ShowBackArrowStrategy.Always
) : ComponentViewModel() {

    abstract val topBarStatePresenter: TopBarStatePresenter

    abstract fun onCreate()
    abstract fun mapComponentToStackBarItem(topComponent: Component): TopBarItem
    abstract fun onCheckChildForNextUriFragment(
        nextUriFragment: String
    ): Component?

    abstract fun onBackstackEmpty()
}

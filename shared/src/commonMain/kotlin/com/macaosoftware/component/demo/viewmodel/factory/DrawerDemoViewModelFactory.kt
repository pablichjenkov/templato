package com.macaosoftware.component.demo.viewmodel.factory

import com.macaosoftware.component.demo.viewmodel.DrawerDemoViewModel
import com.macaosoftware.component.drawer.DrawerComponent
import com.macaosoftware.component.drawer.DrawerComponentViewModelFactory
import com.macaosoftware.component.drawer.DrawerStatePresenterDefault

class DrawerDemoViewModelFactory(
    private val drawerStatePresenter: DrawerStatePresenterDefault
) : DrawerComponentViewModelFactory<DrawerDemoViewModel> {
    override fun create(
        drawerComponent: DrawerComponent<DrawerDemoViewModel>
    ): DrawerDemoViewModel {
        return DrawerDemoViewModel(drawerComponent, drawerStatePresenter)
    }
}

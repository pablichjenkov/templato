package com.pablichj.incubator.uistate3.demo

import com.pablichj.incubator.uistate3.IosComponentRender
import com.pablichj.incubator.uistate3.demo.treebuilders.AdaptableSizeTreeBuilder
import com.pablichj.incubator.uistate3.demo.treebuilders.DrawerTreeBuilder
import com.pablichj.incubator.uistate3.demo.treebuilders.FullAppWithIntroTreeBuilder
import com.pablichj.incubator.uistate3.node.Component
import com.pablichj.incubator.uistate3.node.drawer.DrawerComponent
import com.pablichj.incubator.uistate3.node.navbar.NavBarComponent
import com.pablichj.incubator.uistate3.node.panel.PanelComponent
import com.pablichj.incubator.uistate3.platform.PlatformDelegate
import platform.UIKit.UIViewController

fun ComponentRenderer(
    rootComponent: Component,
    platformDelegate: PlatformDelegate
): UIViewController = IosComponentRender(rootComponent, platformDelegate)

fun buildDrawerComponent(): Component {
    return DrawerTreeBuilder.build()
}

fun buildAdaptableSizeComponent(): Component {
    val subtreeNavItems = AdaptableSizeTreeBuilder.getOrCreateDetachedNavItems()
    return AdaptableSizeTreeBuilder.build().also {
        it.setNavItems(subtreeNavItems, 0)
        it.setCompactContainer(DrawerComponent())
        it.setMediumContainer(NavBarComponent())
        it.setExpandedContainer(PanelComponent())
    }
}

fun buildAppWithIntroComponent(): Component {
    return FullAppWithIntroTreeBuilder.build()
}

fun createPlatformDelegate(): PlatformDelegate {
    return PlatformDelegate()
}
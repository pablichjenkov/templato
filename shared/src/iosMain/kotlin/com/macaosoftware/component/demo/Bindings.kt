package com.macaosoftware.component.demo

import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import com.macaosoftware.app.MacaoApplication
import com.macaosoftware.app.MacaoApplicationState
import com.macaosoftware.app.MacaoKoinApplicationState
import com.macaosoftware.app.MacaoKoinComposeViewController
import com.macaosoftware.component.adaptive.AdaptiveSizeComponent
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.demo.plugin.DemoKoinModuleInitializer
import com.macaosoftware.component.demo.plugin.DemoPluginInitializer
import com.macaosoftware.component.demo.view.SplashScreen
import com.macaosoftware.component.demo.viewmodel.factory.AdaptiveSizeDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.AppViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.DrawerDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.PagerDemoViewModelFactory
import com.macaosoftware.component.drawer.DrawerComponent
import com.macaosoftware.component.drawer.DrawerComponentDefaults
import com.macaosoftware.component.pager.PagerComponent
import com.macaosoftware.component.pager.PagerComponentDefaults
import com.macaosoftware.component.stack.StackComponent
import com.macaosoftware.component.stack.StackComponentDefaults
import com.macaosoftware.plugin.AppLifecycleEvent
import com.macaosoftware.plugin.IosBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.UIKit.UIViewController

fun buildDemoViewController(
    iosBridge: IosBridge,
    onBackPress: () -> Unit = {}
): UIViewController {

    val mDelegate = object : ComposeUIViewControllerDelegate {
        override fun viewDidAppear(animated: Boolean) {
            println("Pablo::viewDidAppear")
        }

        override fun viewDidDisappear(animated: Boolean) {
            println("Pablo::viewDidDisappear")
        }
    }

    return ComposeUIViewController(
        configure = {
            delegate = mDelegate
        }
    ) {
        val macaoApplicationState = MacaoApplicationState(
            // iosBridge,
            dispatcher = Dispatchers.IO,
            rootComponentProvider = IosRootComponentProvider(),
            pluginInitializer = DemoPluginInitializer()
        )

        MacaoApplication(
            onBackPress = onBackPress,
            macaoApplicationState = macaoApplicationState
        )
    }

}

fun buildKoinDemoViewController(
    iosBridge: IosBridge,
    onBackPress: () -> Unit = {}
): UIViewController {

    val applicationState = MacaoKoinApplicationState(
        // iosBridge,
        dispatcher = Dispatchers.IO,
        rootComponentKoinProvider = IosRootComponentKoinProvider(),
        koinModuleInitializer = DemoKoinModuleInitializer()
    )

    return MacaoKoinComposeViewController(
        applicationState,
        onBackPress
    )
}

private fun buildDrawerComponent(): Component {
    return DrawerComponent(
        viewModelFactory = DrawerDemoViewModelFactory(
            DrawerComponentDefaults.createDrawerStatePresenter()
        ),
        content = DrawerComponentDefaults.DrawerComponentView
    )
}

private fun buildPagerComponent(): Component {
    return PagerComponent(
        viewModelFactory = PagerDemoViewModelFactory(),
        content = PagerComponentDefaults.PagerComponentView
    )
}

private fun buildAdaptableSizeComponent(): Component {
    return AdaptiveSizeComponent(AdaptiveSizeDemoViewModelFactory())
}

private fun buildAppWithIntroComponent(): Component {
    return StackComponent(
        viewModelFactory = AppViewModelFactory(
            stackStatePresenter = StackComponentDefaults.createStackStatePresenter(),
        ),
        content = StackComponentDefaults.DefaultStackComponentView
    )
}

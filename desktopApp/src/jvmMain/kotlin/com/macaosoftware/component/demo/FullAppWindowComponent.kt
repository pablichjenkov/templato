package com.macaosoftware.component.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.DesktopComponentRender
import com.macaosoftware.component.demo.treebuilders.FullAppWithIntroTreeBuilder
import com.macaosoftware.platform.DesktopBridge

class FullAppWindowComponent(
    val onCloseClick: () -> Unit
) : Component() {
    private val windowState = WindowState(size = DpSize(800.dp, 900.dp))
    private var activeComponent: Component = FullAppWithIntroTreeBuilder.build()
    private val desktopBridge = DesktopBridge()

    @Composable
    override fun Content(modifier: Modifier) {
        Window(
            state = windowState,
            onCloseRequest = { onCloseClick() }
        ) {
            DesktopComponentRender(
                rootComponent = activeComponent,
                windowState = windowState,
                onBackPress = onCloseClick,
                desktopBridge = desktopBridge
            )
        }
    }

}
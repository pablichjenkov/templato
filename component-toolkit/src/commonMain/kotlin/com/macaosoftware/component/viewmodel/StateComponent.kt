package com.macaosoftware.component.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.macaosoftware.component.core.Component

class StateComponent<out VM : ComponentViewModel>(
    viewModelFactory: ComponentViewModelFactory<VM>,
    private val content: @Composable StateComponent<VM>.(
        modifier: Modifier,
        componentViewModel: VM
    ) -> Unit
) : Component() {

    val componentViewModel = viewModelFactory.create(this)

    @Composable
    override fun Content(modifier: Modifier) {
        this@StateComponent.content(modifier, componentViewModel)
    }
}
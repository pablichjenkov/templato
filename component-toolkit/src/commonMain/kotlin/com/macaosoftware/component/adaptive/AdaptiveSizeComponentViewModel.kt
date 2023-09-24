package com.macaosoftware.component.adaptive

import com.macaosoftware.component.viewmodel.ComponentViewModel

abstract class AdaptiveSizeComponentViewModel : ComponentViewModel()

class AdaptiveSizeComponentDefaultViewModel : AdaptiveSizeComponentViewModel() {

    override fun onStart() {
        println("AdaptiveSizeComponentDefaultViewModel::onStart()")
    }

    override fun onStop() {
        println("AdaptiveSizeComponentDefaultViewModel::onStop()")
    }

    override fun onDestroy() {
        println("AdaptiveSizeComponentDefaultViewModel::onDestroy()")
    }
}

package com.pablichj.templato.component.core.split

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pablichj.templato.component.core.Component
import com.pablichj.templato.component.core.router.DeepLinkMatchData
import com.pablichj.templato.component.core.router.DeepLinkMatchType

class SplitComponent(
    private val config: Config
) : Component() {
    private var topComponent: Component? = null
    private var bottomComponent: Component? = null

    fun setTopNode(topComponent: Component) {
        this.topComponent = topComponent.apply {
            setParent(this@SplitComponent)
        }
    }

    fun setBottomNode(bottomComponent: Component) {
        this.bottomComponent = bottomComponent.apply {
            setParent(this@SplitComponent)
        }
    }

    override fun onStart() {
        println("$clazz::start")
        topComponent?.dispatchStart()
        bottomComponent?.dispatchStart()
    }

    override fun onStop() {
        println("$clazz::stop")
        topComponent?.dispatchStop()
        bottomComponent?.dispatchStop()
    }

    override fun getDeepLinkHandler(): DeepLinkMatchData {
        return DeepLinkMatchData(
            null,
            DeepLinkMatchType.MatchAny
        )
    }

    override fun getChildForNextUriFragment(nextUriFragment: String): Component? {
        return if (topComponent?.getDeepLinkHandler()?.uriFragment == nextUriFragment) {
            topComponent
        } else if (bottomComponent?.getDeepLinkHandler()?.uriFragment == nextUriFragment) {
            bottomComponent
        } else null
    }

    @Composable
    override fun Content(modifier: Modifier) {
        println("SplitNavNode::Composing()")
        Column(modifier = Modifier.fillMaxSize()) {
            val TopNodeCopy = topComponent
            if (TopNodeCopy != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F)
                        .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 20.dp)
                ) {
                    TopNodeCopy.Content(Modifier)
                }
            }

            val BottomNodeCopy = bottomComponent
            if (BottomNodeCopy != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 40.dp, top = 20.dp, end = 40.dp, bottom = 40.dp)
                ) {
                    BottomNodeCopy.Content(Modifier)
                }
            }
        }
    }

    class Config(
        var splitStyle: SplitStyle = SplitStyle()
    )

    companion object {
        val DefaultConfig = Config(
            SplitStyle()
        )
    }

}
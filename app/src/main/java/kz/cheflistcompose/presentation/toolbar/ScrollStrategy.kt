package kz.cheflistcompose.presentation.toolbar

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

enum class ScrollStrategy {
    EnterAlways {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: kz.cheflistcompose.presentation.toolbar.CollapsingToolbarState
        ): NestedScrollConnection =
            EnterAlwaysNestedScrollConnection(offsetY, toolbarState)
    },
    EnterAlwaysCollapsed {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: kz.cheflistcompose.presentation.toolbar.CollapsingToolbarState
        ): NestedScrollConnection =
            EnterAlwaysCollapsedNestedScrollConnection(offsetY, toolbarState)
    },
    ExitUntilCollapsed {
        override fun create(
            offsetY: MutableState<Int>,
            toolbarState: kz.cheflistcompose.presentation.toolbar.CollapsingToolbarState
        ): NestedScrollConnection =
            ExitUntilCollapsedNestedScrollConnection(toolbarState)
    };

    internal abstract fun create(
        offsetY: MutableState<Int>,
        toolbarState: kz.cheflistcompose.presentation.toolbar.CollapsingToolbarState
    ): NestedScrollConnection
}

private class ScrollDelegate(
    private val offsetY: MutableState<Int>
) {
    private var scrollToBeConsumed: Float = 0f

    fun doScroll(delta: Float) {
        val scroll = scrollToBeConsumed + delta
        val scrollInt = scroll.toInt()

        scrollToBeConsumed = scroll - scrollInt

        offsetY.value += scrollInt
    }
}

internal class EnterAlwaysNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState
): NestedScrollConnection {
    private val scrollDelegate = ScrollDelegate(offsetY)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y

        val toolbar = toolbarState.height.toFloat()
        val offset = offsetY.value.toFloat()

        // -toolbarHeight <= offsetY + dy <= 0
        val consume = if(dy < 0) {
            val toolbarConsumption = toolbarState.feedScroll(dy)
            val remaining = dy - toolbarConsumption
            val offsetConsumption = remaining.coerceAtLeast(-toolbar - offset)
            scrollDelegate.doScroll(offsetConsumption)

            toolbarConsumption + offsetConsumption
        }else{
            val offsetConsumption = dy.coerceAtMost(-offset)
            scrollDelegate.doScroll(offsetConsumption)

            val toolbarConsumption = toolbarState.feedScroll(dy - offsetConsumption)

            offsetConsumption + toolbarConsumption
        }

        return Offset(0f, consume)
    }
}

internal class EnterAlwaysCollapsedNestedScrollConnection(
    private val offsetY: MutableState<Int>,
    private val toolbarState: CollapsingToolbarState
): NestedScrollConnection {
    private val scrollDelegate = ScrollDelegate(offsetY)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y

        val consumed = if(dy > 0) { // expanding: offset -> body -> toolbar
            val offsetConsumption = dy.coerceAtMost(-offsetY.value.toFloat())
            scrollDelegate.doScroll(offsetConsumption)

            offsetConsumption
        }else{ // collapsing: toolbar -> offset -> body
            val toolbarConsumption = toolbarState.feedScroll(dy)
            val offsetConsumption = (dy - toolbarConsumption).coerceAtLeast(-toolbarState.height.toFloat() - offsetY.value)

            scrollDelegate.doScroll(offsetConsumption)

            toolbarConsumption + offsetConsumption
        }

        return Offset(0f, consumed)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val dy = available.y

        return if(dy > 0) {
            Offset(0f, toolbarState.feedScroll(dy))
        }else{
            Offset(0f, 0f)
        }
    }
}

internal class ExitUntilCollapsedNestedScrollConnection(
    private val toolbarState: CollapsingToolbarState
): NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val dy = available.y
        val consume = if(dy < 0) { // collapsing: toolbar -> body
            toolbarState.feedScroll(dy)
        }else{
            0f
        }

        return Offset(0f, consume)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val dy = available.y

        val consume = if(dy > 0) { // expanding: body -> toolbar
            toolbarState.feedScroll(dy)
        }else{
            0f
        }

        return Offset(0f, consume)
    }
}
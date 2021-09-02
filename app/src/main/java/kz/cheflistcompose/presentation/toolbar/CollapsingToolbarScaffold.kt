package kz.cheflistcompose.presentation.toolbar

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import kotlin.math.max

@Stable
class CollapsingToolbarScaffoldState(
    val toolbarState: CollapsingToolbarState,
    initialOffsetY: Int = 0
) {
    val offsetY: Int
        get() = offsetYState.value

    internal val offsetYState = mutableStateOf(initialOffsetY)
}

private class CollapsingToolbarScaffoldStateSaver: Saver<CollapsingToolbarScaffoldState, Bundle> {
    override fun restore(value: Bundle): CollapsingToolbarScaffoldState =
        CollapsingToolbarScaffoldState(
            CollapsingToolbarState(value.getInt("height", Int.MAX_VALUE)),
            value.getInt("offsetY", 0)
        )

    override fun SaverScope.save(value: CollapsingToolbarScaffoldState): Bundle =
        Bundle().apply {
            putInt("height", value.toolbarState.height)
            putInt("offsetY", value.offsetY)
        }
}

@Composable
fun rememberCollapsingToolbarScaffoldState(
    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState()
): CollapsingToolbarScaffoldState {
    return rememberSaveable(toolbarState, saver = CollapsingToolbarScaffoldStateSaver()) {
        CollapsingToolbarScaffoldState(toolbarState)
    }
}

@Composable
fun CollapsingToolbarScaffold(
    modifier: Modifier,
    state: CollapsingToolbarScaffoldState,
    scrollStrategy: ScrollStrategy,
    toolbarModifier: Modifier = Modifier,
    toolbar: @Composable CollapsingToolbarScope.() -> Unit,
    body: @Composable () -> Unit
) {
    val nestedScrollConnection = remember(scrollStrategy, state) {
        scrollStrategy.create(state.offsetYState, state.toolbarState)
    }

    val toolbarState = state.toolbarState

    SubcomposeLayout(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
    ) { constraints ->
        val toolbarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight =
            if(scrollStrategy == ScrollStrategy.ExitUntilCollapsed)
                max(0, constraints.maxHeight - toolbarState.minHeight)
            else
                constraints.maxHeight
        )

        val toolbarPlaceables = subcompose(CollapsingToolbarScaffoldContent.Toolbar) {
            CollapsingToolbar(
                modifier = toolbarModifier,
                collapsingToolbarState = toolbarState
            ) {
                toolbar()
            }
        }.map { it.measure(toolbarConstraints) }

        val bodyPlaceables = subcompose(CollapsingToolbarScaffoldContent.Body) {
            body()
        }.map { it.measure(bodyConstraints) }

        val width = max(
            toolbarPlaceables.maxOfOrNull { it.width } ?: 0,
            bodyPlaceables.maxOfOrNull { it.width } ?: 0
        ).coerceIn(constraints.minWidth, constraints.maxWidth)

        val toolbarHeight = toolbarPlaceables.maxOfOrNull { it.height } ?: 0

        val height = max(
            toolbarHeight,
            bodyPlaceables.maxOfOrNull { it.height } ?: 0
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            bodyPlaceables.forEach {
                it.place(0, toolbarHeight + state.offsetY)
            }

            toolbarPlaceables.forEach {
                it.place(0, state.offsetY)
            }
        }
    }
}

private enum class CollapsingToolbarScaffoldContent {
    Toolbar, Body
}
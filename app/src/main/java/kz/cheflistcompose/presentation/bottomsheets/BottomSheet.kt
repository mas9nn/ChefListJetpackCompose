package kz.cheflistcompose.presentation.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BottomSheet(viewModel: BottomSheetViewModel = hiltViewModel()) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }
    Column(modifier = Modifier.fillMaxSize()){
        Text("Sheet with arg: ")
        Button(onClick = { }) {
            Text("Click me to navigate!")
        }
        Button(onClick = {}) {
            Text("Click me to show another sheet!")
        }
    }
}
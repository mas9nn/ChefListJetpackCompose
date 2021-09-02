package kz.cheflistcompose.presentation.bottomsheets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor():ViewModel(){


    fun onStart() {
        Timber.wtf("startBottomSheet")
    }

    fun onStop() {
        Timber.wtf("stopBottomSheet")
    }

}
package kz.cheflistcompose.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor():ViewModel(){

    val currentPage:MutableLiveData<Int> = MutableLiveData(0)

    fun onStart() {
       Timber.wtf("start")
    }

    fun onStop() {
        Timber.wtf("stop")
    }

}
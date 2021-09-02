package kz.cheflistcompose.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor():ViewModel() {

    val currentPage:MutableLiveData<Int> = MutableLiveData(0)

}
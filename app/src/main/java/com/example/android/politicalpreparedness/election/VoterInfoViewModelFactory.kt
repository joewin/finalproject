package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}
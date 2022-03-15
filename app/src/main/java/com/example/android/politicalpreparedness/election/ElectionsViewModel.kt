package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import java.lang.Exception

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)
    //TODO: Create live data val for upcoming elections
    private var _elections = MutableLiveData<List<Election>> ()

    val elections: LiveData<List<Election>>
        get() = _elections
    //TODO: Create live data val for saved elections

    private var _followedElections = MutableLiveData<List<Election>>()

    val followedElections = repository.getAllSaveElections()



    private val _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo
    init{
        try {
           getUpcomingElectionsFromNetwork()
        }
        catch (e:Exception){
        }

    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
   private fun getUpcomingElectionsFromNetwork() {
        viewModelScope.launch {
            _elections.value = repository.getUpcomingElections()
        }
    }
    //TODO: Create functions to navigate to saved or upcoming election voter info

    fun onItemClick(item: Election){
        _navigateToVoterInfo.value = item
    }
    fun doneNavigation(){
        _navigateToVoterInfo.value = null
    }



}
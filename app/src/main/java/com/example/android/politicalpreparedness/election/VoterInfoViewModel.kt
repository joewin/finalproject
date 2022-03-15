package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val application: Application) : ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)

    //live data to hold voter info
    private var _voterInfo = MutableLiveData<AdministrationBody>()
    val voterInfo: LiveData<AdministrationBody>
        get() = _voterInfo

    //livedata for url loading
    private var _goToElectionInformation = MutableLiveData<Boolean>()
    val goToElectionInformation: LiveData<Boolean>
        get() = _goToElectionInformation
    private var _goToVotingLocation = MutableLiveData<Boolean>()
    val goToVotingLocation: LiveData<Boolean>
        get() = _goToVotingLocation
    private val _goToBallotInformation = MutableLiveData<Boolean>()
    val goToBallotInformation: LiveData<Boolean>
        get() = _goToBallotInformation

    //election livedata
    private var _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    //saved election
    private var _saveElection = MutableLiveData<Election?>()
    val saveElection: LiveData<Election?>
        get() = _saveElection

    init {
        _goToElectionInformation.value = false
        _goToVotingLocation.value = false
        _goToBallotInformation.value = false
    }


    //methods to populate voter info
    fun loadVoterInfo(division: Division, electionId:Int){
        viewModelScope.launch {
            _saveElection.value = repository.getSavedElectionById(electionId)
            val address = "${division.country} ${division.state}"
            _voterInfo.value =repository.getVoterInfo(address,electionId)?.state?.first()?.electionAdministrationBody
            _election.value = repository.getVoterInfo(address,electionId)?.election

        }

    }
    //TODO: Add var and methods to support loading URLs
    fun goToElectionInformation(){
        _goToElectionInformation.value = true
    }
    fun doneGoToElectionInformation(){
        _goToElectionInformation.value = false
    }
    fun goToVotingLocation(){
        _goToVotingLocation.value = true
    }
    fun doneGoToVotingLocation(){
        _goToVotingLocation.value = false
    }
    fun goToBallotInformation(){
        _goToBallotInformation.value = true
    }
    fun doneGotoBallotInformation(){
        _goToBallotInformation.value = false
    }

    //Add var and methods to save and remove elections to local database
    fun saveElection(){
        viewModelScope.launch {
            if (election.value?.id == saveElection.value?.id) {
                repository.deleteElection(election.value!!)
                _saveElection.value = null
            }else{
                repository.savedElection(election.value!!)
                _saveElection.value = election.value
            }
        }
    }


}
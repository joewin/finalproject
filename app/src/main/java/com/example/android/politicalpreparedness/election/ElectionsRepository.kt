package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Database
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsHttpClient
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionsRepository (val database: ElectionDatabase){

    fun getAllSaveElections(): LiveData<List<Election>> {
        return database.electionDao.getAllElections()
    }

   suspend fun getUpcomingElections():List<Election>{
       return try {
          // Log.i("Election response",CivicsApi.retrofitService.getElectionResponse().toString() )
           CivicsApi.retrofitService.getElectionResponse().elections
       } catch (e: Exception){
           Log.i("NETWORK ERROR", e.message.toString())
           emptyList()
       }
    }
    suspend fun getVoterInfo(address: String,electionId: Int) : VoterInfoResponse?{
        return try{
            CivicsApi.retrofitService.getVoterInfoResponse(address,electionId)
        }
        catch(e: java.lang.Exception){
            Log.i("NETWORK ERROR", e.message.toString())
            null
        }
    }

    suspend fun getSavedElectionById(id:Int):Election{
        Log.i("ID ELECTION",id.toString())
        return database.electionDao.getElectionById(id)
    }

    suspend fun savedElection(election:Election){
        database.electionDao.insert(election)
    }

    suspend fun deleteElection(election:Election){
        database.electionDao.deleteElection(election)
    }
}
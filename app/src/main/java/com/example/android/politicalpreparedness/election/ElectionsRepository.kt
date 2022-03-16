package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionsRepository (val database: ElectionDatabase){

    // get all elections from database
    fun getAllSaveElections(): LiveData<List<Election>> {
        return database.electionDao.getAllElections()
    }
    // get all elections from network
   suspend fun getUpcomingElections():List<Election>{
       return try {
          // Log.i("Election response",CivicsApi.retrofitService.getElectionResponse().toString() )
           CivicsApi.retrofitService.getElectionResponse().elections
       } catch (e: Exception){
           Log.i("NETWORK ERROR", e.message.toString())
           emptyList()
       }
    }
    // get all votoinfo from network
    suspend fun getVoterInfo(address: String,electionId: Int) : VoterInfoResponse?{
        return try{
            CivicsApi.retrofitService.getVoterInfoResponse(address,electionId)
        }
        catch(e: java.lang.Exception){
            Log.i("NETWORK ERROR", e.message.toString())
            null
        }
    }
    //get election by id from database
    suspend fun getSavedElectionById(id:Int):Election{
        Log.i("ID ELECTION",id.toString())
        return database.electionDao.getElectionById(id)
    }
    // save election to database
    suspend fun savedElection(election:Election){
        database.electionDao.insert(election)
    }
    // delete election to database
    suspend fun deleteElection(election:Election){
        database.electionDao.deleteElection(election)
    }
}
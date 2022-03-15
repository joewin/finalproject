package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    //Add insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(election: Election)
    //Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>
    //Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election
    //Add delete query
    @Delete
    suspend fun deleteElection(election: Election)
    //Add clear query
    @Query("delete from election_table")
    suspend fun clearAll()

}
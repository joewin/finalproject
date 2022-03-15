package com.example.android.politicalpreparedness.representative


import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeRepository {

    suspend fun getAllRepresentative(address: Address) :List <Representative>? {
        return try{
            val (offices, officials) = CivicsApi.retrofitService.getRepresentativesResponse(address.toFormattedString())
            val representatives = offices.flatMap { it.getRepresentatives(officials) }
            representatives
        } catch(e: java.lang.Exception){
            Log.i("NETWORK ERROR", e.message.toString())
            null
        }
    }
}
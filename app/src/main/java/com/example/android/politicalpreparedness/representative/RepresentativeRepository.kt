package com.example.android.politicalpreparedness.representative


import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeRepository {
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
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
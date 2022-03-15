package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import kotlin.math.log

class RepresentativeViewModel: ViewModel() {


    private val repository = RepresentativeRepository()
    //TODO: Establish live data for representatives and address
    private val _representative = MutableLiveData<List<Representative>>()
    val representative : LiveData<List<Representative>>
        get() = _representative


    val address = MutableLiveData<Address>()



    init {
        address.value = Address("","","","","")
    }
    //Create function to fetch representatives from API from a provided address
    fun setRepresentative(){
       viewModelScope.launch {
               _representative.value = address.value?.let { repository.getAllRepresentative(it) }
       }
    }

    fun setAddress(add:Address){
        address.value = add
        Log.i("Address", address.value!!.line1)
    }
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}

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
    //Establish live data for representatives and address
    private val _representative = MutableLiveData<List<Representative>>()
    val representative : LiveData<List<Representative>>
        get() = _representative
    val address = MutableLiveData<Address>()

    private var _emptyFields = MutableLiveData<Boolean?>()
    val emptyFields: LiveData<Boolean?>
        get() = _emptyFields

    init {
        address.value = Address("","","","","")
        _emptyFields.value = true
    }
    //Create function to fetch representatives from API from a provided address
    fun setRepresentative(){
       viewModelScope.launch {
                address.value?.state?.let { Log.i("Address LOG", it) }
               _representative.value = address.value?.let { repository.getAllRepresentative(it) }
       }
    }
    //Create function get address from geo location
    fun setAddress(add:Address){
        address.value = add
        setRepresentative()
    }

    fun checkEmptyField(){
        address.value?.let {
            _emptyFields.value = it.line1.isEmpty() or it.city.isEmpty() or it.state.isEmpty() or it.zip.isEmpty()
        }

    }

}

package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.ElectionsViewModelFactory
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel: RepresentativeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this).get(
            RepresentativeViewModel::class.java)
    }
    companion object {
        //Add Constant for Location request
        private val PERMISSIONS_REQUEST_CODE = 34
    }

    private lateinit var locationProviderClient: FusedLocationProviderClient
    //TODO: Declare ViewModel
    private lateinit var address: Address
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
        binding.viewModel = viewModel
        //TODO: Establish button listeners for field and location search
        return binding.root

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            getLocation()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            Log.i("location permission","granted")
            true
        } else {
            var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(
                permissionsArray,
                PERMISSIONS_REQUEST_CODE
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }
    //Get location from LocationServices
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if(checkLocationPermissions()){
            Log.i("calling","calling")
            locationProviderClient.lastLocation.addOnSuccessListener {
                Log.i("calling",it.latitude.toString())
                if(it!=null){
                    address = getAddress(it)
                    Log.i("calling", address.state)
                    viewModel.setAddress(address)
                }
            }
        }
    }
    //The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    private fun getAddress(location: Location):Address {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map {
                Address(it.thoroughfare,it.subThoroughfare,it.locality,it.adminArea,it.postalCode)
            }
            .first()

    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}
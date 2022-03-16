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
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {
    private val ADDRESS_KEY = "address"
    private val MOTIONLAYOUT_KEY = "motionlayout"
    private val RECYCLER_INDEX_KEY = "recyclerindex"
    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel: RepresentativeViewModel by lazy {
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
    private lateinit var adapter: RepresentativeListAdapter
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        //TODO: Define and assign Representative adapter

        adapter = RepresentativeListAdapter()

        //setting up adapters
        binding.representativeRecycler.adapter = adapter
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.address.value?.state = (requireContext().resources.getStringArray(R.array.states)[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        //Populate Representative adapter
        viewModel.representative.observe(viewLifecycleOwner) {
            it?.let {  adapter.submitList(it)
                try {
                    savedInstanceState?.getInt(RECYCLER_INDEX_KEY).let {
                        if (it != null) { binding.representativeRecycler.layoutManager!!.scrollToPosition(it+it)
                        }}
                }catch (e:Exception){
                }
            }


        }
        binding.buttonSearch.setOnClickListener {
            viewModel.checkEmptyField()
            if(viewModel.emptyFields.value == false){
                hideKeyboard()
                viewModel.setRepresentative()
            }
            else{
                Toast.makeText(context,"Some fields cannot be empty",Toast.LENGTH_LONG).show()
            }
        }
        //Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            getLocation()
        }

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        savedInstanceState?.getParcelable<Address>(ADDRESS_KEY)?.let {
            viewModel.setAddress(it)
        }

        savedInstanceState?.getInt(MOTIONLAYOUT_KEY)?.let {
            binding.motionLayout.transitionToState(it)
        }
        return binding.root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //setting current state of motion-layout and address data
        outState.putInt(MOTIONLAYOUT_KEY,binding.motionLayout.currentState)
        outState.putParcelable(ADDRESS_KEY,binding.viewModel?.address?.value)
        //fixing review 3
        //fixing scrolling index one
        // 3 .When the app is relaunched from Home, All Apps or Recents Screen (if the Operating System had killed our app to free up resources), the representative list's scroll position is not restored.
        val index: Int = (binding.representativeRecycler.layoutManager
                as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        outState.putInt(RECYCLER_INDEX_KEY,index)
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
           //getting the last location
            locationProviderClient.lastLocation.addOnSuccessListener {

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


    //App crashes when the "USE MY LOCATION" button is clicked (Nexus 5X, API 29).
    //remove this to fix crush , I was planning to use location request but in the end didn't use it
    //private lateinit var locationCallback: LocationCallback
   /* override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
    private fun stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback)
    }*/
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView()!!.windowToken, 0)
    }

}
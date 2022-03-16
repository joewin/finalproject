package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    private lateinit var binding: FragmentVoterInfoBinding

    //Declare ViewModel
    private val viewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, VoterInfoViewModelFactory(activity.application)).get(VoterInfoViewModel::class.java)
    }
    //declare navigation arguments
    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        /**
         * ensure proper data is provided from previous fragment.
         */
        val electionId = args.argElectionId
        val division = args.argDivision

        //loading voterInfo
        viewModel.loadVoterInfo(division,electionId)


        binding.viewModel = viewModel


        viewModel.loading.observe(viewLifecycleOwner,{
            if(it == true){
                clickAbleFalse()
            }
            else{
              clickAbleTrue()
            }
        })
        // when user click these link open browser and open these links
        viewModel.goToBallotInformation.observe(viewLifecycleOwner,{
            if(it){
                loadingUrls(viewModel.voterInfo.value?.ballotInfoUrl.toString())
                viewModel.doneGotoBallotInformation()
            }
        })
        viewModel.goToElectionInformation.observe(viewLifecycleOwner,{
            if(it){
                loadingUrls(viewModel.voterInfo.value?.electionInfoUrl.toString())
                viewModel.doneGoToElectionInformation()
            }
        })
        viewModel.goToVotingLocation.observe(viewLifecycleOwner,{
            if(it){
                loadingUrls(viewModel.voterInfo.value?.votingLocationFinderUrl.toString())
                viewModel.doneGoToVotingLocation()
            }
        })

        //Handle save button UI state
        viewModel.saveElection.observe(viewLifecycleOwner,{
            if(it==null){
                binding.savedButton.text = getString(R.string.follow_election)
            }
            else{
                binding.savedButton.text = getString(R.string.unfollw_election)
            }
        })
        //Handle save button clicks
        binding.savedButton.setOnClickListener {
            viewModel.saveElection()
        }


        return binding.root
    }

    //Create method to load URL intents
    private fun loadingUrls(url:String){
        val uri = Uri.parse(url)
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivity(i)
    }
    //set clickable true after loading
    private fun clickAbleTrue(){
        binding.savedButton.isClickable = true
        binding.stateHeader.isClickable = true
        binding.stateLocations.isClickable = true
        binding.stateBallot.isClickable = true
    }
    //set clickable false while loading
    private fun clickAbleFalse(){
        binding.savedButton.isClickable = false
        binding.stateHeader.isClickable = false
        binding.stateLocations.isClickable = false
        binding.stateBallot.isClickable = false
    }
}
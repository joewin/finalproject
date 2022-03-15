package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    private lateinit var binding: FragmentVoterInfoBinding
    private var isSaved:Boolean = false

    //Declare ViewModel
    private val viewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, VoterInfoViewModelFactory(activity.application)).get(VoterInfoViewModel::class.java)
    }
    val args: VoterInfoFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //ass ViewModel values and create ViewModel
        val electionId = args.argElectionId
        val division = args.argDivision

        viewModel.loadVoterInfo(division,electionId)
        //checking the election Id is in the saved

        binding.viewModel = viewModel
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */
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

        binding.savedButton.setOnClickListener {
            viewModel.saveElection()
        }

        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //Create method to load URL intents
    private fun loadingUrls(url:String){
        val uri = Uri.parse(url)
        val i = Intent(Intent.ACTION_VIEW, uri)
        startActivity(i)
    }
}
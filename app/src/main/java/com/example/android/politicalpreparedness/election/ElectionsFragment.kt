package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionBinding

    private lateinit var adapter: ElectionListAdapter

    private lateinit var saveAdapter: ElectionListAdapter
    //Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, ElectionsViewModelFactory(activity.application)).get(ElectionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentElectionBinding.inflate(inflater)
        //Add ViewModel values and create ViewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //Initiate recycler adapters
        adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
            viewModel.onItemClick(it)
        }
        )
        binding.upcomingRecyclerview.adapter = adapter
        saveAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.onItemClick(it)
        })
        binding.saveRecyclerview.adapter = saveAdapter
        //Populate recycler adapters
        //Refresh adapters when fragment loads
        viewModel.elections.observe(viewLifecycleOwner,{
            it?.let {
                Log.i("division-main",it[0].division.state)
                adapter.submitList(it)
            }
        })

        viewModel.followedElections.observe(viewLifecycleOwner,{
            it?.let {
               saveAdapter.submitList(it)
            }
        })
        //Link elections to voter info
        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner,{
            it?.let {

                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id,it.division))
                viewModel.doneNavigation()
            }
        })
        return binding.root

    }



}
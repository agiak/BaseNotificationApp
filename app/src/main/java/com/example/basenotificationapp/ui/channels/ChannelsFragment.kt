package com.example.basenotificationapp.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.basenotificationapp.R
import com.example.basenotificationapp.databinding.FragmentChannelsBinding
import com.example.basenotificationapp.ui.utils.addTitleElevationAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChannelsViewModel by viewModels()

    private lateinit var channelsAdapter: ChannelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        binding.toolbar.apply {
            screenTitle.text = getString(R.string.channels_screen_title)
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        initChannelList()
    }

    private fun initSubscriptions(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.channels.collect(){ channels ->
                    channelsAdapter.updateChannels(channels)
                }
            }
        }
    }

    private fun initChannelList() {
        channelsAdapter = ChannelAdapter()
        binding.channelsList.apply {
            addTitleElevationAnimation(binding.toolbar.root)
            adapter = channelsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

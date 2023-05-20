package com.example.basenotificationapp.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.basenotificationapp.BuildConfig
import com.example.basenotificationapp.R
import com.example.basenotificationapp.data.ChannelType.FINANCE
import com.example.basenotificationapp.data.ChannelType.GENERAL
import com.example.basenotificationapp.data.ChannelType.GOSSIP
import com.example.basenotificationapp.data.ChannelType.SPORTS
import com.example.basenotificationapp.data.NotificationState
import com.example.basenotificationapp.data.PriorityType
import com.example.basenotificationapp.databinding.FragmentCreateNotificationBinding
import com.example.basenotificationapp.domain.findIndex
import com.example.basenotificationapp.ui.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateNotificationFragment : Fragment() {

    private var _binding: FragmentCreateNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadState()
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        binding.toolbar.apply {
            screenTitle.text = getString(R.string.summary_screen_title)
            backButton.apply {
                isVisible = true
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

        binding.applyBtn.setOnClickListener {
            viewModel.applyState(
                NotificationState(
                    title = binding.fieldTitle.text.toString(),
                    description = binding.fieldDescription.text.toString(),
                    channel = createChannelByIndex(binding.channelSpinner.selectedItemPosition),
                    priority = getPriority()
                )
            )
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getPriority(): PriorityType {
        return when(binding.prioritySpinner.selectedItemPosition){
            0 -> PriorityType.DEFAULT
            1 -> PriorityType.HIGH
            2 -> PriorityType.LOW
            else -> PriorityType.DEFAULT
        }
    }

    private fun initSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }

    private fun loadState() {
        viewModel.notificationState.value?.let { notification ->
            binding.fieldTitle.setText(notification.title)
            binding.fieldDescription.setText(notification.description)
            binding.channelSpinner.setSelection(
                findIndex(
                    requireContext().resources.getStringArray(
                        R.array.notification_channels_names
                    ), notification.channel.name
                )
            )
        }?: prefillFields()
    }

    private fun prefillFields(){
        if (BuildConfig.AUTO_FILL_FIELDS){
            binding.fieldTitle.setText("Notification Title")
            binding.fieldDescription.setText("This is description")
        }
    }

    private fun createChannelByIndex(index: Int) =
        when (index) {
            0 -> GENERAL
            1 -> GOSSIP
            2 -> SPORTS
            3 -> FINANCE
            else -> {
                GENERAL
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

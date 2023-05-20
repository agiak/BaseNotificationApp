package com.example.basenotificationapp.ui.summary

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.basenotificationapp.R
import com.example.basenotificationapp.databinding.FragmentSummaryBinding
import com.example.basenotificationapp.ui.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by activityViewModels()

    private var hasNotificationsPermission = false

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationsPermission = granted
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {

        binding.resetBtn.setOnClickListener {
            viewModel.clearState()
        }

        binding.toolbar.apply {
            screenTitle.text = getString(R.string.summary_screen_title)
            backButton.isVisible = false
        }
        binding.showNotificationBtn.setOnClickListener {
            showNotification()
        }

        binding.createNotificationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_summary_to_nav_create_notification)
        }

        binding.channels.setOnClickListener {
            findNavController().navigate(R.id.action_nav_summary_to_nav_channels)
        }
    }

    private fun initSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationState.collect { notification ->

                    binding.sampleGroup.isVisible = notification != null

                    binding.notificationTitle.text = notification?.title
                    binding.notificationDesc.text = notification?.description

                    binding.createNotificationBtn.text =
                        if (notification != null) getString(R.string.summary_edit_notification) else getString(
                            R.string.summary_create_notification
                        )

                    notification?.channel?.let { channel ->
                        binding.selectedChannel.apply {
                            text = channel.name
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    channel.colorId
                                )
                            )
                        }
                        binding.notificationCard.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                channel.colorId
                            )
                        )
                        binding.selectedPriority.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                channel.colorId
                            )
                        )
                    }
                    binding.selectedPriority.text = notification?.priority?.description
                }
            }
        }
    }

    private fun showNotification() {

        if (viewModel.notificationState.value == null){
            Toast.makeText(requireContext(),"Please create a notification", Toast.LENGTH_LONG).show()
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            displayNotification()
        } else {
            if (hasNotificationsPermission) {
                displayNotification()
            } else {
                pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun displayNotification(){
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            1,
            viewModel.getNotification(requireContext().applicationContext)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

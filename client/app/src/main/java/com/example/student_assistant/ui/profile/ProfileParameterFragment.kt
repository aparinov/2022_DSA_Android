package com.example.student_assistant.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.student_assistant.App
import com.example.student_assistant.databinding.FragmentParametersBinding
import com.example.student_assistant.ui.project.ProjectParameterUI
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileParameterFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null

    val binding get() = _binding!!
    val viewModel: ProfileViewModel by activityViewModels {
        (requireContext().applicationContext as App).getApplicationComponent().viewModelFactory()
    }

    @Inject
    lateinit var ui: ProfileParameterUI

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.apply {
            navigate()
            setupHandlers()
            observeViewModel()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
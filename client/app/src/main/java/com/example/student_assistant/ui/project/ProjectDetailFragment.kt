package com.example.student_assistant.ui.project

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navArgs
import com.example.student_assistant.App
import com.example.student_assistant.databinding.FragmentDetailsBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProjectDetailFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    val binding get() = _binding!!
    val viewModel: ProjectViewModel by activityViewModels {
        (requireActivity().applicationContext as App).getApplicationComponent().viewModelFactory()
    }

    @Inject
    lateinit var ui: ProjectDetailUI

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.apply {
            load()
            setupRecycler()
            observe()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
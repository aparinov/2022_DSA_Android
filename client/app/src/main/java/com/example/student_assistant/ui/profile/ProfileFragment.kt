package com.example.student_assistant.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.student_assistant.App
import com.example.student_assistant.R
import com.example.student_assistant.databinding.FragmentMainBinding
import com.example.student_assistant.databinding.FragmentProfileBinding
import com.example.student_assistant.ui.main.MainViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!
    val viewModel: ProfileViewModel by activityViewModels {
        (requireActivity().applicationContext as App).getApplicationComponent().viewModelFactory()
    }

    @Inject
    lateinit var ui: ProfileUI

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.apply {
            setup()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
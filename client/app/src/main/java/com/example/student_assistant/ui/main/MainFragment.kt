package com.example.student_assistant.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.student_assistant.App
import com.example.student_assistant.R
import com.example.student_assistant.databinding.FragmentMainBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    val binding get() = _binding!!
    val viewModel: MainViewModel by activityViewModels {
        (requireActivity().applicationContext as App).getApplicationComponent().viewModelFactory()
    }

    @Inject lateinit var ui: MainUI

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.apply {
            navigate()
            setupAdapter()
            setupHandlers()
            observeViewModel()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIfAuthorized()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
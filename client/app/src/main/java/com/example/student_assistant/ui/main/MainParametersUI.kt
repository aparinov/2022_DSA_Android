package com.example.student_assistant.ui.main

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.R
import com.example.student_assistant.ui.parameter.adapter.ParameterAdapter
import javax.inject.Inject

class MainParametersUI @Inject constructor(
    private val fragment: MainParametersFragment,
    private val adapter: ParameterAdapter,
) {

    fun setupHandlers() {
        adapter.onItemClick = { it ->
            fragment.viewModel.setChosenItem(it)
        }
        fragment.binding.apply {
            paramRv.adapter = adapter
            paramRv.layoutManager = LinearLayoutManager(fragment.requireContext())
            paramReset.setOnClickListener {
                fragment.viewModel.reset()
            }
            paramApply.setOnClickListener {
                fragment.findNavController().popBackStack()
            }
            paramTagsRb.visibility = View.GONE
            paramRg.setOnCheckedChangeListener { _, i ->
                fragment.viewModel.setPPage(i)
            }
        }
    }

    fun observeViewModel() {
        fragment.viewModel.apply {
            parameters.observe(fragment.viewLifecycleOwner) {
                val page = pPage.value
                val parameter = if (page == R.id.param_project_status_rb) {
                    it[0]
                } else if (page == R.id.param_rec_status_rb) {
                    it[1]
                } else {
                    throw IllegalStateException()
                }
                adapter.setParameter(parameter)
            }
            pPage.observe(fragment.viewLifecycleOwner) {
                if (fragment.binding.paramRg.checkedRadioButtonId != it) {
                    fragment.binding.paramRg.check(it)
                }
                val parameters = parameters.value
                if (parameters != null) {
                    val parameter = if (it == R.id.param_project_status_rb) {
                        parameters[0]
                    } else if (it == R.id.param_rec_status_rb) {
                        parameters[1]
                    } else {
                        throw IllegalStateException()
                    }
                    adapter.setParameter(parameter)
                }
            }
        }
    }
}
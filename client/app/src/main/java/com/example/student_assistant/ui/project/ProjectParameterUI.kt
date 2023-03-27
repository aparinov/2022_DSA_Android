package com.example.student_assistant.ui.project

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.R
import com.example.student_assistant.ui.parameter.adapter.ParameterAdapter
import javax.inject.Inject

class ProjectParameterUI @Inject constructor(
    private val fragment: ProjectParameterFragment,
    private val adapter: ParameterAdapter,
) {


    fun navigate() {
        val navController = fragment.findNavController()
        fragment.binding.ivBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    fun setupHandlers() {
        adapter.onItemClick = { it ->
            fragment.viewModel.setChosenItem(it)
        }
        fragment.binding.apply {
            paramRv.adapter = adapter
            paramRv.layoutManager = LinearLayoutManager(fragment.requireContext())
            paramRg.setOnCheckedChangeListener { _, i ->
                fragment.viewModel.setPage(i)
            }
            paramReset.setOnClickListener {
                fragment.viewModel.reset()
            }
            paramApply.setOnClickListener {
                fragment.findNavController().popBackStack()
            }
        }
    }

    fun observeViewModel() {
        val page = fragment.arguments?.getInt("page") ?: throw IllegalStateException()
        fragment.viewModel.apply {
            setPage(page)
            message.observe(fragment.viewLifecycleOwner) {
                Toast.makeText(fragment.requireContext(), it, Toast.LENGTH_LONG).show()
            }
            this.page.observe(fragment.viewLifecycleOwner) {
                if (fragment.binding.paramRg.checkedRadioButtonId != it) {
                    fragment.binding.paramRg.check(it)
                }
                val parameters = this.parameters.value
                val parameter = if (it == R.id.param_project_status_rb) {
                    parameters?.get(0)
                } else if (it == R.id.param_rec_status_rb) {
                    parameters?.get(1)
                } else if (it == R.id.param_tags_rb) {
                    parameters?.get(2)
                } else {
                    throw IllegalStateException()
                }
                adapter.setParameter(parameter!!)
            }
            parameters.observe(fragment.viewLifecycleOwner) {
                val page1 = this.page.value
                val parameter = if (page1 == R.id.param_project_status_rb) {
                    it[0]
                } else if (page1 == R.id.param_rec_status_rb) {
                    it[1]
                } else if (page1 == R.id.param_tags_rb) {
                    it[2]
                } else {
                    throw IllegalStateException()
                }
                adapter.setParameter(parameter)
            }
        }
    }
}
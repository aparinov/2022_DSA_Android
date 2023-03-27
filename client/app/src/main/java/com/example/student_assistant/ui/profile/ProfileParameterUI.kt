package com.example.student_assistant.ui.profile

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.ui.parameter.adapter.ParameterAdapter
import javax.inject.Inject

class ProfileParameterUI @Inject constructor(
    private val fragment: ProfileParameterFragment,
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
            paramReset.setOnClickListener {
                fragment.viewModel.reset()
            }
            paramApply.setOnClickListener {
                fragment.findNavController().popBackStack()
            }
            paramRecStatusRb.visibility = View.GONE
            paramProjectStatusRb.visibility = View.GONE
        }
    }

    fun observeViewModel() {
        fragment.viewModel.apply {
            message.observe(fragment.viewLifecycleOwner) {
                Toast.makeText(fragment.requireContext(), it, Toast.LENGTH_LONG).show()
            }
            this.page.observe(fragment.viewLifecycleOwner) {
                if (fragment.binding.paramRg.checkedRadioButtonId != it) {
                    fragment.binding.paramRg.check(it)
                }
                val parameter = this.parameters.value?.get(0)
                adapter.setParameter(parameter!!)
            }
            parameters.observe(fragment.viewLifecycleOwner) {
                adapter.setParameter(it[0])
            }
        }
    }
}
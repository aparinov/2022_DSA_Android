package com.example.student_assistant.ui.project

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.ui.project.adapter.ProjectParametersAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class ProjectEditUI @Inject constructor(
    private val fragment: ProjectEditFragment,
    private val adapter: ProjectParametersAdapter,
) {

    private val formatter = SimpleDateFormat("dd.MM.yyyy",  Locale("ru"))

    fun navigate() {
        fragment.binding.apply {
            ivClose.setOnClickListener {
                fragment.requireActivity().finish()
            }
        }
    }

    fun setupHandlers() {
        fragment.binding.apply {
            projectEditNumberValue.max = 10
            projectEditNumberValue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    return
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    return
                }

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    projectEditTvNumberValue.text = progress.toString()
                }
            })
            projectBtnSave.setOnClickListener {
                fragment.viewModel.setProject(
                    projectEditName.text.toString(),
                    projectEditDesc.text.toString(),
                    projectEditNumberValue.progress,
                    projectEditRecTo.text.toString(),
                    projectEditFrom.text.toString(),
                    projectEditTo.text.toString(),
                )
            }
            projectBtnDelete.setOnClickListener {
                fragment.viewModel.deleteProject()
            }
            setupDatePicker(projectEditFrom)
            setupDatePicker(projectEditTo)
            setupDatePicker(projectEditRecTo)
        }
    }

    private fun setupDatePicker(editText: EditText) {
        editText.setOnClickListener {
            val dateAndTime = Calendar.getInstance()
            val d = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                val date = Date(Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DATE, d)
                }.time.time)
                editText.setText(formatter.format(date))
            }
            DatePickerDialog(
                fragment.requireContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH),
            ).show()
        }
    }

    fun setupViewModel() {
        fragment.viewModel.apply {
            setId()
            id.observe(fragment.viewLifecycleOwner) {
                if (it != null && it != -1) {
                    fragment.binding.apply {
                        projectEditNumber.visibility = View.GONE
                        projectEditNumberValue.visibility = View.GONE
                        projectEditTvNumberValue.visibility = View.GONE
                    }
                }
            }
            project.observe(fragment.viewLifecycleOwner) {
                fragment.binding.apply {
                    if (it != null) {
                        projectEditName.setText(it.title)
                        projectEditDesc.setText(it.description)
                        projectEditFrom.setText(it.plannedStartOfWork)
                        projectEditTo.setText(it.plannedFinishOfWork)
                        projectEditRecTo.setText(it.applicationsDeadline)
                    } else {
                        val date = formatter.format(Date())
                        projectEditFrom.setText(date)
                        projectEditTo.setText(date)
                        projectEditRecTo.setText(date)
                    }
                }
            }
            updated.observe(fragment.viewLifecycleOwner) {
                if (it) {
                    fragment.requireActivity().finish()
                }
            }
            message.observe(fragment.viewLifecycleOwner) {
                Toast.makeText(fragment.requireContext(), it, Toast.LENGTH_LONG).show()
            }
            parameters.observe(fragment.viewLifecycleOwner) {
                fragment.binding.apply {
                    adapter.submitList(it)
                    adapter.onItemClick = {
                        fragment.findNavController().navigate(ProjectEditFragmentDirections.actionProjectEditFragmentToProjectParameterFragment(it.page))
                    }
                    projectRv.adapter = adapter
                    projectRv.layoutManager = LinearLayoutManager(fragment.requireContext())
                }
            }
        }
    }
}
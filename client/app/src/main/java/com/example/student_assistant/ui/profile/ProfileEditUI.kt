package com.example.student_assistant.ui.profile

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.ui.project.ProjectEditFragmentDirections
import com.example.student_assistant.ui.project.adapter.ProjectParametersAdapter
import javax.inject.Inject

class ProfileEditUI @Inject constructor(
    private val fragment: ProfileEditFragment,
    private val adapter: ProjectParametersAdapter,
) {
    
    fun setup() {
        fragment.viewModel.apply {
            message.observe(fragment.viewLifecycleOwner) {
                Toast.makeText(fragment.requireContext(), it, Toast.LENGTH_LONG).show()
            }
            isUpdated.observe(fragment.viewLifecycleOwner) {
                fragment.findNavController().popBackStack()
            }
            isLoading.observe(fragment.viewLifecycleOwner) {
                fragment.binding.profileEditPb.visibility = if (it) View.VISIBLE else View.GONE
            }
            parameters.observe(fragment.viewLifecycleOwner) {
                adapter.submitList(it)
            }
            user.observe(fragment.viewLifecycleOwner) {
                fragment.binding.apply {
                    val nameSpl = it.name.split(' ')
                    profileEditName.setText(nameSpl[0])
                    profileEditSurname.setText(nameSpl[1])
                    profileEditDesc.setText(it.bio)
                }
            }
        }

        fragment.binding.apply {
            profileEditCancel.setOnClickListener {
                fragment.findNavController().popBackStack()
            }
            profileEditIvBack.setOnClickListener {
                fragment.findNavController().popBackStack()
            }
            profileEditSave.setOnClickListener {
                fragment.viewModel.update(
                    fragment.binding.profileEditName.text.toString(),
                    fragment.binding.profileEditSurname.text.toString(),
                    fragment.binding.profileEditDesc.text.toString(),
                )
            }
            adapter.onItemClick = {
                fragment.findNavController().navigate(ProfileEditFragmentDirections.actionProfileEditFragmentToProfileParameterFragment())
            }
            profileEditRv.adapter = adapter
            profileEditRv.layoutManager = LinearLayoutManager(fragment.requireContext())
        }
    }
}
package com.example.student_assistant.ui.profile

import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_assistant.ui.project.adapter.TagAdapter
import javax.inject.Inject

class ProfileUI @Inject constructor(
    private val fragment: ProfileFragment,
    private val adapter: TagAdapter,
) {

    fun setup() {
        fragment.binding.apply {
            profileBtnEdit.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
                fragment.findNavController().navigate(action)
            }
            ivBack.setOnClickListener {
                fragment.requireActivity().finish()
            }
            ivExit.setOnClickListener {
                fragment.viewModel.logout()
                fragment.requireActivity().finish()
            }
            profileRv.adapter = adapter
            profileRv.layoutManager = LinearLayoutManager(fragment.requireContext(), RecyclerView.HORIZONTAL, false)
        }

        fragment.viewModel.apply {
            loadUser()
            user.observe(fragment.viewLifecycleOwner) {
                fragment.binding.profileDesc.text = it.bio
                fragment.binding.profileName.text = it.name
                fragment.binding.profileContacts.text = it.contacts
                adapter.submitList(it.tags)
            }
            message.observe(fragment.viewLifecycleOwner) {
                Toast.makeText(fragment.requireContext(), it, Toast.LENGTH_LONG).show()
            }
            isLoading.observe(fragment.viewLifecycleOwner) {
                fragment.binding.profilePb.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }
}
package com.example.student_assistant.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.student_assistant.App
import com.example.student_assistant.databinding.FragmentRegisterBinding
import dagger.android.support.AndroidSupportInjection

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    val binding get() = _binding!!
    val viewModel: RegistrationViewModel by viewModels {
        (requireContext().applicationContext as App).getApplicationComponent().viewModelFactory()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            message.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
            isRegistered.observe(viewLifecycleOwner) {
                if (it) {
                    binding.apply {
                        regMail.visibility = View.GONE
                        regName.visibility = View.GONE
                        regSurname.visibility = View.GONE
                        regPassword.visibility = View.GONE
                        regCode.visibility = View.VISIBLE
                    }
                }
            }
            isVerified.observe(viewLifecycleOwner) {
                if (it) {
                    findNavController().popBackStack()
                }
            }
        }

        binding.apply {
            btnOk.setOnClickListener {
                viewModel.register(
                    regMail.text.toString(),
                    regName.text.toString(),
                    regSurname.text.toString(),
                    regPassword.text.toString(),
                )
                val code = regCode.text.toString()
                if (code.isNotBlank()) {
                    viewModel.verify(code)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
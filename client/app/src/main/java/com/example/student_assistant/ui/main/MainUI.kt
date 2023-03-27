package com.example.student_assistant.ui.main

import android.app.SearchManager
import android.content.Context
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_assistant.R
import com.example.student_assistant.ui.main.adapter.CardAdapter
import com.example.student_assistant.util.EnumUtil
import javax.inject.Inject


class MainUI @Inject constructor(
    private val fragment: MainFragment,
    private val adapter: CardAdapter,
) {

    fun navigate() {
        val navController = fragment.findNavController()
        fragment.binding.apply {
            mainIbPlus.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToProjectActivity(-1)
                navController.navigate(action)
            }
            mainIvMore.setOnClickListener {
                val action = MainFragmentDirections.actionMainFragmentToProfileActivity()
                navController.navigate(action)
            }
            adapter.onItemClick = {
                val action =
                    MainFragmentDirections.actionMainFragmentToProjectActivity(it.id)
                navController.navigate(action)
            }
        }
    }

    fun setupAdapter() {
        fragment.binding.apply {
            mainRv.adapter = adapter
            mainRv.layoutManager = LinearLayoutManager(fragment.requireContext())
        }
    }

    fun setupHandlers() {
        fragment.binding.apply {
            val searchManager = fragment.requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
            mainEtSearch.setSearchableInfo(searchManager.getSearchableInfo(fragment.requireActivity().componentName))
            mainEtSearch.setOnSearchClickListener {
                fragment.viewModel.setIsSearching(true)
                mainAbl.setExpanded(false)
            }
            mainEtSearch.setOnCloseListener {
                fragment.viewModel.setIsSearching(false)
                return@setOnCloseListener false
            }
            mainRgTabs.setOnCheckedChangeListener { _, i ->
                fragment.viewModel.setPage(i)
            }
            mainIbParam.setOnClickListener {
                fragment.findNavController().navigate(MainFragmentDirections.actionMainFragmentToMainParametersFragment())
            }
            mainSrl.setOnRefreshListener {
                fragment.viewModel.setPage()
            }
        }
    }

    fun observeViewModel() {
        fragment.viewModel.apply {
            isAuthorized.observe(fragment.viewLifecycleOwner) {
                if (!it) {
                    fragment.requireActivity().finish()
                }
            }
            isSearching.observe(fragment.viewLifecycleOwner) {
                fragment.binding.apply {
                    mainRgTabs.visibility = if (it) View.GONE else View.VISIBLE
                    mainIbPlus.visibility = if (it) View.GONE else View.VISIBLE
                    mainRgTabs.visibility = if (it) View.GONE else View.VISIBLE
                    mainIvMore.visibility = if (it) View.GONE else View.VISIBLE
                    mainTitle.visibility = if (it) View.GONE else View.VISIBLE
                    mainAbl.setExpanded(!it)
                }
            }
            cards.observe(fragment.viewLifecycleOwner) { items -> adapter.submitList(items) }
            page.observe(fragment.viewLifecycleOwner) {
                if (fragment.binding.mainRgTabs.checkedRadioButtonId != it) {
                    fragment.binding.mainRgTabs.check(it)
                }
            }
            isLoading.observe(fragment.viewLifecycleOwner) {
                fragment.binding.mainSrl.isRefreshing = it
            }
        }
        EnumUtil.query.observe(fragment.viewLifecycleOwner) { fragment.viewModel.setQuery(it) }
    }
}
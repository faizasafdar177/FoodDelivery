package com.example.meituanfood.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meituanfood.adapter.RestaurantAdapter
import com.example.meituanfood.databinding.FragmentSearchBinding
import com.example.meituanfood.ui.restaurant.RestaurantDetailActivity
import com.example.meituanfood.viewmodel.SearchViewModel
import com.google.android.material.chip.Chip

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var resultsAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupObservers()
        setupClickListeners()
        setupHotKeywords()
    }

    private fun setupAdapters() {
        resultsAdapter = RestaurantAdapter { restaurant ->
            val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
            intent.putExtra("restaurant_id", restaurant.id)
            startActivity(intent)
        }
        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.results.observe(viewLifecycleOwner) { results ->
            resultsAdapter.submitList(results)
            binding.rvResults.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
            binding.layoutDefault.visibility = View.GONE
        }

        viewModel.isSearching.observe(viewLifecycleOwner) { searching ->
            binding.progressBar.visibility = if (searching) View.VISIBLE else View.GONE
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { empty ->
            if (empty) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvResults.visibility = View.GONE
            }
        }

        viewModel.recentSearches.observe(viewLifecycleOwner) { recent ->
            updateRecentChips(recent)
        }
    }

    private fun setupClickListeners() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        binding.tvClearRecent.setOnClickListener {
            viewModel.clearRecent()
        }

        binding.ivBack.setOnClickListener {
            binding.etSearch.text?.clear()
            viewModel.clearResults()
            binding.layoutDefault.visibility = View.VISIBLE
            binding.rvResults.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
            hideKeyboard()
        }
    }

    private fun setupHotKeywords() {
        viewModel.hotKeywords.forEach { keyword ->
            val chip = Chip(requireContext()).apply {
                text = keyword
                isClickable = true
                setOnClickListener {
                    binding.etSearch.setText(keyword)
                    viewModel.search(keyword)
                }
            }
            binding.chipGroupHot.addView(chip)
        }
    }

    private fun updateRecentChips(recent: List<String>) {
        binding.chipGroupRecent.removeAllViews()
        recent.forEach { keyword ->
            val chip = Chip(requireContext()).apply {
                text = keyword
                isClickable = true
                setOnClickListener {
                    binding.etSearch.setText(keyword)
                    viewModel.search(keyword)
                }
            }
            binding.chipGroupRecent.addView(chip)
        }
        binding.layoutRecent.visibility = if (recent.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.search(query)
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

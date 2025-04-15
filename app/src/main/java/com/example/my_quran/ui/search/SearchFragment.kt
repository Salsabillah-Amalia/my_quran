package com.example.my_quran.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_quran.data.model.Resource
import com.example.my_quran.databinding.FragmentSearchBinding
import com.example.my_quran.ui.surah.AyahAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var ayahAdapter: AyahAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults
                .debounce(300)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.textViewNoResults.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE

                            if (result.data.isEmpty()) {
                                binding.textViewNoResults.visibility = View.VISIBLE
                                binding.recyclerViewSearchResults.visibility = View.GONE
                            } else {
                                binding.textViewNoResults.visibility = View.GONE
                                binding.recyclerViewSearchResults.visibility = View.VISIBLE
                                ayahAdapter.submitList(result.data)
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.textViewNoResults.visibility = View.VISIBLE
                            binding.textViewNoResults.text = result.message
                            binding.recyclerViewSearchResults.visibility = View.GONE
                        }
                    }
                }
        }
    }

    private fun setupRecyclerView() {
        ayahAdapter = AyahAdapter()
        binding.recyclerViewSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ayahAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searchQuran(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    ayahAdapter.submitList(emptyList())
                    binding.textViewNoResults.visibility = View.GONE
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
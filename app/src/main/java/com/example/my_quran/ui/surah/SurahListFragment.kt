package com.example.my_quran.ui.surah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_quran.data.model.Resource
import com.example.my_quran.databinding.FragmentSurahListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SurahListFragment : Fragment() {

    private var _binding: FragmentSurahListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahViewModel by viewModels()
    private lateinit var surahAdapter: SurahAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeSurahs()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshSurahs()
        }
    }

    private fun setupRecyclerView() {
        surahAdapter = SurahAdapter { surah ->
            val action = SurahListFragmentDirections.actionNavigationHomeToSurahDetailFragment(
                surah.number,
                surah.englishName
            )
            findNavController().navigate(action)
        }

        binding.recyclerViewSurah.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = surahAdapter
        }
    }

    private fun observeSurahs() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.surahs.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        surahAdapter.submitList(result.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        // Show error message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
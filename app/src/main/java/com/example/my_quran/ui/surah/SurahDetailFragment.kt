package com.example.my_quran.ui.surah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_quran.data.model.Resource
import com.example.my_quran.databinding.FragmentSurahDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SurahDetailFragment : Fragment() {

    private var _binding: FragmentSurahDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahDetailViewModel by viewModels()
    private lateinit var ayahAdapter: AyahAdapter
    private val args: SurahDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.loadSurahDetail(args.surahNumber)

        observeAyahs()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAyahs(args.surahNumber)
        }
    }

    private fun setupRecyclerView() {
        ayahAdapter = AyahAdapter()
        binding.recyclerViewAyah.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ayahAdapter
        }
    }

    private fun observeAyahs() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ayahs.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                        ayahAdapter.submitList(result.data)
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

package com.example.my_quran.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_quran.databinding.FragmentBookmarkBinding
import com.example.my_quran.ui.surah.AyahAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var ayahAdapter: AyahAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeBookmarks()

        binding.buttonDailyAyah.setOnClickListener {
            viewModel.getRandomAyah()
        }
    }

    private fun setupRecyclerView() {
        ayahAdapter = AyahAdapter()
        binding.recyclerViewBookmarks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ayahAdapter
        }
    }

    private fun observeBookmarks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarks.collectLatest { bookmarks ->
                if (bookmarks.isEmpty()) {
                    binding.textViewNoBookmarks.visibility = View.VISIBLE
                    binding.recyclerViewBookmarks.visibility = View.GONE
                } else {
                    binding.textViewNoBookmarks.visibility = View.GONE
                    binding.recyclerViewBookmarks.visibility = View.VISIBLE
                    ayahAdapter.submitList(bookmarks)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.randomAyah.collectLatest { ayahResource ->
                binding.cardDailyAyah.visibility = if (ayahResource != null) View.VISIBLE else View.GONE
                ayahResource?.let { ayah ->
                    binding.textViewDailyAyahArabic.text = ayah.text
                    binding.textViewDailyAyahTranslation.text = ayah.translation
                    binding.textViewDailyAyahReference.text = "Surah ${ayah.surahNumber}, Ayah ${ayah.numberInSurah}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
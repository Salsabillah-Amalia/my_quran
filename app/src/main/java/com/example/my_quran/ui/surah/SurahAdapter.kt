package com.example.my_quran.ui.surah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.my_quran.data.model.Surah
import com.example.my_quran.databinding.ItemSurahBinding
import com.example.my_quran.R

class SurahAdapter(private val onItemClick: (Surah) -> Unit) :
    ListAdapter<Surah, SurahAdapter.SurahViewHolder>(SurahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val binding = ItemSurahBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SurahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surah = getItem(position)
        holder.bind(surah)
    }

    inner class SurahViewHolder(private val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition  // ✅ Ganti bindingAdapterPosition -> adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(surah: Surah) {
            binding.textViewNumber.text = surah.number.toString()

            binding.textViewArabicName.text = surah.name
            binding.textViewEnglishName.text = surah.englishName
            binding.textViewTranslation.text = surah.englishNameTranslation

            // ✅ Gunakan context dan string resource untuk translation
            val context = binding.root.context
            binding.textViewAyahCount.text =
                context.getString(R.string.ayah_count_format, surah.numberOfAyahs)

            binding.textViewRevelationType.text = surah.revelationType
        }
    }

    private class SurahDiffCallback : DiffUtil.ItemCallback<Surah>() {
        override fun areItemsTheSame(oldItem: Surah, newItem: Surah): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Surah, newItem: Surah): Boolean {
            return oldItem == newItem
        }
    }
}

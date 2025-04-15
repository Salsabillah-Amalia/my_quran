package com.example.my_quran.ui.surah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.my_quran.data.model.Ayah
import com.example.my_quran.databinding.ItemAyahBinding

class AyahAdapter : ListAdapter<Ayah, AyahAdapter.AyahViewHolder>(AyahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        val binding = ItemAyahBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AyahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        val ayah = getItem(position)
        holder.bind(ayah)
    }

    class AyahViewHolder(private val binding: ItemAyahBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ayah: Ayah) {
            binding.textViewAyahNumber.text = ayah.numberInSurah.toString()
            binding.textViewArabicText.text = ayah.text
            binding.textViewTranslation.text = ayah.translation
        }
    }

    private class AyahDiffCallback : DiffUtil.ItemCallback<Ayah>() {
        override fun areItemsTheSame(oldItem: Ayah, newItem: Ayah): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ayah, newItem: Ayah): Boolean {
            return oldItem == newItem
        }
    }
}
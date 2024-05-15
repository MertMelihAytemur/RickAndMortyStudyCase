package com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.loadImage
import com.vmlmedia.rickandmortystudycase.databinding.ItemCharacterBinding
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.model.CharacterStatusType

class CharactersListAdapter(

) : ListAdapter<CharacterUiModel, CharactersListAdapter.ViewHolder>(CharacterListDiffUtil()) {

    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CharacterUiModel) = with(binding) {
            tvCharacterName.text = item.name
            tvStatus.text = item.status
            tvLastLocation.text = item.location

            when (item.status) {
                CharacterStatusType.ALIVE.status -> ivStatus.setImageResource(R.drawable.ic_active)
                CharacterStatusType.DEAD.status -> ivStatus.setImageResource(R.drawable.ic_passive)
                else -> ivStatus.setImageResource(R.drawable.ic_passive)
            }

            if (item.image.isNotEmpty()) {
                ivCharacterImage.loadImage(item.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class CharacterListDiffUtil : DiffUtil.ItemCallback<CharacterUiModel>() {
        override fun areItemsTheSame(
            oldItem: CharacterUiModel,
            newItem: CharacterUiModel
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CharacterUiModel,
            newItem: CharacterUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
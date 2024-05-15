package com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.loadImage
import com.vmlmedia.rickandmortystudycase.databinding.ItemCharacterBinding
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.model.CharacterStatusType

class CharactersArrayAdapter(
    context: Context,
    private val characters: List<CharacterUiModel>,
    private val onDeclineClick : () -> Unit,
    private val onAcceptClick : () -> Unit
) : ArrayAdapter<CharacterUiModel>(context, 0, characters) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemCharacterBinding = if (convertView == null) {
            // Inflate the layout if convertView is null
            val newBinding = ItemCharacterBinding.inflate(LayoutInflater.from(context), parent, false)
            newBinding.root.tag = newBinding // Store binding in tag for re-use
            newBinding
        } else {
            // Re-use the binding from tag
            convertView.tag as ItemCharacterBinding
        }

        val character = getItem(position)
        character?.let {
            bindCharacter(binding, it)
        }

        binding.btnAccept.setOnClickListener {
            onAcceptClick()
        }

        binding.btnDecline.setOnClickListener {
            onDeclineClick()
        }

        return binding.root
    }

    private fun bindCharacter(binding: ItemCharacterBinding, character: CharacterUiModel) {
        with(binding) {
            tvCharacterName.text = character.name
            tvStatus.text = character.status
            tvLastLocation.text = character.location

            when (character.status) {
                CharacterStatusType.ALIVE.status -> ivStatus.setImageResource(R.drawable.ic_active)
                CharacterStatusType.DEAD.status -> ivStatus.setImageResource(R.drawable.ic_passive)
                else -> ivStatus.setImageResource(R.drawable.ic_passive)
            }

            if (character.image.isNotEmpty()) {
                ivCharacterImage.loadImage(character.image)
            }
        }
    }

}
package com.vmlmedia.rickandmortystudycase.feature.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.fadeVisibility
import com.vmlmedia.rickandmortystudycase.core.ui.BaseFragment
import com.vmlmedia.rickandmortystudycase.common.custom.SwipeCardAdapterView
import com.vmlmedia.rickandmortystudycase.databinding.FragmentHomeBinding
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.GetCharacterListApiState
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.CharactersArrayAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels()

    private lateinit var charactersAdapter: CharactersArrayAdapter

    private var characterList : MutableList<CharacterUiModel> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacters()
        observeEvents()
        setupFlingListener()
    }

    private fun observeEvents() {
        collectPageState(viewModel.pageStateFlow) {
            when (it.pageEvent) {
                HomeViewModel.PageEvent.INITIAL -> {}
                HomeViewModel.PageEvent.GET_CHARACTERS_RESPONSE_RECEIVED -> {
                    onGetCharactersResponseReceived(it.getCharacterListApiState)
                }
            }
        }
    }

    private fun setupAdapter() {
        charactersAdapter = CharactersArrayAdapter(
            requireContext(), characterList,
            ::onDeclineClick, ::onAcceptClick
        )

        binding.frame.adapter = charactersAdapter
        charactersAdapter.notifyDataSetChanged()
    }

    private fun setupFlingListener() {
        binding.frame.setFlingListener(object : SwipeCardAdapterView.OnSwipeListener {
            override fun removeFirstObjectInAdapter() {
                if (characterList.isNotEmpty()) {
                    characterList.removeAt(0)
                    charactersAdapter.notifyDataSetChanged()
                }
            }

            override fun onLeftCardExit(dataObject: Any) {
                binding.tvDecline.fadeVisibility()
            }

            override fun onRightCardExit(dataObject: Any) {
                binding.tvAccept.fadeVisibility()
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                if(characterList.isNotEmpty()){
                    viewModel.getCharacters()
                }
            }

            override fun onScroll(scrollProgressPercent: Float) {
                val view = binding.frame.selectedView ?: return
                view.findViewById<View>(R.id.item_swipe_right_indicator).alpha =
                    if (scrollProgressPercent < 0) -scrollProgressPercent else 0f
                view.findViewById<View>(R.id.item_swipe_left_indicator).alpha =
                    if (scrollProgressPercent > 0) scrollProgressPercent else 0f
            }
        })
    }


    private fun onGetCharactersResponseReceived(getCharacterListApiState: GetCharacterListApiState) {
        when (getCharacterListApiState) {
            is GetCharacterListApiState.Success -> {
                onGetCharactersSuccess(getCharacterListApiState.uiModel)
            }

            is GetCharacterListApiState.Error -> {
                handleNetworkError(getCharacterListApiState.error)
            }

            is GetCharacterListApiState.Initial -> {}
        }
    }

    private fun onGetCharactersSuccess(uiModel: CharacterListUiModel?) {
        uiModel?.let {
            viewModel.pageSize = it.pageSize
            viewModel.itemCount = it.itemCount

            characterList = it.characters.toMutableList()
            setupAdapter()
        }
    }

    private fun onDeclineClick() {
        binding.frame.getTopCardListener().selectLeft()
    }

    private fun onAcceptClick() {
        binding.frame.getTopCardListener().selectRight()
    }
}
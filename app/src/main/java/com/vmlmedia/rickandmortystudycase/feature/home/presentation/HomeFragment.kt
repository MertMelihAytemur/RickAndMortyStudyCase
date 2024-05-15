package com.vmlmedia.rickandmortystudycase.feature.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.vmlmedia.core.util.logD
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.fadeVisibility
import com.vmlmedia.rickandmortystudycase.core.ui.BaseFragment
import com.vmlmedia.rickandmortystudycase.core.ui.custom.SwipeFlingAdapterView
import com.vmlmedia.rickandmortystudycase.databinding.FragmentHomeBinding
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.GetCharacterListApiState
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.CharactersArrayAdapter
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.CharactersListAdapter
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.SwipeCardCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels()

    private lateinit var charactersAdapter: CharactersArrayAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacters()
        observeEvents()

        setupAdapter()
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
            requireContext(), viewModel.characterList,
            ::onDeclineClick, ::onAcceptClick
        )

        binding.frame.adapter = charactersAdapter
    }

    private fun setupFlingListener() {
        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                if (viewModel.characterList.isNotEmpty()) {
                    viewModel.characterList.removeAt(0)
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
                viewModel.getCharacters()
                charactersAdapter.notifyDataSetChanged()
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

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

            charactersAdapter.notifyDataSetChanged()
        }
    }

    private fun onDeclineClick() {
        binding.frame.getTopCardListener().selectLeft()
    }

    private fun onAcceptClick() {
        binding.frame.getTopCardListener().selectRight()
    }
}
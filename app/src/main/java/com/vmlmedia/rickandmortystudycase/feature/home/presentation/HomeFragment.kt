package com.vmlmedia.rickandmortystudycase.feature.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.vmlmedia.core.util.logD
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.core.ui.BaseFragment
import com.vmlmedia.rickandmortystudycase.databinding.FragmentHomeBinding
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.GetCharacterListApiState
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.CharactersListAdapter
import com.vmlmedia.rickandmortystudycase.feature.home.presentation.adapter.SwipeCardCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels()

    private val adapter: CharactersListAdapter by lazy {
        CharactersListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.getCharacters()
        observeEvents()
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

    private fun initAdapter() {
        binding.rvCharacters.adapter = adapter

        //set horizontal layout manager
        binding.rvCharacters.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val itemTouchHelper = ItemTouchHelper(SwipeCardCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvCharacters)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvCharacters)

        // Add scroll listener for infinite scroll
        binding.rvCharacters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.getCharacters()
                }
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

            adapter.submitList(viewModel.characterList.toList())
        }
    }
}
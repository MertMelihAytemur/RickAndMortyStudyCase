package com.vmlmedia.rickandmortystudycase.feature.home.presentation

import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.core.util.logD
import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.request.GetCharactersRequestDto
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.GetCharacterListApiState
import com.vmlmedia.rickandmortystudycase.feature.home.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> get() = _pageStateFlow

    var pageSize: Int = 10
    var itemCount: Int = 0

    private var currentPage: Int = 1

    fun getCharacters() {
        if (currentPage >= pageSize && currentPage != 1) {
            return
        }

        launchRequest(requestBody = {
            delay(2000)
            getCharactersUseCase.invoke(
                params = GetCharactersRequestDto(page = currentPage)
            )
        }, onSuccess = { uiModel ->
            if (!uiModel?.characters.isNullOrEmpty()) {
                currentPage++
            }

            _pageStateFlow.update {
                it.copy(
                    pageEvent = PageEvent.GET_CHARACTERS_RESPONSE_RECEIVED,
                    getCharacterListApiState = GetCharacterListApiState.Success(
                        uiModel
                    )
                )
            }
        }, onError = { uiError ->
            _pageStateFlow.update {
                it.copy(
                    pageEvent = PageEvent.GET_CHARACTERS_RESPONSE_RECEIVED,
                    getCharacterListApiState = GetCharacterListApiState.Error(uiError)
                )
            }
        })
    }


    enum class PageEvent {
        INITIAL,
        GET_CHARACTERS_RESPONSE_RECEIVED
    }

    data class PageState(
        var pageEvent: PageEvent = PageEvent.INITIAL,
        var getCharacterListApiState: GetCharacterListApiState = GetCharacterListApiState.Initial
    )
}
package com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel

import com.vmlmedia.core.domain.UiError
import com.vmlmedia.core.domain.UiModel
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel
import java.util.UUID

data class CharacterListUiModel(
    val pageSize : Int,
    val itemCount : Int,
    val characters: List<CharacterUiModel>
) : UiModel

data class CharacterUiModel(
    val name: String,
    val species: String,
    val status: String,
    val image: String,
    val location: String,
    val origin: String
) : UiModel


sealed class GetCharacterListApiState {

    data object Initial : GetCharacterListApiState()

    data class Success(val uiModel: CharacterListUiModel?) : GetCharacterListApiState()

    data class Error(val error : UiError<ApiErrorModel>) : GetCharacterListApiState()
}
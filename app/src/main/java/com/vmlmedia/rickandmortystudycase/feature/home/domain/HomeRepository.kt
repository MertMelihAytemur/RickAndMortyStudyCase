package com.vmlmedia.rickandmortystudycase.feature.home.domain

import com.vmlmedia.core.domain.UiResult
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel
import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.request.GetCharactersRequestDto
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel

interface HomeRepository {
    suspend fun getCharacters(
        params: GetCharactersRequestDto
    ): UiResult<CharacterListUiModel, ApiErrorModel>
}
package com.vmlmedia.rickandmortystudycase.feature.home.domain.usecase

import com.vmlmedia.core.domain.UiResult
import com.vmlmedia.core.domain.UseCase
import com.vmlmedia.core.domain.UseCaseParams
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel
import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.request.GetCharactersRequestDto
import com.vmlmedia.rickandmortystudycase.feature.home.domain.HomeRepository
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) : UseCase<CharacterListUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<CharacterListUiModel, ApiErrorModel> {
        return homeRepository.getCharacters(params as GetCharactersRequestDto)
    }
}
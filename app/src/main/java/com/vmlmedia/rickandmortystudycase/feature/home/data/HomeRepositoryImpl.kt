package com.vmlmedia.rickandmortystudycase.feature.home.data

import com.vmlmedia.core.data.ApiExecutor
import com.vmlmedia.core.data.remote.ApiResult
import com.vmlmedia.core.domain.UiResult
import com.vmlmedia.core.domain.parseError
import com.vmlmedia.rickandmortystudycase.core.model.ApiErrorModel
import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.request.GetCharactersRequestDto
import com.vmlmedia.rickandmortystudycase.feature.home.data.dto.response.toDomain
import com.vmlmedia.rickandmortystudycase.feature.home.data.remote.HomeApi
import com.vmlmedia.rickandmortystudycase.feature.home.domain.HomeRepository
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository, ApiExecutor {
    override suspend fun getCharacters(params: GetCharactersRequestDto): UiResult<CharacterListUiModel, ApiErrorModel> {
        val apiRequest = execute(
            call = {
                homeApi.getCharacters(params.page)
            }
        )

        return when (apiRequest) {
            is ApiResult.Success -> {
                UiResult.Success(apiRequest.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiRequest).error)
            }
        }
    }
}
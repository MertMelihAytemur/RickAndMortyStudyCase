package com.vmlmedia.rickandmortystudycase.feature.home.data.dto.request

import com.vmlmedia.core.domain.UseCaseParams

data class GetCharactersRequestDto(
    val page : Int = 1
) : UseCaseParams
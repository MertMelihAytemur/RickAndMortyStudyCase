package com.vmlmedia.rickandmortystudycase.feature.home.data.dto.response

import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterListUiModel
import com.vmlmedia.rickandmortystudycase.feature.home.domain.uimodel.CharacterUiModel

data class GetCharactersDto(
    val info: InfoDto?,
    val results: List<CharacterDto?>?
) {
    data class InfoDto(
        val count: Int?,
        val next: String?,
        val pages: Int?,
        val prev: Any?
    )

    data class CharacterDto(
        val created: String?,
        val episode: List<String?>?,
        val gender: String?,
        val id: Int?,
        val image: String?,
        val location: LocationDto?,
        val name: String?,
        val origin: OriginDto?,
        val species: String?,
        val status: String?,
        val type: String?,
        val url: String?
    ) {
        data class LocationDto(
            val name: String?,
            val url: String?
        )

        data class OriginDto(
            val name: String?,
            val url: String?
        )
    }
}

fun GetCharactersDto.toDomain(): CharacterListUiModel {
    val pageSize = this.info?.pages ?: 0
    val itemCount = this.info?.count ?: 0
    val characterDisplays = this.results?.mapNotNull { character ->
        character?.let {
            CharacterUiModel(
                name = it.name ?: "Unknown",
                species = it.species ?: "Unknown",
                status = it.status ?: "Unknown",
                image = it.image ?: "",
                location = it.location?.name ?: "Unknown location",
                origin = it.origin?.name ?: "Unknown origin"
            )
        }
    } ?: emptyList()

    return CharacterListUiModel(
        pageSize = pageSize,
        itemCount = itemCount,
        characters = characterDisplays
    )
}
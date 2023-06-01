package mimsoft.io.entities.poster

object PosterMapper {
    fun toPosterTable(posterDto: PosterDto?): PosterTable? {
        return if (posterDto == null) null
        else PosterTable(
            id = posterDto.id,
            merchantId = posterDto.merchantId,
            joinPosterApiKey = posterDto.joinPosterApiKey,
            rKeeperClientId = posterDto.rKeeperClientId,
            rKeeperClientSecret = posterDto.rKeeperClientSecret,
            selected = posterDto.selected
        )
    }

    fun toPosterDto(posterTable: PosterTable?): PosterDto? {
        return if (posterTable == null) null
        else PosterDto(
            id = posterTable.id,
            merchantId = posterTable.merchantId,
            joinPosterApiKey = posterTable.joinPosterApiKey,
            rKeeperClientId = posterTable.rKeeperClientId,
            rKeeperClientSecret = posterTable.rKeeperClientSecret,
            selected = posterTable.selected
        )
    }
}
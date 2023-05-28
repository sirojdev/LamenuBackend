package mimsoft.io.entities.poster

object PosterMapper {
    fun toPosterTable(posterDto: PosterDto?): PosterTable? {
        return if (posterDto == null) null
        else PosterTable(
            id = posterDto.id,
            merchantId = posterDto.merchantId,
            joinPoserApiKey = posterDto.joinPosterApiKey,
            rKeeperClientId = posterDto.rKeeperClientId,
            rKeeperClientSecret = posterDto.rKeeperClientSecret
        )
    }

    fun toPosterDto(posterTable: PosterTable?): PosterDto? {
        return if (posterTable == null) null
        else PosterDto(
            id = posterTable.id,
            merchantId = posterTable.merchantId,
            joinPosterApiKey = posterTable.joinPoserApiKey,
            rKeeperClientId = posterTable.rKeeperClientId,
            rKeeperClientSecret = posterTable.rKeeperClientSecret
        )
    }
}
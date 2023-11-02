package mimsoft.io.features.story

import mimsoft.io.utils.TextModel

object StoryMapper {
  fun toDto(storyTable: StoryTable): StoryDto {
    return StoryDto(
      id = storyTable.id,
      merchantId = storyTable.merchantId,
      name = TextModel(uz = storyTable.nameUz, ru = storyTable.nameRu, eng = storyTable.nameEng),
      image =
        TextModel(uz = storyTable.imageUz, ru = storyTable.imageRu, eng = storyTable.imageEng),
      priority = storyTable.priority
    )
  }

  fun toTable(storyDto: StoryDto): StoryTable {
    return StoryTable(
      id = storyDto.id,
      merchantId = storyDto.merchantId,
      nameUz = storyDto.name?.uz,
      nameRu = storyDto.name?.ru,
      nameEng = storyDto.name?.eng,
      imageUz = storyDto.image?.uz,
      imageRu = storyDto.image?.ru,
      imageEng = storyDto.image?.eng,
      priority = storyDto.priority
    )
  }
}

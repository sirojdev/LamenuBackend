package mimsoft.io.utils

import java.sql.Timestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TimestampSerializer : KSerializer<Timestamp> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.LONG)

  override fun deserialize(decoder: Decoder): Timestamp {
    return Timestamp(decoder.decodeLong())
  }

  override fun serialize(encoder: Encoder, value: Timestamp) {
    encoder.encodeLong(value.time)
  }
}

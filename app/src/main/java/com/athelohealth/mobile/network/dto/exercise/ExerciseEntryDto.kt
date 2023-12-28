package com.athelohealth.mobile.network.dto.exercise

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.exercise.ExerciseEntry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

@Serializable(with = ExerciseListDtoSerializer::class)
class ExerciseListDto(val data: List<ExerciseEntryDto>)

class ExerciseEntryDto(
    val date: String,
    val duration: Float,
    val calories: Float,
    val steps: Float,
) {
    fun toExerciseEntry() = ExerciseEntry(date.toDate() ?: Date(), duration.toInt())
}

class ExerciseListDtoSerializer : KSerializer<ExerciseListDto> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("com.athelohealth.mobile.network.dto.exercise.ExerciseListDto")

    override fun deserialize(decoder: Decoder): ExerciseListDto {
        val jsonDecoder = (decoder as JsonDecoder)
        return try {
            val rootObj = jsonDecoder.decodeJsonElement().jsonObject
            val items = arrayListOf<ExerciseEntryDto>()
            rootObj.keys.forEach { date ->
                val mainObj = rootObj[date]?.jsonObject
                val duration = mainObj?.get("duration_seconds")?.jsonPrimitive?.floatOrNull
                val calories = mainObj?.get("calories")?.jsonPrimitive?.floatOrNull
                val steps = mainObj?.get("steps")?.jsonPrimitive?.floatOrNull
                items.add(ExerciseEntryDto(date, duration ?: 0f, calories ?: 0f, steps ?: 0f))
            }
            ExerciseListDto(items)
        } catch (e: Exception) {
            ExerciseListDto(emptyList())
        }
    }

    override fun serialize(encoder: Encoder, value: ExerciseListDto) {}
}
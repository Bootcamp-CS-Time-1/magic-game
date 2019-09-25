package br.com.bootcamp.magicgamecs.models.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Card(
    @PrimaryKey
    val id: String,
    val imageUrl: String?,
    val type: String = "",
    val name: String = "",
    val types: List<String> = listOf(),
    val set: String = "",
    val setName: String = ""
) : Parcelable

class StringListToGsonConverter {
    @TypeConverter
    fun stringToMeasurements(json: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        return gson.toJson(list, type)
    }

}
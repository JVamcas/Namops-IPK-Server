package com.jvmtechs.utils

import com.google.gson.*
import com.jvmtechs.utils.DateUtil.Companion._24
import javafx.scene.control.*
import java.lang.Double.parseDouble
import java.sql.Timestamp
import java.time.LocalDateTime
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class ParseUtil {

    class LocalDateTimeSerializer : JsonSerializer<LocalDateTime?> {
        override fun serialize(
            localDateTime: LocalDateTime?,
            srcType: Type?,
            context: JsonSerializationContext
        ): JsonElement? {

            return if (localDateTime == null) null
            else {
                val year = localDateTime.year
                val month = localDateTime.month
                val day = localDateTime.dayOfMonth

                val hour = localDateTime.hour
                val minute = localDateTime.minute
                val seconds = localDateTime.second
                val json =
                    "{date:{year:$year,month:$month,day:$day},time:{hour:$hour,minute:$minute,second:$seconds}}"
                JsonParser.parseString(json).asJsonObject
            }

        }
    }

    class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime?> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext
        ): LocalDateTime? {
            return if (json != null && !json.isJsonNull) {
                val date = json.asJsonObject.get("date").asJsonObject
                val time = json.asJsonObject.get("time").asJsonObject

                val year = date.get("year").asInt
                val month = date.monthIndex()
                val day = date.get("day").asInt

                val hour = time.get("hour").asInt
                val minute = time.get("minute").asInt
                val seconds = time.get("second").asInt

                LocalDateTime.of(year, month, day, hour, minute, seconds)

            } else null
        }
    }

    object ParseGson {
        var gson: Gson? = null
        private val builder = GsonBuilder()
            .apply {
                registerTypeAdapter(LocalDateTime::class.java, ParseUtil.LocalDateTimeSerializer())
                registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeDeserializer()
                )
            }

        init {
            gson = if (gson == null) builder.setPrettyPrinting().create() else gson
        }

    }

    companion object {

        /**
         * Convert a boolean value in a cell to yes if true else no string
         */
        fun <T> TableColumn<T, Boolean>.toYesNo() {
            this.setCellFactory {
                object : TableCell<T, Boolean>() {
                    override fun updateItem(item: Boolean?, empty: Boolean) {
                        super.updateItem(item, empty)
                        text = if (empty) null else if (item == true) "Yes" else "No"
                    }
                }
            }
        }

        fun <T> TableColumn<T, Timestamp>.localDateTimeFormat() {
            this.setCellFactory {
                object : TableCell<T, Timestamp>() {
                    override fun updateItem(item: Timestamp?, empty: Boolean) {
                        super.updateItem(item, empty)
                        text = if (empty) null else item?.toLocalDateTime()?._24()
                    }
                }
            }
        }

        val gson: Gson by lazy { ParseGson.gson!! }

        fun <T> T.toMap(): Map<String, Any> {
            return convert()
        }

        fun JsonObject.monthIndex(): Int {
            val value = this.get("month")
            return try {
                value.asInt
            } catch (e: NumberFormatException) {
                val date: Date = SimpleDateFormat("MMMM").parse(value.asString)
                val cal = Calendar.getInstance()
                cal.time = date
                cal[Calendar.MONTH] + 1
            }
        }


        inline fun <I, reified O> I.convert(): O {
            val json = this.toJson()
            return gson.fromJson(json, object : TypeToken<O>() {}.type)
        }

        inline fun <reified O> String.convert(): O {
            return gson.fromJson(this, O::class.java)
        }

        fun <T> String.convert(type: Type): T {
            return gson.fromJson(this, type)
        }

        fun <K> K.toJson(): String {
            return gson.toJson(this)
        }


        fun Any?.isNumber(): Boolean {
            val value = this.toString()
            return value != "null" &&
                    try {
                        parseDouble(value.replace(',', '.'))
                        true
                    } catch (e: Exception) {
                        false
                    }
        }

        fun Any?.toDouble(): Double {
            return if (!this.isNumber())
                0.0
            else {
                parseDouble(this.toString().replace(',', '.'))
            }
        }

        fun Int?.isOldId() = this != null && this > 0

        fun String?.strip(): String {
            return this?.trim() ?: ""
        }
    }
}



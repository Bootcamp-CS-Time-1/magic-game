package br.com.bootcamp.magicgamecs.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.bootcamp.magicgamecs.App
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.pojo.StringListToGsonConverter

@Database(entities = arrayOf(Card::class), version = 1, exportSchema = false)
@TypeConverters(StringListToGsonConverter::class)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        val DATABASE_NAME = "magicDB"
        var sInstance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase? {
            if (sInstance == null) {
                synchronized(this) {
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        AppDataBase.DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return sInstance
        }
    }

    abstract fun cardDao(): CardDao

}
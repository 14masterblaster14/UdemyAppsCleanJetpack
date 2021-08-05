package com.example.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1, exportSchema = false)
//  Set the version as 1. Whenever you change the schema, you'll have to increase the version number.
//  Set exportSchema to false, so as not to keep schema version history backups.
abstract class SubscriberDatabase : RoomDatabase() {

    abstract val subscriberDAO: SubscriberDAO // Connects the database to the DAO.

    companion object {   // Singleton Object
        @Volatile
        private var INSTANCE: SubscriberDatabase? = null
        fun getInstance(context: Context): SubscriberDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                    )
                        .fallbackToDestructiveMigration()   // Wipes and rebuilds instead of migrating if no Migration object.
                        .build()
                }
                return instance
            }
        }

    }
}

/**
@Volatile :-
The value of a volatile variable will never be cached, and all writes and reads will be
done to and from the main memory. This helps make sure the value of INSTANCE is always
up-to-date and the same to all execution threads. It means that changes made by one
thread to INSTANCE are visible to all other threads immediately, and you don't get a
situation where, say, two threads each update the same entity in a cache,
which would create a problem.

synchronized :-
Multiple threads can potentially ask for a database instance at the same time,
resulting in two databases instead of one. This problem is not likely to happen
in this sample app, but it's possible for a more complex app. Wrapping the code
to get the database into synchronized means that only one thread of execution
at a time can enter this block of code, which makes sure the database only gets
initialized once.

fallbackToDestructiveMigration() :-
Normally, you would have to provide a migration object with a migration strategy for
when the schema changes. A migration object is an object that defines how you take all rows
with the old schema and convert them to rows in the new schema, so that no data is lost.
Migration is beyond the scope of this codelab. A simple solution is to destroy and rebuild the database,
which means that the data is lost.
 */
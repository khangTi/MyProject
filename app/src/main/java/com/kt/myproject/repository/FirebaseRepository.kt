package com.kt.myproject.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kt.myproject.ex.parse
import com.kt.myproject.ex.toJsonObject
import com.kt.myproject.repository.data.list
import com.kt.myproject.repository.model.RealtimeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/08/27
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object FirebaseRepository {

    private val database = FirebaseDatabase.getInstance()

    private val databaseRealtime = database.getReference("realtime")

    private var listenerRealtime: ValueEventListener? = null

    @ExperimentalCoroutinesApi
    fun eventRealtime() = callbackFlow<RealtimeData> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val data = (snapshot.value as HashMap<*, *>).toJsonObject()
                        .parse(RealtimeData::class.java) ?: RealtimeData()
                    trySend(data)
                } catch (e: Exception) {
                    close(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(Exception(error.message))
            }
        }
        databaseRealtime.addValueEventListener(listener)
        awaitClose { databaseRealtime.removeEventListener(listener) }
    }

}
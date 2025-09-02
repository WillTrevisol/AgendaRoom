package br.edu.ifsp.agendaroom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Insert
    suspend fun insert(contactEntity: ContactEntity)

    @Query("SELECT * FROM contacts ORDER BY name")
    fun getAllContacts(): Flow<List<ContactEntity>>
}

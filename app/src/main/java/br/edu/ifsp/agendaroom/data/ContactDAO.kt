package br.edu.ifsp.agendaroom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Insert
    suspend fun insert(contactEntity: ContactEntity)

    @Update
    suspend fun update(contactEntity: ContactEntity)

    @Delete
    suspend fun delete(contactEntity: ContactEntity)

    @Query("SELECT * FROM contacts ORDER BY name")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id=:id")
    fun getContactById(id: Int): Flow<ContactEntity>
}

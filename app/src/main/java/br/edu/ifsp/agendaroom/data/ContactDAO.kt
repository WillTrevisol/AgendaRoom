package br.edu.ifsp.agendaroom.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ContactDAO {
    @Insert
    suspend fun insert(contactEntity: ContactEntity)
}

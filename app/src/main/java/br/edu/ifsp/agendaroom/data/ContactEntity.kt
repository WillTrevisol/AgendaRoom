package br.edu.ifsp.agendaroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifsp.agendaroom.domain.Contact

@Entity(tableName = "contacts")
data class ContactEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
    val phone:String,
    val email:String
) {
    fun toDomain() : Contact {
        return Contact(id, name, phone, email)
    }
}

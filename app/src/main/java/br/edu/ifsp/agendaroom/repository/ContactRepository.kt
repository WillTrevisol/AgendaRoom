package br.edu.ifsp.agendaroom.repository

import br.edu.ifsp.agendaroom.data.ContactDAO
import br.edu.ifsp.agendaroom.data.ContactEntity
import br.edu.ifsp.agendaroom.domain.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ContactRepository (private val contactDAO: ContactDAO) {
    suspend fun insert(contact: Contact) {
        contactDAO.insert(contact.toEntity())
    }

    suspend fun update(contact: Contact) {
        contactDAO.update(contact.toEntity())
    }

    suspend fun delete(contact: Contact) {
        contactDAO.delete(contact.toEntity())
    }

    fun getAllContacts(): Flow<List<Contact>> {
        return contactDAO.getAllContacts().map { list ->
            list.map {
                it.toDomain()
            }
        }
    }

    fun getContactById(id: Int): Flow<Contact> {
        return contactDAO.getContactById(id).filterNotNull().map {
            it.toDomain()
        }
    }
}

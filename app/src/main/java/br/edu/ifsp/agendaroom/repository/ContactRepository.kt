package br.edu.ifsp.agendaroom.repository

import br.edu.ifsp.agendaroom.data.ContactDAO
import br.edu.ifsp.agendaroom.domain.Contact

class ContactRepository (private val contactDAO: ContactDAO) {
    suspend fun insert(contact: Contact){
        contactDAO.insert(contact.toEntity())
    }
}

package br.edu.ifsp.agendaroom

import android.app.Application
import br.edu.ifsp.agendaroom.data.ContactDatabase
import br.edu.ifsp.agendaroom.repository.ContactRepository

class ContactApplication: Application() {
    val database by lazy { ContactDatabase.getDatabase(this) }
    val repository by lazy { ContactRepository(database.contactDao()) }
}
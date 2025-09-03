package br.edu.ifsp.agendaroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.ifsp.agendaroom.ContactApplication
import br.edu.ifsp.agendaroom.domain.Contact
import br.edu.ifsp.agendaroom.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    data object InsertSuccess : RegisterState()
    data object ShowLoading : RegisterState()
}

sealed class ContactDetailState {
    data object UpdateSuccess: ContactDetailState()
    data object DeleteSuccess: ContactDetailState()
    data class GetByIdSuccess(val contact: Contact): ContactDetailState()
    data object Loading: ContactDetailState()
}

sealed class ContactsListState {
    data class SearchAllSuccess(val contacts: List<Contact>): ContactsListState()
    data object Loading: ContactsListState()
    data object Empty: ContactsListState()
}

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.ShowLoading)
    val registerState = _registerState.asStateFlow()

    private val _contactDetailState =
        MutableStateFlow<ContactDetailState>(ContactDetailState.Loading)
    val contactDetailState = _contactDetailState.asStateFlow()


    private val _contactsListState = MutableStateFlow<ContactsListState>(ContactsListState.Loading)
    val contactsListState = _contactsListState.asStateFlow()

    fun insert(contactEntity: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contactEntity)
        _registerState.value = RegisterState.InsertSuccess
    }

    fun update(contactEntity: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contactEntity)
        _contactDetailState.value = ContactDetailState.UpdateSuccess
    }

    fun delete(contactEntity: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contactEntity)
        _contactDetailState.value = ContactDetailState.DeleteSuccess
    }

    fun getAllContacts() {
        viewModelScope.launch {
            repository.getAllContacts().collect { result ->
                if (result.isEmpty()) {
                    _contactsListState.value = ContactsListState.Empty
                } else {
                    _contactsListState.value = ContactsListState.SearchAllSuccess(result)
                }
            }
        }
    }

    fun getContactById(id: Int) {
        viewModelScope.launch {
            repository.getContactById(id).collect { result ->
                _contactDetailState.value = ContactDetailState.GetByIdSuccess(result)
            }
        }
    }

    companion object {
    fun contactViewModelFactory(): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(
                    extras[APPLICATION_KEY]
                )
                return ContactViewModel(
                    (application as ContactApplication).repository
                ) as T
            }
        }
    }
}

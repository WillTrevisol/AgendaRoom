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

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.ShowLoading)
    val registerState = _registerState.asStateFlow()

    fun insert(contactEntity: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contactEntity)
        _registerState.value = RegisterState.InsertSuccess
    }

    companion object {
        fun contactViewModelFactory() : ViewModelProvider.Factory =
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

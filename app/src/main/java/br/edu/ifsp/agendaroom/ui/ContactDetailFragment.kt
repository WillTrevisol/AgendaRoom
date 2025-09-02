package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.agendaroom.databinding.FragmentContactDetailBinding
import br.edu.ifsp.agendaroom.domain.Contact
import br.edu.ifsp.agendaroom.viewmodel.ContactDetailState
import br.edu.ifsp.agendaroom.viewmodel.ContactViewModel
import br.edu.ifsp.agendaroom.viewmodel.ContactsListState
import kotlinx.coroutines.launch


class ContactDetailFragment : Fragment() {

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var contact: Contact

    lateinit var nameEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var emailEditText: EditText

    val viewModel: ContactViewModel by viewModels {
        ContactViewModel.contactViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = binding.commonLayout.nameEditText
        phoneEditText = binding.commonLayout.phoneEditText
        emailEditText = binding.commonLayout.editTextEmail

        val contactId = requireArguments().getInt("contactId")
        viewModel.getContactById(contactId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactDetailState.collect {
                when(it) {
                    ContactDetailState.Loading -> { }

                    is ContactDetailState.GetByIdSuccess -> {
                        fillFields(it.contact)
                    }
                }
            }
        }
    }

    private fun fillFields(contact: Contact) {
        this.contact = contact
        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)
    }
}
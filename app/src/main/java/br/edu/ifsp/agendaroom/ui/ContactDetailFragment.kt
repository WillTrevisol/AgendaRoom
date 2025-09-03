package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.databinding.FragmentContactDetailBinding
import br.edu.ifsp.agendaroom.domain.Contact
import br.edu.ifsp.agendaroom.viewmodel.ContactDetailState
import br.edu.ifsp.agendaroom.viewmodel.ContactViewModel
import br.edu.ifsp.agendaroom.viewmodel.ContactsListState
import com.google.android.material.snackbar.Snackbar
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

                    ContactDetailState.DeleteSuccess -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.contact_deleted),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                    ContactDetailState.UpdateSuccess -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.contact_updated),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.actionUpdateContact -> {
                        contact.name = nameEditText.text.toString()
                        contact.phone = phoneEditText.text.toString()
                        contact.email = emailEditText.text.toString()

                        viewModel.update(contact)
                        true
                    }

                    R.id.actionDeleteContact -> {
                        viewModel.delete(contact)
                        true
                    }

                    else -> { false }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun fillFields(contact: Contact) {
        this.contact = contact
        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)
    }
}
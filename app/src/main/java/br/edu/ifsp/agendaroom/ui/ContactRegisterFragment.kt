package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.databinding.FragmentContactRegisterBinding
import br.edu.ifsp.agendaroom.domain.Contact
import br.edu.ifsp.agendaroom.viewmodel.ContactViewModel
import br.edu.ifsp.agendaroom.viewmodel.RegisterState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ContactRegisterFragment : Fragment() {
    private var _binding: FragmentContactRegisterBinding? = null
    private val binding get() = _binding!!

    val viewModel: ContactViewModel by viewModels {
        ContactViewModel.contactViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactRegisterBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect {
                when(it) {
                    RegisterState.InsertSuccess -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.success_on_insert_contact),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                    RegisterState.ShowLoading -> { }
                }
            }
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.register_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.actionSaveContact -> {
                        val name: String = binding.commonLayout.nameEditText.text.toString()
                        val phone: String = binding.commonLayout.phoneEditText.text.toString()
                        val email: String = binding.commonLayout.editTextEmail.text.toString()

                        val contact = Contact(name=name, phone=phone, email=email)
                        viewModel.insert(contact)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}
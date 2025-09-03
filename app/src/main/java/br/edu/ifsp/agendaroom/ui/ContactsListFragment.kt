package br.edu.ifsp.agendaroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.agendaroom.R
import br.edu.ifsp.agendaroom.adapter.ContactAdapter
import br.edu.ifsp.agendaroom.databinding.FragmentContactsListBinding
import br.edu.ifsp.agendaroom.domain.Contact
import br.edu.ifsp.agendaroom.viewmodel.ContactViewModel
import br.edu.ifsp.agendaroom.viewmodel.ContactsListState
import kotlinx.coroutines.launch

class ContactsListFragment : Fragment() {
    private var _binding: FragmentContactsListBinding? = null
    private val binding get() = _binding!!
    lateinit var contactAdapter: ContactAdapter
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
        _binding = FragmentContactsListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.actionContactsListToRegister)
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllContacts()
        setupViewModel()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        contactAdapter.filter?.filter(newText)
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactsListState.collect {
                when(it) {
                    is ContactsListState.SearchAllSuccess -> {
                        setupRecyclerView(it.contacts)
                    }
                    ContactsListState.Loading -> { }
                    ContactsListState.Empty -> {
                        binding.textEmptyList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(contactsList: List<Contact>) {
        contactAdapter = ContactAdapter().apply {
            updateList(contactsList)
        }
        binding.recyclerview.adapter = contactAdapter

        contactAdapter.onItemClick = { it ->
            val bundle = Bundle()
            bundle.putInt("contactId", it.id)
            findNavController().navigate(
                R.id.actionContactsListToContactDetail,
                bundle
            )
        }
    }
}
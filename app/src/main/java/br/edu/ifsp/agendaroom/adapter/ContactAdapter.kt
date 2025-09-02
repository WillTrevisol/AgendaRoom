package br.edu.ifsp.agendaroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.agendaroom.databinding.ContactTileBinding
import br.edu.ifsp.agendaroom.domain.Contact

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    var contactsList = ArrayList<Contact>()
    private lateinit var binding: ContactTileBinding

    fun updateList(newList: List<Contact>) {
        contactsList = newList as ArrayList<Contact>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactAdapter.ContactViewHolder {
        binding = ContactTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.name.text = contactsList[position].name
        holder.phone.text = contactsList[position].phone
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    inner class ContactViewHolder(view: ContactTileBinding): RecyclerView.ViewHolder(view.root) {
        var name = view.nameTextView
        var phone = view.phoneTextView
    }
}
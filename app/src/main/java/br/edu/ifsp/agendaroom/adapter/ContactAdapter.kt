package br.edu.ifsp.agendaroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.agendaroom.databinding.ContactTileBinding
import br.edu.ifsp.agendaroom.domain.Contact

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {
    var contactsList = ArrayList<Contact>()
    var contactsListFilterable = ArrayList<Contact>()
    private lateinit var binding: ContactTileBinding
    var onItemClick: ((Contact) -> Unit) ?= null

    fun updateList(newList: List<Contact>) {
        contactsList = newList as ArrayList<Contact>
        contactsListFilterable = newList
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
        return contactsListFilterable.size
    }

    override fun getFilter(): Filter? {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                if (constraint.toString().isEmpty()) {
                    contactsListFilterable = contactsList
                } else {
                    val resultList = ArrayList<Contact>()
                    for (row in contactsList) {
                        if (row.name.lowercase().contains(constraint.toString().lowercase())) {
                            resultList.add(row)
                        }
                    }
                    contactsListFilterable = resultList
                }

                val filterResults = FilterResults()
                filterResults.values = contactsListFilterable
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactsListFilterable = results?.values as ArrayList<Contact>
                notifyDataSetChanged()
            }

        }
    }

    inner class ContactViewHolder(view: ContactTileBinding): RecyclerView.ViewHolder(view.root) {
        var name = view.nameTextView
        var phone = view.phoneTextView

        init {
            view.root.setOnClickListener {
                onItemClick?.invoke(contactsList[adapterPosition])
            }
        }
    }
}
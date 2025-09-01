package br.edu.ifsp.agendaroom.domain

import br.edu.ifsp.agendaroom.data.ContactEntity

data class Contact(
    var id:Int=0,
    var name:String,
    var phone:String,
    var email:String
) {
    fun toEntity() : ContactEntity {
        return ContactEntity(id, name, phone, email)
    }
}

package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import org.hibernate.annotations.Parameter

enum class UserGroup { Driver,Officer, Admin }

@Entity
@Table(name="\"tblUsers\"")
class User {

    @GenericGenerator(
        name = "user_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblUser_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"usrLog\"", nullable = false)
    @GeneratedValue(generator = "user_seq_generator")
    var id: Int? = null

    @Column(name = "\"firstName\"", nullable = false)
    var firstName : String? = null

    @Column(name = "\"lastName\"", nullable = false)
    var lastName: String? = null

    @Column(name = "\"userGroup\"", nullable = false)
    var userGroup = UserGroup.Officer.name

    var deleted = false

    @Column(name = "\"username\"", nullable = true)
    var username: String? = null

    @Column(name = "\"password\"", nullable = true)
    var password: String? = null

    override fun toString(): String {
        return "$firstName $lastName"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is User)
            return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }
}

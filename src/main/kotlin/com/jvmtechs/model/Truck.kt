package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*

@Entity
@Table(name = "\"tblTruck\"")
class Truck {
    @GenericGenerator(
        name = "truck_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblTruck_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"truckLog\"", nullable = false)
    @GeneratedValue(generator = "truck_seq_generator")
    var id: Int? = null

    @Column(name = "\"fleetNo\"", nullable = false)
    val fleetNo: String? = null

    @Column(name = "\"plateNo\"", nullable = false)
    val plateNo: String? = null

    var deleted = false

    override fun toString(): String {
        return "$fleetNo | $plateNo"
    }
}
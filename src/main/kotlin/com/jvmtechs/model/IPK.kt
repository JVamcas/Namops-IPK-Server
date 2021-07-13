package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "\"tblIncomePerKilometre\"")
class IPK {

    @GenericGenerator(
        name = "ipk_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblIPK_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"IPKLog\"", nullable = false)
    @GeneratedValue(generator = "ipk_seq_generator")
    var id: Int? = null

    var dtmCreated: LocalDateTime? = null

    var startKm: String? = null

    var endKm: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"truckLog\"")
    var truck: Truck? = null

    var distance: Double = 0.0

    var ipk: Double = 0.0

    var deleted = false
}

package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name="\"tblTrip\"")
class Trip {

    @GenericGenerator(
        name = "trip_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblTrip_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"tripLog\"", nullable = false)
    @GeneratedValue(generator = "trip_seq_generator")
    var id: Int? = null

    @Column(name = "\"dtmStarted\"", nullable = false)
    var dtmStarted : LocalDateTime? = null

    @Column(name = "\"dtmCompleted\"", nullable = false)
    var dtmCompleted : LocalDateTime? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "\"truckLog\"")
    var truck : Truck? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "\"driverLog\"")
    var driver : User? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "tripLog")
    var containerList = mutableSetOf<Container>()

    @Column(name = "\"description\"", nullable = false)
    var description : String? = null

    @Column(name = "\"memo\"", nullable = false)
    var memo : String? = null

    @Column(name = "\"pickUpLocation\"", nullable = false)
    var pickUpLocation : String? = null

    @Column(name = "\"deliveryLocation\"", nullable = false)
    var deliveryLocation : String? = null

    var deleted = false

}
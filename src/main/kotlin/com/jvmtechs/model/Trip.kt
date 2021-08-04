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

    @Column(name = "\"dtmStarted\"", nullable = true)
    var dtmStarted : LocalDateTime? = null

    @Column(name = "\"dtmCompleted\"", nullable = true)
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

    @Column(name = "\"description\"", nullable = true)
    var description : String? = null

    @Column(name = "\"memo\"", nullable = true)
    var memo : String? = null

    @Column(name = "\"pickUpLocation\"", nullable = true)
    var pickUpLocation : String? = null

    @Column(name = "\"deliveryLocation\"", nullable = true)
    var deliveryLocation : String? = null

    var deleted = false

    @Column(name = "\"priceLessTax\"", nullable = true)
    var priceLessTax: Float? = null

}

class TripQuery{

    var toDate: LocalDateTime? = null
    var fromDate: LocalDateTime? = null
    var truck: Truck? = null
    var driver: User? = null
    var containerNo: String? = null

}
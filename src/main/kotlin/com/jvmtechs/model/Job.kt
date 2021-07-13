package com.jvmtechs.model

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="\"tblJobs\"")
@DynamicUpdate(true)
class Job {

    @GenericGenerator(
        name = "job_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblJob_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"jobLog\"", nullable = false)
    @GeneratedValue(generator = "job_seq_generator")
    var id: Int? = null

    @Column(name = "\"dtmPosted\"", nullable = false)
    var dtmPosted : LocalDateTime? = null

    /***
     * Entry number from the import
     */
    @Column(name = "\"strEntryNo\"", nullable = false)
    var strEntryNo: String? = null

    @Column(name = "\"jobNo\"", nullable = false)
    var legIdx : String? = null

    /***
     * Auto-generated from the [strEntryNo] and [id] as JA[000][id][strEntryNo]
     */
    @Column(name = "\"logNo\"", nullable = false)
    var jobNo : String? = null

    @Column(name = "\"qty\"", nullable = false)
    var qty : Int? = null

    @Column(name = "\"code\"", nullable = false)
    var code : String? = null

    @Column(name = "\"qBCode\"", nullable = false)
    var qBCode : String? = null

    @Column(name = "\"uom\"", nullable = false)
    var uom : String? = null

    @Column(name = "\"description\"", nullable = false)
    var description : String? = null

    @Column(name = "\"memo\"", nullable = false)
    var memo : String? = null

    @Column(name = "\"pickUpLocation\"", nullable = false)
    var pickUpLocation : String? = null

    @Column(name = "\"deliveryLocation\"", nullable = false)
    var deliveryLocation : String? = null

    @Column(name = "\"dtmStart\"", nullable = false)
    var dtmStart : LocalDateTime? = null

    @Column(name = "\"dtmDue\"", nullable = false)
    var dtmDue : LocalDateTime? = null

    @Column(name = "\"priceLessTax\"", nullable = false)
    var priceLessTax : Float? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name="\"allocatedDriver\"", nullable = true)
    var allocatedDriver: User? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "jobLog")
    var containerList = mutableSetOf<Container>()

    var deleted = false
}
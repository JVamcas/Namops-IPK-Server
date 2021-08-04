package com.jvmtechs.model

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import javax.persistence.*

enum class JobStatus {
    Draft, Ready, Completed
}

@Entity
@Table(name = "\"tblJobs\"")
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
    @Column(name = "\"jobLog\"", nullable = true)
    @GeneratedValue(generator = "job_seq_generator")
    var id: Int? = null

    @Column(name = "\"dtmPosted\"", nullable = true)
    var dtmPosted: LocalDateTime? = null

    /***
     * Entry number from the import
     */
    @Column(name = "\"entryNo\"", nullable = true)
    var entryNo: String? = null

    @Column(name = "\"legIdx\"", nullable = true)
    var legIdx: String? = null

    /***
     * Auto-generated from the [entryNo] and [id] as JA[000][id][entryNo]
     */
    @Column(name = "\"jobNo\"", nullable = true)
    var jobNo: String? = null

    @Column(name = "\"qty\"", nullable = true)
    var qty: Int? = null

    @Column(name = "\"code\"", nullable = true)
    var code: String? = null

    @Column(name = "\"qBCode\"", nullable = true)
    var qBCode: String? = null

    @Column(name = "\"uom\"", nullable = true)
    var uom: String? = null

    @Column(name = "\"description\"", nullable = true)
    var description: String? = null

    @Column(name = "\"client\"", nullable = true)
    var client: String? = null

    @Column(name = "\"memo\"", nullable = true)
    var memo: String? = null

    @Column(name = "\"pickUpLocation\"", nullable = true)
    var pickUpLocation: String? = null

    @Column(name = "\"deliveryLocation\"", nullable = true)
    var deliveryLocation: String? = null

    @Column(name = "\"status\"", nullable = true)
    var status: String = JobStatus.Draft.name

    @Column(name = "\"dtmStart\"", nullable = true)
    var dtmStart: LocalDateTime? = null

    @Column(name = "\"dtmDue\"", nullable = true)
    var dtmDue: LocalDateTime? = null

    @Column(name = "\"completed\"", nullable = true)
    var completed = false

    @Column(name = "\"priceLessTax\"", nullable = true)
    var priceLessTax: Float? = null

//    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
//    @JoinColumn(name = "\"allocatedDriver\"", nullable = true)
//    var allocatedDriver: User? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "jobLog")
    var containerList = mutableSetOf<Container>()

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "\"tblDriverJobs\"",
        joinColumns = [JoinColumn(name = "\"jobLog\"")],
        inverseJoinColumns = [JoinColumn(name = "\"usrLog\"")]
    )
    var driversList = mutableSetOf<User>()

    var deleted = false
}

@Entity
@Table(name = "\"tblJob_Seq\"")
class JobSeq {
    @Id
    @Column(name = "\"last_value\"", nullable = true)
    var lastValue: Int? = null

    @Column(name = "\"is_called\"")
    var isCalled = false
}

class JobQuery {

    var jobNo: String? = null
    var qbNumber: String? = null
    var allocatedDriver: User? = null
    var fromDate: LocalDateTime? = null
    var toDate: LocalDateTime? = null
    var container: Container? = null
}
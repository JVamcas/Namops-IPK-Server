package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*

@Entity
@Table(name="\"tblJobAllocations\"")
class DriverAllocation {
    @GenericGenerator(
        name = "allocation_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblJobAllocation_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"allocLog\"", nullable = true)
    @GeneratedValue(generator = "allocation_seq_generator")
    var id: Int? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "\"driver\"", nullable = true)
    var allocatedDriver: User? = null
}

package com.jvmtechs.model

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*

@Entity
@Table(name = "\"tblContainers\"")
class Container {
    @GenericGenerator(
        name = "container_seq_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "\"tblContainer_Seq\""),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @Id
    @Column(name = "\"containerLog\"", nullable = false)
    @GeneratedValue(generator = "container_seq_generator")
    var id: Int? = null

    @Column(name = "\"containerNo\"", nullable = true)
    var containerNo: String? = null

    @Column(name = "\"delivered\"", nullable = true)
    var delivered: Boolean = false

    @Column(name = "\"pickedUp\"", nullable = true)
    var pickedUp: Boolean = false

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name="\"allocatedDriver\"", nullable = true)
    var allocatedDriver: User? = null

    var deleted = false

}
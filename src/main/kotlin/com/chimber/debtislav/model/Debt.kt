package com.chimber.debtislav.model

import org.hibernate.Hibernate
import javax.persistence.*

enum class DebtStatus {
    REQUESTED,
    ACTIVE,
    PAID
}


@Entity
@Table(name = "debt")
data class Debt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var group_id: Long = 0,

    @Column(nullable = false)
    var amount: Int = 0,

    @Column(nullable = false)
    var lender_id: Long = 0,

    @Column(nullable = false)
    var loaner_id: Long = 0,

    @Column(nullable = true)
    var description: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: DebtStatus = DebtStatus.REQUESTED
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Debt

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return "Debt(id=$id, group_id=$group_id, amount=$amount, lender_id=$lender_id," +
                " loaner_id=$loaner_id, description=$description, status=$status)"
    }
}

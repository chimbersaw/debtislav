package com.chimber.debtislav.model

import org.hibernate.Hibernate
import javax.persistence.*


@Entity
@Table(name = "groups")
data class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var admin_id: Long = 0
) {
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_in_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val userList: MutableSet<User> = HashSet()

    @OneToMany(targetEntity = Debt::class, mappedBy = "group_id", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val debtList: MutableSet<Debt> = HashSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Group

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return "Group(id=$id, name=$name)"
    }
}

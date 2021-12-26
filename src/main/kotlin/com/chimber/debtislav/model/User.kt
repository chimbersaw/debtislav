package com.chimber.debtislav.model

import org.hibernate.Hibernate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    private var username: String = "",

    @Column(nullable = false)
    private var password: String = "",

    @Column(unique = true, nullable = false)
    private var email: String = "",

    @Column(name = "token", nullable = true)
    private var token: String? = null,

    @Column(name = "non_expired", nullable = false)
    private val nonExpired: Boolean = true,
    @Column(name = "non_locked", nullable = false)
    private val nonLocked: Boolean = true,
    @Column(nullable = false)
    private val enabled: Boolean = true,
    @Column(name = "credentials_non_expired", nullable = false)
    private val credentialsNonExpired: Boolean = true
) : UserDetails {
    @ManyToMany(mappedBy = "userList", cascade = [CascadeType.ALL])
    val groupList: MutableSet<Group> = HashSet()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
        GrantedAuthority {
            "user"
        }
    )


    override fun getPassword(): String = password
    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = nonExpired
    override fun isAccountNonLocked(): Boolean = nonLocked
    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired
    override fun isEnabled(): Boolean = enabled

    fun getToken(): String? = token
    fun setToken(token: String?) {
        this.token = token
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        return id == (other as User).id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "AppUser(id=$id, username=$username, password=$password)"
    }
}

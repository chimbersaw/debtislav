package com.chimber.debtislav.repository

import com.chimber.debtislav.model.Debt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
@Transactional
interface DebtRepository : JpaRepository<Debt, Long> {
    override fun findById(id: Long): Optional<Debt>
}

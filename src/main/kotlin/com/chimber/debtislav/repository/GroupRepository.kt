package com.chimber.debtislav.repository

import com.chimber.debtislav.exception.GroupNotFoundException
import com.chimber.debtislav.model.Group
import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
@Transactional
interface GroupRepository : JpaRepository<Group, Long> {
    override fun findById(id: Long): Optional<Group>

    fun getGroupOrThrow(groupId: Long): Group
}

@Component
private class GroupRepositoryImpl(@Lazy private val groupRepository: GroupRepository) {
    @SuppressWarnings("unused")
    fun getGroupOrThrow(groupId: Long): Group = groupRepository.findById(groupId).orElseThrow {
        GroupNotFoundException("No group with id $groupId exists")
    }
}

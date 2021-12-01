package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.EngUser
import com.hamawdeh.engineering.data_schema.tables.records.EngUserRecord
import com.hamawdeh.engineering.model.User
import com.hamawdeh.engineering.repo.UserPrivilege.ADMIN
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository

@Repository
class UserRepo(private val dsl: DSLContext) {

    fun findUserByUserName(userName: String): User {
        val result = dsl.selectFrom(EngUser.ENG_USER)
            .where(EngUser.ENG_USER.USER_NAME.eq(userName))
            .fetch()
        return toUser(result).first()
    }

    fun findAdminById(id: Int): User {
        val result = dsl.selectFrom(EngUser.ENG_USER)
            .where(EngUser.ENG_USER.ID.eq(id))
            .and(EngUser.ENG_USER.PRIVILEGE.eq(ADMIN.code))
            .fetch()
        return toUser(result).first()
    }

    fun addUser(user : User) {
        dsl.insertInto(EngUser.ENG_USER)
            .columns(EngUser.ENG_USER.USER_NAME,EngUser.ENG_USER.PASSWORD)
            .values(user.username,user.password)
            .execute()
    }

    private fun toUser(result: Result<EngUserRecord>): List<User> {
        return result.map { User().password(it.password).username(it.userName) }
    }
}
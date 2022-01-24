package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.EngUser
import com.hamawdeh.engineering.data_schema.tables.records.EngUserRecord
import com.hamawdeh.engineering.model.User
import com.hamawdeh.engineering.repo.UserPrivilege.ADMIN
import org.jooq.DSLContext
import org.jooq.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class UserRepo(private val dsl: DSLContext) {
    val log = LoggerFactory.getLogger(UserRepo::class.java)
    fun findAll(): List<User> {
        val records = dsl.selectFrom(EngUser.ENG_USER)
            .where(EngUser.ENG_USER.ACTIVE.eq(true))
            .fetch()
        return toUser(records)
    }

    fun findUserByUserName(userName: String): User {
        val result = dsl.selectFrom(EngUser.ENG_USER)
            .where(
                EngUser.ENG_USER.USER_NAME.eq(userName)
                    .and(EngUser.ENG_USER.ACTIVE.eq(true))
            )
            .fetch()
        return toUser(result).first()
    }

    fun findAdminById(id: Int): User {
        val result = dsl.selectFrom(EngUser.ENG_USER)
            .where(EngUser.ENG_USER.ID.eq(id))
            .and(EngUser.ENG_USER.PRIVILEGE.eq(ADMIN.code))
            .and(EngUser.ENG_USER.ACTIVE.eq(true))
            .fetch()
        return toUser(result).first()
    }

    fun addUser(user: User) {
        val permission = when (user.permission) {
            "admin" -> 2
            else -> 1
        }
        dsl.insertInto(EngUser.ENG_USER)
            .columns(
                EngUser.ENG_USER.USER_NAME,
                EngUser.ENG_USER.PASSWORD,
                EngUser.ENG_USER.PRIVILEGE
            )
            .values(
                user.username,
                user.password,
                permission
            )
            .execute()
    }

    fun updateUser(user: User) {
        log.info(user.toString())
        dsl.update(EngUser.ENG_USER)
            .set(EngUser.ENG_USER.PASSWORD, user.password)
            .where(EngUser.ENG_USER.ID.eq(user.id))
            .execute()

    }

    private fun toUser(result: Result<EngUserRecord>): List<User> {
        return result.map {
            User(
                password = it.password,
                username = it.userName,
                id = it.id,
                permission = when (it.privilege) {
                    2 -> "admin"
                    else -> "user"
                }
            )
        }
    }

    fun deactivateUserByName(name: String) {
        dsl.update(EngUser.ENG_USER)
            .set(EngUser.ENG_USER.ACTIVE, false)
            .where(EngUser.ENG_USER.USER_NAME.eq(name))
            .execute()
    }
}
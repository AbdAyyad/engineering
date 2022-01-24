package com.hamawdeh.engineering.controllers

import com.hamawdeh.engineering.controller.UserApi
import com.hamawdeh.engineering.model.LoginResponse
import com.hamawdeh.engineering.model.User
import com.hamawdeh.engineering.repo.UserRepo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userRepo: UserRepo) : UserApi {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    override fun createUser(adminId: Int, body: User): ResponseEntity<User> {
        userRepo.findAdminById(adminId)
        userRepo.addUser(body)
        return ResponseEntity.ok(body)
    }

    override fun loginUser(user: User): ResponseEntity<LoginResponse> {
        logger.info(user.username)
        logger.info(user.password)
        val userFromDb = userRepo.findUserByUserName(user.username!!)
        logger.info(userFromDb.password)
        return ResponseEntity.ok(
            LoginResponse(
                status = userFromDb.password == user.password,
                id = userFromDb.id,
                name = user.username,
                permission= userFromDb.permission
            )
        )
    }

    override fun deleteUserByName(name: String): ResponseEntity<Unit> {
        userRepo.deactivateUserByName(name)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepo.findAll())
    }

    override fun updateUser(body: User): ResponseEntity<User> {
        userRepo.updateUser(body)
        return ResponseEntity.ok(body)
    }
}
package com.hamawdeh.engineering.controllers

import com.hamawdeh.engineering.controller.UserApi
import com.hamawdeh.engineering.model.LoginResponse
import com.hamawdeh.engineering.model.User
import com.hamawdeh.engineering.repo.UserRepo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userRepo: UserRepo) : UserApi {

    override fun createUser(adminId: Int, body: User): ResponseEntity<User> {
        userRepo.findAdminById(adminId)
        userRepo.addUser(body)
        return ResponseEntity.ok(body)
    }

    override fun loginUser(user: User): ResponseEntity<LoginResponse> {
        val userFromDb = userRepo.findUserByUserName(user.username)
        return ResponseEntity.ok(LoginResponse().status(userFromDb.password == user.password).id(userFromDb.id))
    }
}
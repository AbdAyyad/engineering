package com.hamawdeh.engineering.controllers

import com.hamawdeh.engineering.controller.ItemsApi
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.SerialObject
import com.hamawdeh.engineering.repo.ItemRepo
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ItemsController(private val itemRepo: ItemRepo) : ItemsApi {
    override fun getItemBySerial(serialId: Int): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.getItemBySerial(serialId))
    }

    override fun getSerial(): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.getSerial())
    }

    override fun getSubItemByItem(itemId: Int): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.getSubItemBySerial(itemId))
    }

    override fun postOrder(orderObject: OrderObject): ResponseEntity<OrderObject> {
        itemRepo.addOrder(orderObject)
        return ResponseEntity.ok(orderObject)
    }
}
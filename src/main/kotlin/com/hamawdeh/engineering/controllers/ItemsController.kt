package com.hamawdeh.engineering.controllers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.hamawdeh.engineering.controller.ItemsApiDelegate
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.OrderResponse
import com.hamawdeh.engineering.model.SerialObject
import com.hamawdeh.engineering.repo.ItemRepo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.io.StringWriter

@RestController
class ItemsController(private val itemRepo: ItemRepo) : ItemsApiDelegate {

    override fun postOrder(orderObject: OrderObject): ResponseEntity<OrderObject> {
        itemRepo.addOrder(orderObject)
        return ResponseEntity.ok(orderObject)
    }

    override fun getCategory(): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.findOrderType())
    }

    override fun getItemByCode(typeCode: Int): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.findItemByOrderType(typeCode))
    }

    override fun getOrders(type: Int?): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(itemRepo.findOrder(type))
    }

    override fun getOrdersCsv(type: Int?): ResponseEntity<String> {
        val orders = itemRepo.findOrder(type)

        val csvSchemaBuilder = CsvSchema.builder()
        csvSchemaBuilder
            .addColumn("serial")
            .addColumn("name")
            .addColumn("type")
            .addColumn("role")
            .addColumn("phone")
            .addColumn("email")
            .addColumn("notes")

        val stringWriter = StringWriter()
        val csvMapper = CsvMapper()
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)

        stringWriter.write(csvMapper
            .writer(csvSchemaBuilder.build().withHeader())
            .writeValueAsString(null))

        orders.map {
            csvMapper
                .writerFor(OrderResponse::class.java)
                .with(csvSchemaBuilder.build())
                .writeValue(stringWriter, it)
        }

        val headers = HttpHeaders()
        headers.set("Content-type", "text/csv")
        headers.set("Content-disposition", "attachment;filename=excel.csv")
        return ResponseEntity(stringWriter.toString(), headers, HttpStatus.OK)
    }

    override fun getTypes(): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.findOrderType())
    }
}
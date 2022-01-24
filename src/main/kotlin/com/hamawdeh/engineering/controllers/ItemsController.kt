package com.hamawdeh.engineering.controllers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.hamawdeh.engineering.controller.ItemsApi
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.OrderResponse
import com.hamawdeh.engineering.model.SerialObject
import com.hamawdeh.engineering.model.UpdateOrderRequest
import com.hamawdeh.engineering.repo.ItemRepo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.StringWriter
import javax.validation.constraints.NotNull

@RestController
class ItemsController(private val itemRepo: ItemRepo) : ItemsApi {

    override fun postOrder(orderObject: OrderObject): ResponseEntity<OrderObject> {
        itemRepo.addOrder(orderObject)
        return ResponseEntity.ok(orderObject)
    }

    override fun getCategory(): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.findCategory())
    }

    override fun getItemByCode(typeCode: Int): ResponseEntity<List<SerialObject>> {
        return ResponseEntity.ok(itemRepo.findItemByOrderType(typeCode))
    }

    override fun getOrders(
        @RequestParam(required = false, value = "cat") cat: Int?,
        @RequestParam(required = false, value = "type") type: Int?,
        @RequestParam(required = false, value = "item") item: Int?
    ): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(itemRepo.findOrderByTypeCodeAndCatCodeAndItemCode(type, cat, item))
    }

    override fun getOrdersCsv(
        @RequestParam(required = false, value = "cat") cat: Int?,
        @RequestParam(required = false, value = "type") type: Int?,
        @RequestParam(required = false, value = "item") item: Int?
    ): ResponseEntity<String> {
        val orders = itemRepo.findOrderByTypeCodeAndCatCodeAndItemCode(type, cat, item)

        val csvSchemaBuilder = CsvSchema.builder()
        csvSchemaBuilder
            .addColumn("serial")
            .addColumn("name")
            .addColumn("type")
            .addColumn("role")
            .addColumn("email")
            .addColumn("phone")
            .addColumn("secPhone")
            .addColumn("company")
            .addColumn("companyCat")
            .addColumn("item")
            .addColumn("subItem")
            .addColumn("category")
            .addColumn("notes")

        val stringWriter = StringWriter()
        val csvMapper = CsvMapper()
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)

        stringWriter.write(
            csvMapper
                .writer(csvSchemaBuilder.build().withHeader())
                .writeValueAsString(null)
        )

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

    override fun addCategory(serialObject: SerialObject): ResponseEntity<SerialObject> {
        itemRepo.addCategory(serialObject)
        return ResponseEntity.ok(serialObject)
    }

    override fun addSubType(typeCode: Int, serialObject: SerialObject): ResponseEntity<SerialObject> {
        itemRepo.addSubItem(typeCode, serialObject)
        return ResponseEntity.ok(serialObject)
    }

    override fun addType(serialObject: SerialObject): ResponseEntity<SerialObject> {
        itemRepo.addItem(serialObject)
        return ResponseEntity.ok(serialObject)
    }

    override fun patchCategory(code: Int, serialObject: SerialObject?): ResponseEntity<SerialObject> {
        return ResponseEntity.ok(itemRepo.updateCategoryByCode(code, serialObject!!))
    }

    override fun patchSubType(typeCode: Int, serialObject: SerialObject?): ResponseEntity<SerialObject> {
        return ResponseEntity.ok(itemRepo.updateItemByCode(typeCode, serialObject!!))
    }

    override fun patchType(code: Int, serialObject: SerialObject?): ResponseEntity<SerialObject> {
        return ResponseEntity.ok(itemRepo.updateTypeByCode(code, serialObject!!))
    }

    override fun patchOrder(updateOrderRequest: UpdateOrderRequest): ResponseEntity<OrderResponse> {
        return ResponseEntity.ok(itemRepo.updateOrder(updateOrderRequest))
    }

    override fun deleteOrder(id: Int): ResponseEntity<Int> {
        return ResponseEntity.ok(itemRepo.deleteOrder(id))
    }

    override fun searchOrder(
        @NotNull @RequestParam(required = true, value = "keyword") keyword: String,
        @RequestParam(required = false, value = "cat") cat: Int?,
        @RequestParam(required = false, value = "type") type: Int?,
        @RequestParam(required = false, value = "item") item: Int?
    ): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(itemRepo.search(keyword, type, cat, item))
    }
}
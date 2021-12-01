package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.EngOrder
import com.hamawdeh.engineering.data_schema.tables.Item
import com.hamawdeh.engineering.data_schema.tables.ItemSerial
import com.hamawdeh.engineering.data_schema.tables.SubItem
import com.hamawdeh.engineering.data_schema.tables.records.ItemRecord
import com.hamawdeh.engineering.data_schema.tables.records.ItemSerialRecord
import com.hamawdeh.engineering.data_schema.tables.records.SubItemRecord
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.SerialObject
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository

@Repository
class ItemRepo(private val dsl: DSLContext) {
    fun getSerial(): List<SerialObject> {
        val result = dsl.selectFrom(ItemSerial.ITEM_SERIAL)
            .fetch()
        return mapSerial(result)
    }

    private fun mapSerial(result: Result<ItemSerialRecord>): List<SerialObject> {
        return result.map { SerialObject().code(it.code.toInt()).description(it.description) }
    }

    fun getItemBySerial(serialId: Int): List<SerialObject> {
        val result = dsl.selectFrom(Item.ITEM).where(Item.ITEM.SERIAL_ID.eq(serialId)).fetch()
        return mapItem(result)
    }

    private fun mapItem(result: Result<ItemRecord>): List<SerialObject> {
        return result.map { SerialObject().code(it.code.toInt()).description(it.description) }
    }

    fun getSubItemBySerial(itemId: Int): List<SerialObject> {
        val result = dsl.selectFrom(SubItem.SUB_ITEM).where(SubItem.SUB_ITEM.ITEM_ID.eq(itemId)).fetch()
        return mapSubItem(result)
    }

    private fun mapSubItem(result: Result<SubItemRecord>): List<SerialObject> {
        return result.map { SerialObject().code(it.code.toInt()).description(it.description) }
    }

    fun addOrder(order: OrderObject) {
        dsl.insertInto(EngOrder.ENG_ORDER).columns(
            EngOrder.ENG_ORDER.SUB_ITEM_ID,
            EngOrder.ENG_ORDER.NAME,
            EngOrder.ENG_ORDER.ROLE,
            EngOrder.ENG_ORDER.PHONE,
            EngOrder.ENG_ORDER.ADDRESS,
            EngOrder.ENG_ORDER.NOTES
        ).values(
            order.subitemId,
            order.name,
            order.role,
            order.phone,
            order.adress,
            order.notes
        ).execute()
    }
}
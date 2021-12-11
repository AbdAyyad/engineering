package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.*
import com.hamawdeh.engineering.data_schema.tables.records.CategoryRecord
import com.hamawdeh.engineering.data_schema.tables.records.EngOrderRecord
import com.hamawdeh.engineering.data_schema.tables.records.ItemRecord
import com.hamawdeh.engineering.data_schema.tables.records.OrderTypeRecord
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.OrderResponse
import com.hamawdeh.engineering.model.SerialObject
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository

@Repository
class ItemRepo(private val dsl: DSLContext) {
    fun findCategory(): List<SerialObject> {
        val result = dsl.selectFrom(Category.CATEGORY)
            .fetch()
        return mapCategory(result)
    }

    private fun mapCategory(result: Result<CategoryRecord>): List<SerialObject> {
        return result.map {
            SerialObject(
                code = it.code,
                description = it.name
            )
        }
    }

    fun findOrderType(): List<SerialObject> {
        val result = dsl.selectFrom(OrderType.ORDER_TYPE).fetch()
        return mapOrderType(result)
    }

    private fun mapOrderType(result: Result<OrderTypeRecord>): List<SerialObject> {
        return result.map {
            SerialObject(
                code = it.code,
                description = it.description
            )
        }
    }

    fun findItemByOrderType(OrderTypeCode: Int): List<SerialObject> {
        val result = dsl.selectFrom(Item.ITEM)
            .where(Item.ITEM.TYPE_CODE.eq(OrderTypeCode))
            .fetch()
        return mapItem(result)
    }

    private fun mapItem(result: Result<ItemRecord>): List<SerialObject> {
        return result.map {
            SerialObject(
                code = it.code,
                description = it.description
            )
        }
    }

    fun addOrder(order: OrderObject) {
        dsl.insertInto(EngOrder.ENG_ORDER).columns(
            EngOrder.ENG_ORDER.CATEGORY_CODE,
            EngOrder.ENG_ORDER.ITEM_CODE,
            EngOrder.ENG_ORDER.NAME,
            EngOrder.ENG_ORDER.ROLE,
            EngOrder.ENG_ORDER.PHONE,
            EngOrder.ENG_ORDER.ADDRESS,
            EngOrder.ENG_ORDER.NOTES
        ).values(
            order.categoryCode,
            order.itemCode,
            order.name,
            order.role,
            order.phone,
            order.adress,
            order.notes
        ).execute()
    }

    fun findOrder(type: Int?): List<OrderResponse> {
        val selectFrom = dsl.select()
            .from(
                EngOrder.ENG_ORDER
                    .join(Item.ITEM)
                    .on(EngOrder.ENG_ORDER.ITEM_CODE.eq(Item.ITEM.CODE))
            )

        if (type != null) {
            selectFrom.where(Item.ITEM.TYPE_CODE.eq(type))
        }

        return mapOrders(selectFrom.fetchInto(EngOrder.ENG_ORDER))
    }

    fun mapOrders(result: Result<EngOrderRecord>): List<OrderResponse> {
        return result.map {
            val record = dsl.select()
                .from(
                    OrderType.ORDER_TYPE
                        .join(Item.ITEM)
                        .on(Item.ITEM.TYPE_CODE.eq(OrderType.ORDER_TYPE.CODE))
                )
                .where(Item.ITEM.CODE.eq(it.itemCode))
                .fetchInto(OrderType.ORDER_TYPE)
                .first()

            OrderResponse(
                name = it.name,
                notes = it.notes,
                phone = it.phone,
                role = it.role,
                serial = "${it.categoryCode}-${record.code}-${it.itemCode}",
                type = record.description
            )
        }
    }

    fun addCategory(serialObject: SerialObject) {
        dsl.insertInto(Category.CATEGORY)
            .columns(
                Category.CATEGORY.CODE,
                Category.CATEGORY.NAME
            ).values(
                serialObject.code,
                serialObject.description
            )
            .execute()
    }

    fun addItem(serialObject: SerialObject) {
        dsl.insertInto(OrderType.ORDER_TYPE)
            .columns(
                OrderType.ORDER_TYPE.CODE,
                OrderType.ORDER_TYPE.DESCRIPTION
            ).values(
                serialObject.code,
                serialObject.description
            )
            .execute()
    }

    fun addSubItem(itemCode: Int, serialObject: SerialObject) {
        dsl.insertInto(Item.ITEM)
            .columns(
                Item.ITEM.CODE,
                Item.ITEM.DESCRIPTION,
                Item.ITEM.TYPE_CODE
            ).values(
                serialObject.code,
                serialObject.description,
                itemCode
            )
            .execute()
    }
}
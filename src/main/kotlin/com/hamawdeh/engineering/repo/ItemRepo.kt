package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.Category
import com.hamawdeh.engineering.data_schema.tables.EngOrder
import com.hamawdeh.engineering.data_schema.tables.Item
import com.hamawdeh.engineering.data_schema.tables.OrderType
import com.hamawdeh.engineering.data_schema.tables.records.CategoryRecord
import com.hamawdeh.engineering.data_schema.tables.records.EngOrderRecord
import com.hamawdeh.engineering.data_schema.tables.records.ItemRecord
import com.hamawdeh.engineering.data_schema.tables.records.OrderTypeRecord
import com.hamawdeh.engineering.model.OrderObject
import com.hamawdeh.engineering.model.OrderResponse
import com.hamawdeh.engineering.model.SerialObject
import com.hamawdeh.engineering.model.UpdateOrderRequest
import org.jooq.DSLContext
import org.jooq.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ItemRepo(private val dsl: DSLContext) {

    val log = LoggerFactory.getLogger(ItemRepo::class.java)

    fun findCategory(): List<SerialObject> {
        val result = dsl.selectFrom(Category.CATEGORY).fetch()
        return mapCategory(result)
    }

    private fun mapCategory(result: Result<CategoryRecord>): List<SerialObject> {
        return result.map {
            SerialObject(
                code = it.code, description = it.name
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
                code = it.code, description = it.description
            )
        }
    }

    fun findItemByOrderType(OrderTypeCode: Int): List<SerialObject> {
        val id = findTypeIdByCode(OrderTypeCode)
        val result = dsl.selectFrom(Item.ITEM).where(Item.ITEM.TYPE_ID.eq(id)).fetch()
        return mapItem(result)
    }

    private fun mapItem(result: Result<ItemRecord>): List<SerialObject> {
        return result.map {
            SerialObject(
                code = it.code, description = it.description
            )
        }
    }

    fun addOrder(order: OrderObject) {
        dsl.insertInto(EngOrder.ENG_ORDER).columns(
            EngOrder.ENG_ORDER.CATEGORY_ID,
            EngOrder.ENG_ORDER.ITEM_ID,
            EngOrder.ENG_ORDER.NAME,
            EngOrder.ENG_ORDER.ROLE,
            EngOrder.ENG_ORDER.PHONE,
            EngOrder.ENG_ORDER.ADDRESS,
            EngOrder.ENG_ORDER.NOTES,
            EngOrder.ENG_ORDER.SEC_PHONE,
            EngOrder.ENG_ORDER.COMPANY,
            EngOrder.ENG_ORDER.COMPANY_CAT,
        ).values(
            order.categoryCode,
            order.itemCode,
            order.name,
            order.role,
            order.phone,
            order.adress,
            order.notes,
            order.secPhone,
            order.company,
            order.companyCat
        ).execute()
    }

    fun findOrderByTypeCode(type: Int?): List<OrderResponse> {
        val selectFrom = dsl.select().from(
            EngOrder.ENG_ORDER.join(Item.ITEM).on(EngOrder.ENG_ORDER.ITEM_ID.eq(Item.ITEM.ID))
                .join(OrderType.ORDER_TYPE).on(Item.ITEM.TYPE_ID.eq(OrderType.ORDER_TYPE.ID))
        )

        if (type != null) {
            selectFrom.where(OrderType.ORDER_TYPE.CODE.eq(type))
        }

        return mapOrders(selectFrom.fetchInto(EngOrder.ENG_ORDER))
    }

    fun mapOrders(result: Result<EngOrderRecord>): List<OrderResponse> {
        return result.map {
            val query = dsl.select().from(
                EngOrder.ENG_ORDER.join(Item.ITEM).on(Item.ITEM.ID.eq(EngOrder.ENG_ORDER.ITEM_ID))
                    .join(OrderType.ORDER_TYPE).on(OrderType.ORDER_TYPE.ID.eq(Item.ITEM.TYPE_ID))
                    .join(Category.CATEGORY).on(Category.CATEGORY.ID.eq(EngOrder.ENG_ORDER.CATEGORY_ID))
            ).where(EngOrder.ENG_ORDER.ID.eq(it.id))


            log.info(query.toString())

            val record = query.fetch().first()

            OrderResponse(
                id = it.id,
                name = it.name,
                notes = it.notes,
                phone = it.phone,
                role = it.role,
                serial = "${record.get(Category.CATEGORY.CODE)}-${record.get(OrderType.ORDER_TYPE.CODE)}-${
                    record.get(
                        Item.ITEM.CODE
                    )
                }",
                type = record.get(OrderType.ORDER_TYPE.DESCRIPTION),
                company = it.company,
                companyCat = it.companyCat,
                secPhone = it.secPhone
            )
        }
    }

    fun addCategory(serialObject: SerialObject) {
        dsl.insertInto(Category.CATEGORY).columns(
            Category.CATEGORY.CODE, Category.CATEGORY.NAME
        ).values(
            serialObject.code, serialObject.description
        ).execute()
    }

    fun addItem(serialObject: SerialObject) {
        dsl.insertInto(OrderType.ORDER_TYPE).columns(
            OrderType.ORDER_TYPE.CODE, OrderType.ORDER_TYPE.DESCRIPTION
        ).values(
            serialObject.code, serialObject.description
        ).execute()
    }

    private fun findTypeIdByCode(code: Int): Int {
        return dsl.select(OrderType.ORDER_TYPE.ID).from(OrderType.ORDER_TYPE).where(OrderType.ORDER_TYPE.CODE.eq(code))
            .fetchOne()?.map { it.get(OrderType.ORDER_TYPE.ID) }!!

    }

    fun addSubItem(itemCode: Int, serialObject: SerialObject) {
        val id = findTypeIdByCode(itemCode)

        dsl.insertInto(Item.ITEM).columns(
            Item.ITEM.CODE, Item.ITEM.DESCRIPTION, Item.ITEM.TYPE_ID
        ).values(
            serialObject.code, serialObject.description, id
        ).execute()
    }

    fun updateCategoryByCode(code: Int, serialObject: SerialObject): SerialObject {
        val result = dsl.update(Category.CATEGORY).set(Category.CATEGORY.CODE, serialObject.code)
            .set(Category.CATEGORY.NAME, serialObject.description).where(Category.CATEGORY.CODE.eq(code)).returning()
        return result.map {
            SerialObject(
                code = it.code, description = it.name
            )
        }.first()
    }

    fun updateTypeByCode(code: Int, serialObject: SerialObject): SerialObject {
        val result = dsl.update(OrderType.ORDER_TYPE).set(OrderType.ORDER_TYPE.CODE, serialObject.code)
            .set(OrderType.ORDER_TYPE.DESCRIPTION, serialObject.description).where(OrderType.ORDER_TYPE.CODE.eq(code))
            .returning()
        return result.map {
            SerialObject(
                code = it.code, description = it.description
            )
        }.first()
    }

    fun updateItemByCode(code: Int, serialObject: SerialObject): SerialObject {
        val result = dsl.update(Item.ITEM).set(Item.ITEM.CODE, serialObject.code)
            .set(Item.ITEM.DESCRIPTION, serialObject.description).where(Item.ITEM.CODE.eq(code)).returning()
        return result.map {
            SerialObject(
                code = it.code, description = it.description
            )
        }.first()
    }

    fun updateOrder(updateOrderRequest: UpdateOrderRequest): OrderResponse {
        return dsl.update(EngOrder.ENG_ORDER).set(EngOrder.ENG_ORDER.NAME, updateOrderRequest.name)
            .set(EngOrder.ENG_ORDER.ROLE, updateOrderRequest.role)
            .set(EngOrder.ENG_ORDER.PHONE, updateOrderRequest.phone)
            .set(EngOrder.ENG_ORDER.NOTES, updateOrderRequest.notes)
            .set(EngOrder.ENG_ORDER.ADDRESS, updateOrderRequest.adress)
            .where(EngOrder.ENG_ORDER.ID.eq(updateOrderRequest.id)).returning().map {
                val query = dsl.select().from(
                    EngOrder.ENG_ORDER.join(Item.ITEM).on(Item.ITEM.ID.eq(EngOrder.ENG_ORDER.ITEM_ID))
                        .join(OrderType.ORDER_TYPE).on(OrderType.ORDER_TYPE.ID.eq(Item.ITEM.TYPE_ID))
                        .join(Category.CATEGORY).on(Category.CATEGORY.ID.eq(EngOrder.ENG_ORDER.CATEGORY_ID))
                ).where(EngOrder.ENG_ORDER.ID.eq(it.id))


                log.info(query.toString())

                val record = query.fetch().first()

                OrderResponse(
                    id = it.id,
                    name = it.name,
                    notes = it.notes,
                    phone = it.phone,
                    role = it.role,
                    serial = "${record.get(Category.CATEGORY.CODE)}-${record.get(OrderType.ORDER_TYPE.CODE)}-${
                        record.get(
                            Item.ITEM.CODE
                        )
                    }",
                    type = record.get(OrderType.ORDER_TYPE.DESCRIPTION)
                )
            }.first()
    }

    fun deleteOrder(id: Int): Int {
        return dsl.deleteFrom(EngOrder.ENG_ORDER).where(EngOrder.ENG_ORDER.ID.eq(id)).execute()
    }

    fun search(keyword: String): List<OrderResponse> {
        val ids = dsl.select(EngOrder.ENG_ORDER.ID).from(EngOrder.ENG_ORDER).where(
            EngOrder.ENG_ORDER.NAME.like("%$keyword%").or(
                EngOrder.ENG_ORDER.ADDRESS.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.NOTES.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.PHONE.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.SEC_PHONE.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.ROLE.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.EMAIL.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.COMPANY.like("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.COMPANY_CAT.like("%$keyword%")
            )
        ).fetch().map { it.get(EngOrder.ENG_ORDER.ID) }.distinct()

        return ids.map {
            val query = dsl.select().from(
                EngOrder.ENG_ORDER.join(Item.ITEM).on(Item.ITEM.ID.eq(EngOrder.ENG_ORDER.ITEM_ID))
                    .join(OrderType.ORDER_TYPE).on(OrderType.ORDER_TYPE.ID.eq(Item.ITEM.TYPE_ID))
                    .join(Category.CATEGORY).on(Category.CATEGORY.ID.eq(EngOrder.ENG_ORDER.CATEGORY_ID))
            ).where(EngOrder.ENG_ORDER.ID.eq(it))
            val record = query.fetch().first()

            OrderResponse(
                id = it,
                name = record.get(EngOrder.ENG_ORDER.NAME),
                notes = record.get(EngOrder.ENG_ORDER.NOTES),
                phone = record.get(EngOrder.ENG_ORDER.PHONE),
                role = record.get(EngOrder.ENG_ORDER.ROLE),
                serial = "${record.get(Category.CATEGORY.CODE)}-${record.get(OrderType.ORDER_TYPE.CODE)}-${
                    record.get(
                        Item.ITEM.CODE
                    )
                }",
                type = record.get(OrderType.ORDER_TYPE.DESCRIPTION)
            )
        }

    }
}
package com.hamawdeh.engineering.repo

import com.hamawdeh.engineering.data_schema.tables.Category
import com.hamawdeh.engineering.data_schema.tables.EngOrder
import com.hamawdeh.engineering.data_schema.tables.Item
import com.hamawdeh.engineering.data_schema.tables.OrderType
import com.hamawdeh.engineering.data_schema.tables.records.CategoryRecord
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
                code = it.code,
                description = it.description
            )
        }
    }

    fun addOrder(order: OrderObject) {

        val catId = dsl.select(Category.CATEGORY.ID)
            .from(Category.CATEGORY)
            .where(Category.CATEGORY.CODE.eq(order.categoryCode))
            .fetchOne()
            ?.map { it.get(Category.CATEGORY.ID) }

        log.info("cat code ${order.categoryCode} id $catId")

        val itemId = dsl.select(Item.ITEM.ID)
            .from(Item.ITEM)
            .where(Item.ITEM.CODE.eq(order.itemCode))
            .fetchOne()
            ?.map { it.get(Category.CATEGORY.ID) }

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
            EngOrder.ENG_ORDER.EMAIL
        ).values(
            catId!!,
            itemId!!,
            order.name,
            order.role,
            order.phone,
            order.adress,
            order.notes,
            order.secPhone,
            order.company,
            order.companyCat,
            order.email
        ).execute()
    }

    fun findOrderByTypeCodeAndCatCodeAndItemCode(type: Int?, cat: Int?, item: Int?): List<OrderResponse> {
        val selectFrom = dsl.select().from(
            EngOrder.ENG_ORDER.join(Item.ITEM).on(Item.ITEM.ID.eq(EngOrder.ENG_ORDER.ITEM_ID))
                .join(OrderType.ORDER_TYPE).on(OrderType.ORDER_TYPE.ID.eq(Item.ITEM.TYPE_ID)).join(Category.CATEGORY)
                .on(Category.CATEGORY.ID.eq(EngOrder.ENG_ORDER.CATEGORY_ID))
        )

        if (type != null) {
            selectFrom.where(OrderType.ORDER_TYPE.CODE.eq(type))
        }

        if (cat != null) {
            selectFrom.where(Category.CATEGORY.CODE.eq(cat))
        }

        if (item != null) {
            selectFrom.where(Item.ITEM.CODE.eq(item))
        }

        selectFrom.orderBy(OrderType.ORDER_TYPE.CREATED.desc())

        log.info(selectFrom.toString())

        return selectFrom.fetch().map {
            OrderResponse(
                id = it.get(EngOrder.ENG_ORDER.ID),
                name = it.get(EngOrder.ENG_ORDER.NAME),
                notes = it.get(EngOrder.ENG_ORDER.NOTES),
                phone = it.get(EngOrder.ENG_ORDER.PHONE),
                role = it.get(EngOrder.ENG_ORDER.ROLE),
                serial = "${it.get(Category.CATEGORY.CODE)}-${it.get(OrderType.ORDER_TYPE.CODE)}-${
                    it.get(
                        Item.ITEM.CODE
                    )
                }",
                type = it.get(Category.CATEGORY.NAME),
                item = it.get(OrderType.ORDER_TYPE.DESCRIPTION),
                category = it.get(Category.CATEGORY.NAME),
                subItem = it.get(Item.ITEM.DESCRIPTION),
                email = it.get(EngOrder.ENG_ORDER.EMAIL)
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
            .set(EngOrder.ENG_ORDER.EMAIL, updateOrderRequest.email)
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
                    type = record.get(Category.CATEGORY.NAME),
                    item = record.get(OrderType.ORDER_TYPE.DESCRIPTION),
                    category = record.get(Category.CATEGORY.NAME),
                    subItem = record.get(Item.ITEM.DESCRIPTION)
                )
            }.first()
    }

    fun deleteOrder(id: Int): Int {
        return dsl.deleteFrom(EngOrder.ENG_ORDER).where(EngOrder.ENG_ORDER.ID.eq(id)).execute()
    }

    fun search(keyword: String, type: Int?, cat: Int?, item: Int?): List<OrderResponse> {
        val sqlStatement = dsl.select().from(
            EngOrder.ENG_ORDER.join(Item.ITEM).on(Item.ITEM.ID.eq(EngOrder.ENG_ORDER.ITEM_ID))
                .join(OrderType.ORDER_TYPE).on(OrderType.ORDER_TYPE.ID.eq(Item.ITEM.TYPE_ID))
                .join(Category.CATEGORY).on(Category.CATEGORY.ID.eq(EngOrder.ENG_ORDER.CATEGORY_ID))
        ).where(
            EngOrder.ENG_ORDER.NAME.likeIgnoreCase("%$keyword%").or(
                EngOrder.ENG_ORDER.ADDRESS.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.NOTES.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.PHONE.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.SEC_PHONE.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.ROLE.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.EMAIL.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.COMPANY.likeIgnoreCase("%$keyword%")
            ).or(
                EngOrder.ENG_ORDER.COMPANY_CAT.likeIgnoreCase("%$keyword%")
            ).or(
                OrderType.ORDER_TYPE.DESCRIPTION.likeIgnoreCase("%$keyword%")
            ).or(
                Category.CATEGORY.NAME.likeIgnoreCase("%$keyword%")
            ).or(
                Item.ITEM.DESCRIPTION.likeIgnoreCase("%$keyword%")
            )
        )

        if (type != null) {
            sqlStatement.and(OrderType.ORDER_TYPE.CODE.eq(type))
        }

        if (cat != null) {
            sqlStatement.and(Category.CATEGORY.CODE.eq(cat))
        }

        if (item != null) {
            sqlStatement.and(Item.ITEM.CODE.eq(item))
        }

        sqlStatement.orderBy(OrderType.ORDER_TYPE.CREATED.desc())

        log.info(sqlStatement.toString())

        return sqlStatement.fetch().map {
            OrderResponse(
                id = it.get(EngOrder.ENG_ORDER.ID),
                name = it.get(EngOrder.ENG_ORDER.NAME),
                notes = it.get(EngOrder.ENG_ORDER.NOTES),
                phone = it.get(EngOrder.ENG_ORDER.PHONE),
                role = it.get(EngOrder.ENG_ORDER.ROLE),
                serial = "${it.get(Category.CATEGORY.CODE)}-${it.get(OrderType.ORDER_TYPE.CODE)}-${
                    it.get(
                        Item.ITEM.CODE
                    )
                }",
                type = it.get(Category.CATEGORY.NAME),
                item = it.get(OrderType.ORDER_TYPE.DESCRIPTION),
                category = it.get(Category.CATEGORY.NAME),
                subItem = it.get(Item.ITEM.DESCRIPTION),
                email = it.get(EngOrder.ENG_ORDER.EMAIL)
            )
        }.distinct()
    }

}
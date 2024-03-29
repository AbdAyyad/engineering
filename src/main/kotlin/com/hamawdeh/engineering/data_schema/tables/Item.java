/*
 * This file is generated by jOOQ.
 */
package com.hamawdeh.engineering.data_schema.tables;


import com.hamawdeh.engineering.data_schema.Keys;
import com.hamawdeh.engineering.data_schema.Public;
import com.hamawdeh.engineering.data_schema.tables.records.ItemRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Item extends TableImpl<ItemRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.item</code>
     */
    public static final Item ITEM = new Item();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ItemRecord> getRecordType() {
        return ItemRecord.class;
    }

    /**
     * The column <code>public.item.id</code>.
     */
    public final TableField<ItemRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.item.type_id</code>.
     */
    public final TableField<ItemRecord, Integer> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.item.description</code>.
     */
    public final TableField<ItemRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.item.code</code>.
     */
    public final TableField<ItemRecord, Integer> CODE = createField(DSL.name("code"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.item.created</code>.
     */
    public final TableField<ItemRecord, LocalDateTime> CREATED = createField(DSL.name("created"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    private Item(Name alias, Table<ItemRecord> aliased) {
        this(alias, aliased, null);
    }

    private Item(Name alias, Table<ItemRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.item</code> table reference
     */
    public Item(String alias) {
        this(DSL.name(alias), ITEM);
    }

    /**
     * Create an aliased <code>public.item</code> table reference
     */
    public Item(Name alias) {
        this(alias, ITEM);
    }

    /**
     * Create a <code>public.item</code> table reference
     */
    public Item() {
        this(DSL.name("item"), null);
    }

    public <O extends Record> Item(Table<O> child, ForeignKey<O, ItemRecord> key) {
        super(child, key, ITEM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<ItemRecord, Integer> getIdentity() {
        return (Identity<ItemRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ItemRecord> getPrimaryKey() {
        return Keys.ITEM_PKEY;
    }

    @Override
    public List<UniqueKey<ItemRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.ITEM_CODE_KEY);
    }

    @Override
    public List<ForeignKey<ItemRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ITEM__FK_TYPE_ID);
    }

    private transient OrderType _orderType;

    public OrderType orderType() {
        if (_orderType == null)
            _orderType = new OrderType(this, Keys.ITEM__FK_TYPE_ID);

        return _orderType;
    }

    @Override
    public Item as(String alias) {
        return new Item(DSL.name(alias), this);
    }

    @Override
    public Item as(Name alias) {
        return new Item(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Item rename(String name) {
        return new Item(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Item rename(Name name) {
        return new Item(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, Integer, String, Integer, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}

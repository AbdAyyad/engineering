/*
 * This file is generated by jOOQ.
 */
package com.hamawdeh.engineering.data_schema.tables;


import com.hamawdeh.engineering.data_schema.Keys;
import com.hamawdeh.engineering.data_schema.Public;
import com.hamawdeh.engineering.data_schema.tables.records.EngOrderRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row8;
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
public class EngOrder extends TableImpl<EngOrderRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.eng_order</code>
     */
    public static final EngOrder ENG_ORDER = new EngOrder();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EngOrderRecord> getRecordType() {
        return EngOrderRecord.class;
    }

    /**
     * The column <code>public.eng_order.id</code>.
     */
    public final TableField<EngOrderRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.eng_order.sub_item_id</code>.
     */
    public final TableField<EngOrderRecord, Integer> SUB_ITEM_ID = createField(DSL.name("sub_item_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.eng_order.name</code>.
     */
    public final TableField<EngOrderRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.eng_order.role</code>.
     */
    public final TableField<EngOrderRecord, String> ROLE = createField(DSL.name("role"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.eng_order.phone</code>.
     */
    public final TableField<EngOrderRecord, String> PHONE = createField(DSL.name("phone"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.eng_order.address</code>.
     */
    public final TableField<EngOrderRecord, String> ADDRESS = createField(DSL.name("address"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.eng_order.notes</code>.
     */
    public final TableField<EngOrderRecord, String> NOTES = createField(DSL.name("notes"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.eng_order.created</code>.
     */
    public final TableField<EngOrderRecord, LocalDateTime> CREATED = createField(DSL.name("created"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    private EngOrder(Name alias, Table<EngOrderRecord> aliased) {
        this(alias, aliased, null);
    }

    private EngOrder(Name alias, Table<EngOrderRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.eng_order</code> table reference
     */
    public EngOrder(String alias) {
        this(DSL.name(alias), ENG_ORDER);
    }

    /**
     * Create an aliased <code>public.eng_order</code> table reference
     */
    public EngOrder(Name alias) {
        this(alias, ENG_ORDER);
    }

    /**
     * Create a <code>public.eng_order</code> table reference
     */
    public EngOrder() {
        this(DSL.name("eng_order"), null);
    }

    public <O extends Record> EngOrder(Table<O> child, ForeignKey<O, EngOrderRecord> key) {
        super(child, key, ENG_ORDER);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<EngOrderRecord, Integer> getIdentity() {
        return (Identity<EngOrderRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<EngOrderRecord> getPrimaryKey() {
        return Keys.ENG_ORDER_PKEY;
    }

    @Override
    public List<ForeignKey<EngOrderRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ENG_ORDER__FK_SUB_ITEM_ID);
    }

    private transient SubItem _subItem;

    public SubItem subItem() {
        if (_subItem == null)
            _subItem = new SubItem(this, Keys.ENG_ORDER__FK_SUB_ITEM_ID);

        return _subItem;
    }

    @Override
    public EngOrder as(String alias) {
        return new EngOrder(DSL.name(alias), this);
    }

    @Override
    public EngOrder as(Name alias) {
        return new EngOrder(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EngOrder rename(String name) {
        return new EngOrder(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EngOrder rename(Name name) {
        return new EngOrder(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, Integer, String, String, String, String, String, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
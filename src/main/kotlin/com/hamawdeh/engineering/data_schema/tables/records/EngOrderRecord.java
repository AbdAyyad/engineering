/*
 * This file is generated by jOOQ.
 */
package com.hamawdeh.engineering.data_schema.tables.records;


import com.hamawdeh.engineering.data_schema.tables.EngOrder;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EngOrderRecord extends UpdatableRecordImpl<EngOrderRecord> implements Record8<Integer, Integer, String, String, String, String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.eng_order.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.eng_order.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.eng_order.sub_item_id</code>.
     */
    public void setSubItemId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.eng_order.sub_item_id</code>.
     */
    public Integer getSubItemId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.eng_order.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.eng_order.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.eng_order.role</code>.
     */
    public void setRole(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.eng_order.role</code>.
     */
    public String getRole() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.eng_order.phone</code>.
     */
    public void setPhone(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.eng_order.phone</code>.
     */
    public String getPhone() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.eng_order.address</code>.
     */
    public void setAddress(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.eng_order.address</code>.
     */
    public String getAddress() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.eng_order.notes</code>.
     */
    public void setNotes(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.eng_order.notes</code>.
     */
    public String getNotes() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.eng_order.created</code>.
     */
    public void setCreated(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.eng_order.created</code>.
     */
    public LocalDateTime getCreated() {
        return (LocalDateTime) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Integer, Integer, String, String, String, String, String, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Integer, Integer, String, String, String, String, String, LocalDateTime> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return EngOrder.ENG_ORDER.ID;
    }

    @Override
    public Field<Integer> field2() {
        return EngOrder.ENG_ORDER.SUB_ITEM_ID;
    }

    @Override
    public Field<String> field3() {
        return EngOrder.ENG_ORDER.NAME;
    }

    @Override
    public Field<String> field4() {
        return EngOrder.ENG_ORDER.ROLE;
    }

    @Override
    public Field<String> field5() {
        return EngOrder.ENG_ORDER.PHONE;
    }

    @Override
    public Field<String> field6() {
        return EngOrder.ENG_ORDER.ADDRESS;
    }

    @Override
    public Field<String> field7() {
        return EngOrder.ENG_ORDER.NOTES;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return EngOrder.ENG_ORDER.CREATED;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getSubItemId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public String component4() {
        return getRole();
    }

    @Override
    public String component5() {
        return getPhone();
    }

    @Override
    public String component6() {
        return getAddress();
    }

    @Override
    public String component7() {
        return getNotes();
    }

    @Override
    public LocalDateTime component8() {
        return getCreated();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getSubItemId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public String value4() {
        return getRole();
    }

    @Override
    public String value5() {
        return getPhone();
    }

    @Override
    public String value6() {
        return getAddress();
    }

    @Override
    public String value7() {
        return getNotes();
    }

    @Override
    public LocalDateTime value8() {
        return getCreated();
    }

    @Override
    public EngOrderRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public EngOrderRecord value2(Integer value) {
        setSubItemId(value);
        return this;
    }

    @Override
    public EngOrderRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public EngOrderRecord value4(String value) {
        setRole(value);
        return this;
    }

    @Override
    public EngOrderRecord value5(String value) {
        setPhone(value);
        return this;
    }

    @Override
    public EngOrderRecord value6(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public EngOrderRecord value7(String value) {
        setNotes(value);
        return this;
    }

    @Override
    public EngOrderRecord value8(LocalDateTime value) {
        setCreated(value);
        return this;
    }

    @Override
    public EngOrderRecord values(Integer value1, Integer value2, String value3, String value4, String value5, String value6, String value7, LocalDateTime value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EngOrderRecord
     */
    public EngOrderRecord() {
        super(EngOrder.ENG_ORDER);
    }

    /**
     * Create a detached, initialised EngOrderRecord
     */
    public EngOrderRecord(Integer id, Integer subItemId, String name, String role, String phone, String address, String notes, LocalDateTime created) {
        super(EngOrder.ENG_ORDER);

        setId(id);
        setSubItemId(subItemId);
        setName(name);
        setRole(role);
        setPhone(phone);
        setAddress(address);
        setNotes(notes);
        setCreated(created);
    }
}

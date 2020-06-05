package org.jeecg.modules.business.utils;

import java.util.Objects;

/**
 * 不同的属性
 *
 * @author dadiyang
 */
public class FieldInfo {
    private String fieldName;
    private Class<?> fieldType;
    private Object firstVal;
    private Object secondVal;

    public FieldInfo() {
    }

    public FieldInfo(String fieldName, Class<?> fieldType, Object firstVal, Object secondVal) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.firstVal = firstVal;
        this.secondVal = secondVal;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public Object getFirstVal() {
        return firstVal;
    }

    public void setFirstVal(Object firstVal) {
        this.firstVal = firstVal;
    }

    public Object getSecondVal() {
        return secondVal;
    }

    public void setSecondVal(Object secondVal) {
        this.secondVal = secondVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(fieldName, fieldInfo.fieldName) &&
                Objects.equals(fieldType, fieldInfo.fieldType) &&
                Objects.equals(firstVal, fieldInfo.firstVal) &&
                Objects.equals(secondVal, fieldInfo.secondVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldType, firstVal, secondVal);
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType=" + fieldType +
                ", firstVal=" + firstVal +
                ", secondVal=" + secondVal +
                '}';
    }
}

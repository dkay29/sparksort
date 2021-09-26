package com.dkay29.spark.sort;

import java.util.Objects;

/**
 * Sort key is a field within data that sorting is performed on. It may be a TEXT, BINARY or NUMMERCsort and may be ascending or descending.
 */
public class SortKey {
    private final int fieldNumber;
    private final SortTypeEnum sortType;

    public int getFieldNumber() {
        return fieldNumber;
    }

    public SortTypeEnum getSortType() {
        return sortType;
    }

    public SortKey(int fieldNumber, SortTypeEnum sortType) {
        if (fieldNumber<1) {
            throw new RuntimeException("Filed number nust be greater than zero : "+fieldNumber);
        }
        this.fieldNumber = fieldNumber;
        this.sortType = sortType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortKey sortKey = (SortKey) o;
        return fieldNumber == sortKey.fieldNumber && sortType == sortKey.sortType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldNumber, sortType);
    }

    @Override
    public String toString() {
        return "SortKey{" +
                "fieldNumber=" + fieldNumber +
                ", sortType=" + sortType +
                '}';
    }
}

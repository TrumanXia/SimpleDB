package simpledb;

import java.util.*;
import java.util.stream.Stream;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc {
    private Type[] types = new Type[0];
    private String[] fieldNames = new String[0];

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields, with the first td1.numFields coming
     * from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc combine(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        Type[] type1 = td1.types;
        Type[] types2 = td2.types;
        Type[] newTypes = new Type[type1.length + types2.length];
        System.arraycopy(type1, 0, newTypes, 0, type1.length);
        System.arraycopy(types2, 0, newTypes, type1.length, types2.length);
        
        String[] fieldNames1 = td1.fieldNames;
        String[] fieldNames2 = td2.fieldNames;
        String[] combinedFieldNames = new String[fieldNames1.length + fieldNames2.length];
        System.arraycopy(fieldNames1, 0, combinedFieldNames, 0, fieldNames1.length);
        System.arraycopy(fieldNames2, 0, combinedFieldNames, fieldNames1.length, fieldNames2.length);
        /*Type[] newTypes = merge(td1.types, td2.types);
        String[] combinedFieldNames = merge(td1.fieldNames, td2.fieldNames);*/
        return new TupleDesc(newTypes, combinedFieldNames);
    }

    // TODO
    private static <T> T[] merge(T[] t1, T[] t2) {
        int len1 = t1.length;
        int len2 = t2.length;
        T[] newTypes = (T[])new Object[len1 + len2];
        System.arraycopy(t1, 0, newTypes, 0, len1);
        System.arraycopy(t2, 0, newTypes, len1, len2);
        return newTypes;
    }

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the specified types, with associated named
     * fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this TupleDesc. It must contain at least one
     *            entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        if (typeAr.length < 1) {
            throw new IllegalArgumentException("at least one type entry");
        }
        if (fieldAr.length > typeAr.length) {
            throw new IllegalArgumentException("name num exceed type num");
        }
        types = typeAr;
        if (fieldAr.length < typeAr.length) {
            fieldNames = new String[typeAr.length];
            System.arraycopy(fieldAr, 0, fieldNames, 0, fieldAr.length);
        } else {
            fieldNames = fieldAr;
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with fields of the specified types, with anonymous
     * (unnamed) fields.
     *
     * @param typeAr
     *            array specifying the number of and types of fields in this TupleDesc. It must contain at least one
     *            entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        if (typeAr.length < 1) {
            throw new IllegalArgumentException("at least one type entry");
        }
        types = typeAr;
        fieldNames = new String[typeAr.length];
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return types.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if (i < 0 || i >= fieldNames.length) {
            throw new NoSuchElementException();
        }
        return fieldNames[i];
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int nameToId(String name) throws NoSuchElementException {
        // some code goes here
        if (Objects.isNull(name)) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < fieldNames.length; i++) {
            if (name.equals(fieldNames[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i
     *            The index of the field to get the type of. It must be a valid index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getType(int i) throws NoSuchElementException {
        // some code goes here
        if (i < 0 || i >= types.length) {
            throw new NoSuchElementException();
        }
        return types[i];
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc. Note that tuples from a given TupleDesc
     *         are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int size = 0;
        for (Type type : types) {
            size += type.getLen();
        }
        return size;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two TupleDescs are considered equal if they are
     * the same size and if the n-th type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        // some code goes here
        if (!(o instanceof TupleDesc)) {
            return false;
        }
        TupleDesc that = (TupleDesc)o;
        if (this.types.length != that.types.length) {
            return false;
        }
        for (int i = 0; i < this.types.length; i++) {
            if (!this.types[i].getClass().getSimpleName().equals(that.types[i].getClass().getSimpleName())) {
                return false;
            }
            if ((this.fieldNames[i] != null && that.fieldNames[i] == null)
                || this.fieldNames[i] == null && that.fieldNames[i] != null) {
                return false;
            }
            if (this.fieldNames[i] == null && that.fieldNames[i] == null) {
                continue;
            }
            if (!this.fieldNames[i].equals(that.fieldNames[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form "fieldType[0](fieldName[0]), ...,
     * fieldType[M](fieldName[M])", although the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            builder.append(types[i].getClass().getSimpleName());
            builder.append("(");
            builder.append(fieldNames[i]);
            builder.append("),");
        }
        return builder.toString().substring(0, builder.toString().length() - 1);
    }
}

package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

import java.io.Serializable;

/**
 * Represents a single attribute of a service definition.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class AttributeInfo implements Serializable {

    private static final long serialVersionUID = -6704474615491645869L;
    @SerializedName(DataParser.VARIABLE_TAG)
    private Boolean variable;
    @SerializedName(DataParser.CODE_TAG)
    private String code;
    @SerializedName(DataParser.DATATYPE_TAG)
    private Datatype datatype;
    @SerializedName(DataParser.REQUIRED_TAG)
    private Boolean required;
    @SerializedName(DataParser.DATATYPE_DESCRIPTION_TAG)
    private String datatypeDescription;
    private Integer order;
    private String description;
    /**
     * Pair key, name. Check the attribute section of the <a
     * href="http://wiki.open311.org/GeoReport_v2#GET_Service_Definition"
     * >Service Definition</a>.
     */
    private Value[] values;

    public AttributeInfo(Boolean variable, String code, Datatype datatype,
                         Boolean required, String datatypeDescription, Integer order,
                         String description, Value[] values) {
        super();
        this.variable = variable;
        this.code = code;
        this.datatype = datatype;
        this.required = required;
        this.datatypeDescription = datatypeDescription;
        this.order = order;
        this.description = description;
        this.values = values;
    }

    public Boolean isVariable() {
        return variable;
    }

    public String getCode() {
        return code;
    }

    public Datatype getDatatype() {
        if (datatype == null) {
            return Datatype.SINGLEVALUELIST;
        } else {
            return datatype;
        }

    }

    public Integer getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    public Value[] getValues() {
        return values;
    }

    public Boolean isRequired() {
        return required;
    }

    public String getDatatypeDescription() {
        if (datatypeDescription == null) {
            return "";
        } else {
            return datatypeDescription;
        }
    }

    public enum Datatype {
        STRING, NUMBER, DATETIME, TEXT, SINGLEVALUELIST, MULTIVALUELIST;

        /**
         * Returns an instance of this class from a given string.
         *
         * @param datatype String representation of the datatype.
         * @return <code>null</code> if the string is not one of the contained
         * types.
         */
        public static Datatype getFromString(String datatype) {
            if (datatype.toLowerCase().equals("string")) {
                return STRING;
            }
            if (datatype.toLowerCase().equals("number")) {
                return NUMBER;
            }
            if (datatype.toLowerCase().equals("datetime")) {
                return DATETIME;
            }
            if (datatype.toLowerCase().equals("text")) {
                return TEXT;
            }
            if (datatype.toLowerCase().equals("singlevaluelist")) {
                return SINGLEVALUELIST;
            }
            if (datatype.toLowerCase().equals("multivaluelist")) {
                return MULTIVALUELIST;
            }
            return null;
        }
    }
}

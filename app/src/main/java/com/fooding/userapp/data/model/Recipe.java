package com.fooding.userapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("RID")
    @Expose
    private String id;
    @SerializedName("RNAME")
    @Expose
    private String name;
    @SerializedName("CID")
    @Expose
    private String c_id;
    @SerializedName("CNAME")
    @Expose
    private String c_name;

    public String getId() { return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getC_id() {
        return c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }
}

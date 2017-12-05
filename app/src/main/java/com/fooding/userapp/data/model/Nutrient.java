package com.fooding.userapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ajidmasterz on 12/5/2017.
 */

public class Nutrient {
    @SerializedName("...")
    @Expose
    private String id;
    @SerializedName("...")
    @Expose
    private String name;

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
}

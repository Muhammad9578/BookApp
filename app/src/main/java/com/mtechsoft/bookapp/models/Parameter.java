package com.mtechsoft.bookapp.models;

public class Parameter {
    String key;
    String value;

    public Parameter(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getKey()).append(":").append(getValue());
        return builder.toString();
    }
}

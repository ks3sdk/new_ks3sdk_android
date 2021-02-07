package com.ksyun.ks3.services.request.tag;

import com.ksyun.ks3.model.result.policy.BucketPolicyActionCode;
import com.ksyun.ks3.model.result.policy.BucketPolicyRule;

import java.io.Serializable;

public class ObjectTag implements Serializable {
    private String key;
    private String value;

    public ObjectTag() {
    }

    public ObjectTag(String key, String value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectTag tag = (ObjectTag) o;

        if (key != null ? !key.equals(tag.key) : tag.key != null) {
            return false;
        }

        return value != null ? value.equals(tag.value) : tag.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
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
        return "ObjectTag{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

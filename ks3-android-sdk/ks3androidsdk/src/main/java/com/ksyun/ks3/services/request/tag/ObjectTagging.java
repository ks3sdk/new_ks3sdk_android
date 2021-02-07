package com.ksyun.ks3.services.request.tag;

import com.ksyun.ks3.model.result.policy.BucketPolicyRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectTagging implements Serializable {
    protected List<ObjectTag> tagSet;

    public ObjectTagging() {}

    public ObjectTagging(List<ObjectTag> tagSet) {
        this.tagSet = tagSet;
    }

    public List<ObjectTag> getTagSet() {
        return tagSet;
    }

    /**
     * 添加ObjectTag
     *
     * @return
     */
    public ObjectTagging addObjectTag(String key, String value) {
        if (tagSet == null){
            tagSet = new ArrayList<>();
        }
        tagSet.add(new ObjectTag(key,value));
        return this;
    }
    public void setTagSet(List<ObjectTag> tagSet) {
        this.tagSet = tagSet;
    }

    @Override
    public String toString() {
        return "ObjectTagging{" +
                "tagSet=" + tagSet +
                '}';
    }
}


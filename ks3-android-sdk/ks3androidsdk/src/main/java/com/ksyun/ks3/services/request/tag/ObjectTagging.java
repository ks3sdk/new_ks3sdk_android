package com.ksyun.ks3.services.request.tag;

import com.ksyun.ks3.model.result.policy.BucketPolicyRule;
import com.ksyun.ks3.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectTagging implements Serializable {

    protected List<ObjectTag> tagSet;

    /*
     *指定如何设置目标Object的对象标签。
     * 默认值：COPY
     * 1. COPY（默认值）：复制源Object的对象标签到目标 Object。
     * 2. REPLACE：忽略源Object的对象标签，直接采用请求中指定的对象标签。
     */
    private String taggingDirective = "COPY";


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

    public String getTaggingDirective() {
        if (StringUtils.isBlank(taggingDirective)){
            taggingDirective = "COPY";
        }
        return taggingDirective;
    }

    public void setTaggingDirective(String taggingDirective) {
        this.taggingDirective = taggingDirective;
    }
    @Override
    public String toString() {
        return "ObjectTagging{" +
                "tagSet=" + tagSet +
                '}';
    }
}


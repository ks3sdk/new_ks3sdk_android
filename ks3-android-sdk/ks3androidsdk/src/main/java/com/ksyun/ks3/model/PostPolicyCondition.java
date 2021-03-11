package com.ksyun.ks3.model;

public class PostPolicyCondition {
    public static enum MatchingType{
        //相等
        eq("eq"),
        //以xxx开头
        startsWith("starts-with"),
        //指定content-length的范围
        contentLengthRange("content-length-range");
        String value;
        private MatchingType(String value){
            this.value = value;
        }
        @Override
        public String toString(){
            return value;
        }
    }
    //匹配规则
    private MatchingType matchingType;
    /**
     * 需要的第一个参数
     * 当为eq或starts-with时，该参数表示表单项的名称,需要在表单项名称前面加$符号<br/>
     * 当为content-length-range时，该参数表示content-length的最小值
     *
     */
    private String paramA;
    /**
     * 需要的第一个参数
     * 当为eq或starts-with时，该参数表示表单项的值<br/>
     * 当为content-length-range时，该参数表示content-length的最大值
     *
     * */
    private String paramB;
    public MatchingType getMatchingType() {
        return matchingType;
    }
    public void setMatchingType(MatchingType matchingType) {
        this.matchingType = matchingType;
    }
    public String getParamA() {
        return paramA;
    }
    /**
     * 需要的第一个参数
     * 当为eq或starts-with时，该参数表示表单项的名称,需要在表单项名称前面加$符号<br/>
     * 当为content-length-range时，该参数表示content-length的最小值
     *
     */
    public void setParamA(String paramA) {
        this.paramA = paramA;
    }
    public String getParamB() {
        return paramB;
    }
    /**
     * 需要的第一个参数
     * 当为eq或starts-with时，该参数表示表单项的值<br/>
     * 当为content-length-range时，该参数表示content-length的最大值
     *
     * */
    public void setParamB(String paramB) {
        this.paramB = paramB;
    }

}

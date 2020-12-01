package com.ksyun.ks3.model.result;

import java.util.List;

public class BucketPolicy {

    public List<PolicyStatement> getStatement() {
        return Statement;
    }

    public void setStatement(List<PolicyStatement> statement) {
        Statement = statement;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    /**
     * 版本
     */
    private String Version;

    /**
     * 桶策略
     */
    private List<PolicyStatement> Statement;


    public static class PolicyStatement {

        /**
         * 名称
         */
        private String Effect;

        /**
         * 接口名称
         */
        private String Action;

        /**
         * 资源地址
         */
        private String Resource;

        public PolicyStatement(String effect, String action, String resource) {
            Effect = effect;
            Action = action;
            Resource = resource;
        }


        public String getEffect() {
            return Effect;
        }

        public void setEffect(String effect) {
            Effect = effect;
        }

        public String getAction() {
            return Action;
        }

        public void setAction(String action) {
            Action = action;
        }

        public String getResource() {
            return Resource;
        }

        public void setResource(String resource) {
            Resource = resource;
        }
    }

}


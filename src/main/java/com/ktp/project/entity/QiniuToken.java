package com.ktp.project.entity;

import java.io.Serializable;

/**
 * 七牛上传凭证实体
 */
public class QiniuToken implements Serializable {

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
}

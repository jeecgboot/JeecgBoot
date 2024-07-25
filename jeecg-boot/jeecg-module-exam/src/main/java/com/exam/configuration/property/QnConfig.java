package com.exam.configuration.property;


/**
 * @version 3.5.0
 * @description: The type Qn config.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class QnConfig {

    private String url;
    private String bucket;
    private String accessKey;
    private String secretKey;

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets bucket.
     *
     * @return the bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * Sets bucket.
     *
     * @param bucket the bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * Gets access key.
     *
     * @return the access key
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Sets access key.
     *
     * @param accessKey the access key
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * Gets secret key.
     *
     * @return the secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets secret key.
     *
     * @param secretKey the secret key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}

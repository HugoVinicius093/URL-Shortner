package com.hugo.urlshortner.service;

import java.util.Base64;

public final class Base64UrlCommonService {

    public static String encodeString(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes());
    }

    public static String decodeString(String value) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(value);
        return new String(decodedBytes);
    }
}

package com.example.common;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author houguangqiang
 * @date 2018-09-21
 * @since 1.0
 */
public class RedisKey {

    private static final String PREFIX = "USER_CENTER";

    private RedisKey() {}

    public static String joinKey(Object... values) {
        return Arrays.stream(values).map(Object::toString).collect(Collectors.joining(":"));
    }

    public static final class Login {
        private Login() {}
        private static final String LOGIN_PREFIX = "LOGIN";
        private static final String UUID_AS_VALUE = joinKey(PREFIX, LOGIN_PREFIX, "UUID");
        private static final String USER_ID_AS_HASH = joinKey(PREFIX, LOGIN_PREFIX, "USER_ID");
        private static final String CAPTCHA_ID_AS_VALUE = joinKey(PREFIX, LOGIN_PREFIX, "CAPTCHA");

        public static String uuidAsValue(String uuid) {
            return joinKey(UUID_AS_VALUE, uuid);
        }

        public static String userIdAsHash(Long userId) {
            return joinKey(USER_ID_AS_HASH, userId);
        }

        public static String captchaIdAsValue(String captchaId) {
            return joinKey(Login.CAPTCHA_ID_AS_VALUE, captchaId);
        }
    }


    public static final class UserInfoModel {
        private UserInfoModel() {}
        private static final String USER_INFO_MODEL_PREFIX = "USER_INFO_MODEL";
        private static final String USER_ID = joinKey(PREFIX, USER_INFO_MODEL_PREFIX, "USER_ID");

        public static String userIdAsValue(Long userId) {
            return joinKey(USER_ID, userId);
        }
    }
}
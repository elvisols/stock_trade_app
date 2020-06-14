package com.iex.stocktrading.config;

/**
 * Application constants.
 */
public final class Constants {

    public static final int PASSWORD_MIN_LENGTH = 6;

    public static final String LOGIN_URL = "/auth/login";

    public static final String REGISTER_URL = "/users";

    public static final String H2_URL = "/h2-console/**";

    public static final String SECRET = "MySecretKeyJWT";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String IEX_TOKEN = "pk_50b799876c4541b89d194f994bdc064f";

    public static final String HEADER_STRING = "Authorization";

    public static final long EXPIRATION_TIME = 864_000_000_0L;
}

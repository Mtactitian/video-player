package com.alexb.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilConstants {

    public static final  int DEFAULT_BUFFER_SIZE = 20480; // ..bytes = 20KB.
    public static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
}

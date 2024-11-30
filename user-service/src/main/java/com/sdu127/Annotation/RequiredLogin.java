package com.sdu127.Annotation;

import com.sdu127.Data.Constant.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredLogin {
    boolean required() default true;

    Role[] roles() default {Role.ADMIN};
}

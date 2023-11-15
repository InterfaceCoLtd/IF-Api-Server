package xyz.interfacesejong.interfaceapi.global.aop;

import xyz.interfacesejong.interfaceapi.global.fcm.domain.ContentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushNotification {
    ContentType topic() default ContentType.NONE;
}

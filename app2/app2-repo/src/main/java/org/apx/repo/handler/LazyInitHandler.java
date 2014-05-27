package org.apx.repo.handler;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LazyInitHandler {
	Class<? extends IDBHandler> handlerClass();
	boolean override() default false;
}

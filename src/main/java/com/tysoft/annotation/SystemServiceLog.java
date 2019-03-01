package com.tysoft.annotation;

import java.lang.annotation.*;
/**
 * Title: SystemControllerLog 
 **@date 2019年1月3日 
 **@version V1.0 * Description: 
   *    自定义注解，拦截service 
 **/
@Documented
@Inherited
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public  @interface SystemServiceLog {
	String description() default "";
}


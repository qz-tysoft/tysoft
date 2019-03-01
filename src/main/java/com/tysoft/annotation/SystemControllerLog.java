package com.tysoft.annotation;

import java.lang.annotation.*;
/** Title: SystemControllerLog
 * @date 2019年3月1日
 * @version V1.0
 ** Description:
  自定义注解，拦截controller
 */    
@Documented 
@Inherited
@Target({ElementType.PARAMETER, ElementType.METHOD})
//作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME)
public  @interface SystemControllerLog { 
	String description() default "";
  }

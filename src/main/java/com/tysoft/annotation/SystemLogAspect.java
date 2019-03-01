package com.tysoft.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.tysoft.common.IpUtils;
import com.tysoft.common.JsonUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Title: SystemControllerLog
 * @date 2019年3月1日
 * @version V1.0
 * Description: 切点类
 */
@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {

	//本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    //Service层切点
    @Pointcut("@annotation(com.tysoft.annotation.SystemServiceLog)")
    public void serviceAspect(){

    }
    
    //Controller层切点
    @Pointcut("@annotation(com.tysoft.annotation.SystemControllerLog)")
    public void controllerAspect(JoinPoint joinPoint){
    //	/tysoft/src/main/java/com/tysoft/annotation/SystemServiceLog.java
    }

    
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();        HttpSession session = request.getSession();
    	HttpSession baseSession = request.getSession();
    	String ip = IpUtils.getIpAddr(request);
    	try {
			System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
	    	System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName())); 
            System.out.println("ip:"+ip);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @Description  异常通知 用于拦截service层记录异常日志
     * @date 2019年3月1日 下午5:43
     */
    @AfterThrowing(pointcut = "serviceAspect()")
    public void doAfterThrowing(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String ip = IpUtils.getIpAddr(request);
        String params = "";
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
        	for (int i = 0; i < joinPoint.getArgs().length; i++) {
        	params+= JsonUtils.objectToJson(joinPoint.getArgs()[i])+";";
          }
        }
        
        System.out.println("服务层请求参数:" + params+" 电脑ip:"+ip);
    }
    /**
     * @Description  获取注解中对方法的描述信息 用于service层注解
     * @date 2019年3月1日 下午5:05
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint)throws Exception{
    	String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
        	if (method.getName().equals(methodName)){ 
        		Class[] clazzs = method.getParameterTypes();
        		if (clazzs.length==arguments.length){ 
        			description = method.getAnnotation(SystemServiceLog.class).description(); 
        			break;
        			}
        		}
        	}
        return description;
    }
    
    /**
     * @author hxx
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date 2019年3月1日 上午12:01
     */
       public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
    	   String targetName = joinPoint.getTarget().getClass().getName();        String methodName = joinPoint.getSignature().getName();
    	   //目标方法名
    	   Object[] arguments = joinPoint.getArgs();
    	   Class targetClass = Class.forName(targetName);
    	   Method[] methods = targetClass.getMethods();
    	   String description = ""; 
    	   for (Method method:methods) {
    		   if (method.getName().equals(methodName)){
    			   Class[] clazzs = method.getParameterTypes();
    			   if (clazzs.length==arguments.length){
    				   description = method.getAnnotation(SystemControllerLog.class).description();
    				   break;
    				   }
    			   }
    		   }
    	   return description;
       }
}
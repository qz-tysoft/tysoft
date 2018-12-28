package com.tysoft;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableAsync//开启异步任务
/*@ImportResource("classpath:context.xml")//加载配置文件
*/public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}


	/**
	 * 设置默认登陆页面
	 * @author Administrator
	 *
	 */
	@Configuration
	public class DefaultView extends WebMvcConfigurerAdapter{
		@Override
		public void addViewControllers( ViewControllerRegistry registry ) {
			registry.addViewController( "/" ).setViewName( "forward:/login.html" );
			registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
			super.addViewControllers( registry );
		} 
	}

	/**
	 * 设置sessin周期
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer(){
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.setSessionTimeout(0);//session生命周期
			}
		};
	}
	/**
	 * socket配置
	 * @author Administrator
	 *
	 */
	@Configuration
	public class WebSocketConfig {
		@Bean
		public ServerEndpointExporter serverEndpointExporter() {
			return new ServerEndpointExporter();
		}
	}

	@Bean
	public ServletRegistrationBean buildServlet(){

		return new ServletRegistrationBean(new com.bstek.uflo.console.UfloServlet(),"/uflo/*");
	}
}

package com.myappteam.microservice.auth;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig implements AsyncConfigurer{

	@Override
	public Executor getAsyncExecutor() {
		 ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
         executor.setCorePoolSize(7);
         executor.setMaxPoolSize(42);
         executor.setQueueCapacity(11);
         executor.setThreadNamePrefix("MyExecutor-");
         executor.initialize();
         return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		 return MyAsyncUncaughtExceptionHandler();
	}

	private AsyncUncaughtExceptionHandler MyAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return new AsyncUncaughtExceptionHandler() {
			
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				// TODO Auto-generated method stub
				
			}
		};
	}

}

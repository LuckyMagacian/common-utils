package com.lanxi.util.utils;

import org.aspectj.lang.ProceedingJoinPoint;

public class AopUtil {
	public static Class<?> getTargetClass(ProceedingJoinPoint point){
		return point.getTarget().getClass();
	}
	public static Object[] getArgs(ProceedingJoinPoint point) {
		return point.getArgs();
	}
	public static Object execute(ProceedingJoinPoint point,Object[] args) {
		try {
			return point.proceed(args);
		} catch (Throwable e) {
			throw new RuntimeException("exception happened when aop method invoke !",e);
		}
	}
	public static String getTargetMethodName(ProceedingJoinPoint point) {
		return point.getSignature().getName();
	}
}

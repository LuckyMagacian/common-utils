package com.lanxi.util.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.lanxi.util.entity.GenericRetMessage;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.entity.RetMessage;

import static com.lanxi.util.consts.RetCodeEnum.*;

public class TaskUtil {
	
	public static RetMessage doTaskMatchAll(CommonTask task,Object[] args,Map<String, Object>targetAndExcpectation,int retryTimes,long sleepMilliesSeconds){
		RetMessage result=null;
		String taskResult=null;
		LogFactory.info(TaskUtil.class, "尝试执行任务["+task.getClass()+"],参数["+Arrays.asList(args)+"],重试次数["+retryTimes+"],目标["+targetAndExcpectation+"]");
		int currentTime=0;
		while(currentTime<retryTimes){
			try {
				LogFactory.info(TaskUtil.class, "开始执行任务,当前次数["+currentTime+"]");
				taskResult=task.beExcuted(args);
				JSONObject jResult=JSONObject.parseObject(taskResult);
				if(targetAndExcpectation!=null){
					for(Entry<String, Object> each:targetAndExcpectation.entrySet()){
						String property=each.getKey();
						Object value=each.getValue();
						if (property==null){
							continue;
						}else if(value==null){
							if(jResult.get(property)!=null){
								result= new RetMessage(FAIL, "任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}else{
							if(!jResult.getObject(property, value.getClass()).equals(value)){
								result= new RetMessage(FAIL, "任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}
					}
					if(result==null){
						result= new RetMessage(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
						LogFactory.info(TaskUtil.class, result.getMessage());
					}
				}else{
					result= new RetMessage(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
				}
				if(result.getResult().equals(SUCCESS))
					return result;
				currentTime++;
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					Thread.sleep(sleepMilliesSeconds);
				}
			} catch (Exception e) {
				LogFactory.warn(TaskUtil.class,"发生异常,尝试重试,当前结果["+taskResult+"]");
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					try {
						Thread.sleep(sleepMilliesSeconds);
					} catch (Exception o) {
						LogFactory.warn(TaskUtil.class, "休眠时发生异常");
						throw new RuntimeException("执行任务休眠时发生异常", o);
					}
				}
				if(currentTime>=retryTimes){
					result= new RetMessage(EXCEPTION, "任务失败,当前结果["+taskResult+"]", taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
					return result;
				}
			}
		}
		return result;
	}
	public static RetMessage doTaskMatchOne(CommonTask task,Object[] args,Map<String, Object>targetAndExcpectation,int retryTimes,long sleepMilliesSeconds){
		RetMessage result=null;
		String taskResult=null;
		LogFactory.info(TaskUtil.class, "尝试执行任务["+task.getClass()+"],参数["+Arrays.asList(args)+"],重试次数["+retryTimes+"],目标["+targetAndExcpectation+"]");
		int currentTime=0;
		while(currentTime<retryTimes){
			try {
				LogFactory.info(TaskUtil.class, "开始执行任务,当前次数["+currentTime+"]");
				taskResult=task.beExcuted(args);
				JSONObject jResult=JSONObject.parseObject(taskResult);
				if(targetAndExcpectation!=null){
					for(Entry<String, Object> each:targetAndExcpectation.entrySet()){
						String property=each.getKey();
						Object value=each.getValue();
						if (property==null){
							continue;
						}else if(value==null){
							if(jResult.get(property)==null){
								result= new RetMessage(SUCCESS, "任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}else{
							if(jResult.getObject(property, value.getClass()).equals(value)){
								result= new RetMessage(SUCCESS, "任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}
					}
					if(result==null){
						result= new RetMessage(FAIL,"任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]",taskResult);
						LogFactory.info(TaskUtil.class, result.getMessage());
					}
				}else{
					result= new RetMessage(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
				}
				if(result.getResult().equals(SUCCESS))
					return result;
				currentTime++;
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					Thread.sleep(sleepMilliesSeconds);
				}
			} catch (Exception e) {
				LogFactory.warn(TaskUtil.class,"发生异常,尝试重试,当前结果["+taskResult+"]");
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					try {
						Thread.sleep(sleepMilliesSeconds);
					} catch (Exception o) {
						LogFactory.warn(TaskUtil.class, "执行任务休眠时发生异常");
						throw new RuntimeException("执行任务休眠时发生异常", o);
					}
				}
				if(currentTime>=retryTimes){
					result= new RetMessage(EXCEPTION, "任务失败,当前结果["+taskResult+"]", taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
					return result;
				}
			}
		}
		return result;
	}
	
	public static <T> GenericRetMessage<T> doTaskMatchAll(GenericTask<T> task,Object[] args,Map<String, Object>targetAndExcpectation,int retryTimes,long sleepMilliesSeconds){
		GenericRetMessage<T> result=null;
		T taskResult=null;
		LogFactory.info(TaskUtil.class, "尝试执行任务["+task.getClass()+"],参数["+Arrays.asList(args)+"],重试次数["+retryTimes+"],目标["+targetAndExcpectation+"]");
		int currentTime=0;
		while(currentTime<retryTimes){
			try {
				LogFactory.info(TaskUtil.class, "开始执行任务,当前次数["+currentTime+"]");
				taskResult=task.beExcuted(args);
				if(targetAndExcpectation!=null){
					for(Entry<String, Object> each:targetAndExcpectation.entrySet()){
						String property=each.getKey();
						Object value=each.getValue();
						if (property==null){
							continue;
						}else if(value==null){
							if(BeanUtil.get(taskResult, property)!=null){
								result= new GenericRetMessage<T>(FAIL, "任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}else{
							if(!BeanUtil.get(taskResult, property).equals(value)){
								result= new GenericRetMessage<T>(FAIL, "任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}
					}
					if(result==null){
						result= new GenericRetMessage<T>(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
						LogFactory.info(TaskUtil.class, result.getMessage());
					}
				}else{
					result= new GenericRetMessage<T>(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
				}
				if(result.getResult().equals(SUCCESS))
					return result;
				currentTime++;
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					Thread.sleep(sleepMilliesSeconds);
				}
			} catch (Exception e) {
				LogFactory.warn(TaskUtil.class,"发生异常,尝试重试,当前结果["+taskResult+"]");
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					try {
						Thread.sleep(sleepMilliesSeconds);
					} catch (Exception o) {
						LogFactory.warn(TaskUtil.class, "休眠时发生异常");
						throw new RuntimeException("执行任务休眠时发生异常", o);
					}
				}
				if(currentTime>=retryTimes){
					result= new GenericRetMessage<T>(EXCEPTION, "任务失败,当前结果["+taskResult+"]", taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
					return result;
				}
			}
		}
		return result;
	}
	public static <T> GenericRetMessage<T> doTaskMatchOne(GenericTask<T> task,Object[] args,Map<String, Object>targetAndExcpectation,int retryTimes,long sleepMilliesSeconds){
		GenericRetMessage<T> result=null;
		T taskResult=null;
		LogFactory.info(TaskUtil.class, "尝试执行任务["+task.getClass()+"],参数["+Arrays.asList(args)+"],重试次数["+retryTimes+"],目标["+targetAndExcpectation+"]");
		int currentTime=0;
		while(currentTime<retryTimes){
			try {
				LogFactory.info(TaskUtil.class, "开始执行任务,当前次数["+currentTime+"]");
				taskResult=task.beExcuted(args);
				if(targetAndExcpectation!=null){
					for(Entry<String, Object> each:targetAndExcpectation.entrySet()){
						String property=each.getKey();
						Object value=each.getValue();
						if (property==null){
							continue;
						}else if(value==null){
							if(BeanUtil.get(taskResult, property)==null){
								result= new GenericRetMessage<T>(SUCCESS, "任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}else{
							if(BeanUtil.get(taskResult, property).equals(value)){
								result= new GenericRetMessage<T>(SUCCESS, "任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]", taskResult);
								LogFactory.info(TaskUtil.class, result.getMessage());
								break;
							}
						}
					}
					if(result==null){
						result= new GenericRetMessage<T>(FAIL,"任务完成,当前次数第["+currentTime+"]次,任务结果不符合期望,当前结果["+taskResult+"]",taskResult);
						LogFactory.info(TaskUtil.class, result.getMessage());
					}
				}else{
					result= new GenericRetMessage<T>(SUCCESS,"任务完成,当前次数第["+currentTime+"]次,任务结果符合期望,当前结果["+taskResult+"]",taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
				}
				if(result.getResult().equals(SUCCESS))
					return result;
				currentTime++;
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					Thread.sleep(sleepMilliesSeconds);
				}
			} catch (Exception e) {
				LogFactory.warn(TaskUtil.class,"发生异常,尝试重试,当前结果["+taskResult+"]");
				if(sleepMilliesSeconds>0){
					LogFactory.info(TaskUtil.class, "配置了休眠时间,休眠["+sleepMilliesSeconds+"]毫秒");
					try {
						Thread.sleep(sleepMilliesSeconds);
					} catch (Exception o) {
						LogFactory.warn(TaskUtil.class, "休眠时发生异常");
						throw new RuntimeException("执行任务休眠时发生异常", o);
					}
				}
				if(currentTime>=retryTimes){
					result= new GenericRetMessage<T>(EXCEPTION, "任务失败,当前结果["+taskResult+"]", taskResult);
					LogFactory.info(TaskUtil.class, result.getMessage());
					return result;
				}
			}
		}
		return result;
	}
	public static interface CommonTask{
		public String beExcuted(Object[] args);
	}
	public static interface  GenericTask<T>{
		public T beExcuted(Object[] args);
	}
}

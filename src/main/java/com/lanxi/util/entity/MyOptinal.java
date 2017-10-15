package com.lanxi.util.entity;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
/**
 * 类似java.util.optinal<br>
 * 增加了maybe,ifNotPreset<br>
 * 未实现optinal其他方法
 * @author yangyuanjian
 *
 * @param <T>
 */
public final class MyOptinal<T> {
	private volatile Optional<T> optional;
	private static final MyOptinal<?> empty=new MyOptinal<>();
	private MyOptinal(){
		optional=null;
	}
	
	public static <T> MyOptinal<T> of(T t){
		MyOptinal<T> myOptinal=new MyOptinal<>();
		myOptinal.optional=Optional.ofNullable(t);
		return myOptinal;
	}
	
	public void ifPresent(Consumer<T> consumer) {
		if(optional!=null)
			optional.ifPresent(consumer);
	}
	public Object ifNotPresent(Callable<Object> callable) {
		if(optional==null||optional.equals(Optional.empty()))
			try {
				return callable.call();
			} catch (Exception e) {
				return null;
			}
		return optional.get();
	}
	public Object maybe(Callable<Object> callable,Consumer<T> consumer) {
		synchronized(optional) {
			if(optional==null||Optional.empty().equals(optional)) {
				try {
					return callable.call();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				optional.ifPresent(consumer);
				return optional.get();
			}
		}
	}
	

	
	public T get() {
		return optional.get();
	}
	
	public static<T> MyOptinal<T> empty(){
		@SuppressWarnings("unchecked")
		MyOptinal<T> t=(MyOptinal<T>) empty;
		return t;
	}

	@Override
	public String toString() {
		return "MyOptinal ["+(optional==null||Optional.empty().equals(optional)?"":optional.get())+"]";
	}
	
	
}

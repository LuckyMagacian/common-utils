package com.lanxi.util.interfaces;

public interface FromBean extends ConfigByBean {
	default public void fromBean(Object arg){
		configByBean(arg);
	}
}

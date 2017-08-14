package com.lanxi.util.interfaces;

import java.lang.reflect.Method;
import java.util.List;
import static com.lanxi.util.utils.BeanUtil.*;
import com.lanxi.util.utils.CheckReplaceUtil;

public interface ToString2 {
	default public String toString2(){
		StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName() + ":[");
		try {
			List<Method> getters = getGetters(this);
			for (Method each : getters) {
				if (!buffer.toString().endsWith("["))
					buffer.append(",");
				buffer.append(CheckReplaceUtil.firstCharLowcase(each.getName().substring(3)) + "=");
				buffer.append(each.invoke(this));
			}
			buffer.append("]");
		} catch (Exception e) {
			throw new RuntimeException("构建bean字符串信息异常", e);
		}
		return buffer.toString();
	}
}

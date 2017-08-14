package com.lanxi.util.entity;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.lanxi.util.utils.LoggerUtil;


public class InitServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public void init(ServletConfig config) throws ServletException {
		LoggerUtil.init();
		ConfigManager.loadConfigs();
	}
}

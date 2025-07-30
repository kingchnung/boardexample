package com.mvc.common.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DriverCleanupListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		//DriverManager.getDrivers()를 통해 등록된 JDBC 드라이버 목록을 가져온다.
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while(drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				//각 드라이버가 현재 웹 애플리케이션의 클래스 로더에 의해 로드된 것인지 확인.
				if(driver.getClass().getClassLoader() == Thread.currentThread().getContextClassLoader()) {
					DriverManager.deregisterDriver(driver);
					System.out.println("[INFO] JDBC 드라이버 해제됨 : " + driver);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//필요 시 초기 작업
	}
	
}

package com.huajie.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class H2DBServerStartListener implements ServletContextListener {

    //H2数据库服务器启动实例
    //private Server server;
    String driver;
    String ds ;
    String url ;
    String urn ;
    String pwd ;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            //加载数据库配置信息
/*            java.util.Properties prop = new java.util.Properties();
//            InputStream in = H2DBServerStartListener.class.getResourceAsStream("/jdbc.properties");
            File jdbcConfig = new File(System.getProperty("catalina.home")+"/conf/jdbc.properties");
            InputStream in = new FileInputStream(jdbcConfig);
            prop.load(in);
            driver = prop.getProperty("h2.jdbc.driverClassName");
            ds = prop.getProperty("dataSource.name");
            url = prop.getProperty("h2.jdbc.url");
            urn = prop.getProperty("h2.jdbc.username");
            pwd = prop.getProperty("h2.jdbc.password");
            System.out.println(ds);
            //若数据源使用为h2
            if ("h2_ds".equals(ds)) {
                //System.out.println("正在启动h2数据库...");
                //使用org.h2.tools.Server这个类创建一个H2数据库的服务并启动服务，由于没有指定任何参数，那么H2数据库启动时默认占用的端口就是8082

                //server = Server.createTcpServer(url,urn,pwd).start();
                //System.out.println("h2数据库启动成功...");
            }*/

        } catch (Exception e) {
            System.out.println("启动h2数据库出错：" + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        if (this.server != null) {
//            // 停止H2数据库
//            this.server.stop();
//            this.server = null;
//        }
        try {
            //若数据源使用为h2
            if ("h2_ds".equals(ds)) {
                System.out.println("正在关闭h2数据库...");
                Class.forName(driver).newInstance();
                Connection conn = DriverManager.getConnection(url, urn, pwd);
                conn.createStatement().executeUpdate("SHUTDOWN");//关闭数据库
                //conn.createSQLQuery("SHUTDOWN").executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("关闭h2数据库出错：" + e.toString());
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) {

    }

}

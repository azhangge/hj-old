/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.util;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author 铁铁
 */
public class DerbyTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, MalformedURLException {
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/exam2;create=true");
        Statement st = conn.createStatement();
        st.execute("CREATE TABLE rerebbs_client_file (id VARCHAR(255), ancestors VARCHAR(255), father_ID VARCHAR(255), file_Abstract VARCHAR(255), file_Ext VARCHAR(255), file_Full_Path VARCHAR(255), file_Name VARCHAR(255), file_Size VARCHAR(255), if_Folder SMALLINT DEFAULT 0, real_Length BIGINT, file_scope VARCHAR(255), secret_Grade VARCHAR(255), upload_Time TIMESTAMP, user_id VARCHAR(255), PRIMARY KEY (id))");
        

    }
}

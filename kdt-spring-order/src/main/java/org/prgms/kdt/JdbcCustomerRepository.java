package org.prgms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class JdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    public static void main(String[] args){
        // 끝날 때 close해줘야해서 맨위에 선언
        Connection connection=null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection=DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt","root","hi01071104");
            statement = connection.createStatement();
            resultSet=statement.executeQuery("select * from customers");
            while (resultSet.next()){
                var name=resultSet.getString("name");
                var customerId= UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id")); // UUID라서 getBytes
                logger.info("customer Id -> {}, name -> {}", customerId,name);
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }finally {
            try {
                if(connection!=null) connection.close();
                if(statement!=null) statement.close();
                if(resultSet!=null) resultSet.close();
            }catch (SQLException exception){
                logger.error("Got error while closing connection", exception);
            }
        }
    }
}

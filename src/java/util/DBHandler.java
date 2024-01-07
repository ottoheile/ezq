package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBHandler {
    public class QueryResult {
        private final ArrayList<Object[]> result;
        private int numberOfColumns;
        
        private QueryResult() {
            result = new ArrayList<>();
            numberOfColumns = 0;
        }
        
        private void addRow(Object[] row) {
            result.add(row);
            numberOfColumns = (row.length > numberOfColumns) ? row.length : numberOfColumns;
        }
        
        public boolean isEmpty() {
            return result.isEmpty();
        }
        
        public int getNumberOfRows() {
            return result.size();
        }
        
        public int getNumberOfColumns() {
            return numberOfColumns;
        }
        
        public Object[] getRow(int index) {
            return result.get(index);
        }
    }
    
    public static QueryResult runQuery(String query, String... params) {
        QueryResult queryResult = new DBHandler().new QueryResult();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
                        
            Properties properties = new Properties();
            properties.load(DBHandler.class.getClassLoader().getResourceAsStream("conf/credentials/db.properties"));
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection(url, username, password);
//            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ezq", "dbscript", "password");
            
            statement = connection.prepareStatement(query);
            if (params.length > 0) {
                for (int i = 1; i <= params.length; i++) {
                    statement.setObject(i, params[i-1]);
                }   
            }
                        
            if (statement.execute()) {
                resultSet = statement.getResultSet();
                
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columns = metaData.getColumnCount();
                
                while (resultSet.next()) {
                    Object[] row = new Object[columns];
                    for(int i = 1; i <= columns; i++) {
                        row[i-1] = resultSet.getObject(i);
                    }
                    queryResult.addRow(row);
                }
            } else {
                queryResult.addRow(new Object[] {statement.getUpdateCount()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {}
        }
        return queryResult;
    }
}

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;

public class Main {
    private static final String insertSQL="insert into employee (name, surname, date_of_receipt) values (?,?,?);";
    private static final String selectSQL="select * from employee";
    private static final String deleteSQL="delete from employee where id=?";
    private static final String updateSQL="update employee set name=?, surname=?, date_of_birth=? where id=?";

    private static final String URL="jdbc:mysql://localhost:3306/mydbtest?autoReconnect=true&useSSL=false";
    private static final String USERNAME="root";
    private static final String PASSWORD="root";

    public static void main(String[] args) {
        Connection connection=getConnection();
        try {
            switch (args[0]) {
                case "-c":
                    createRecord(args, connection);
                    break;
                case "-r":
                    readRecord(args, connection);
                    break;
                case "-u":
                    updateRecord(args,connection);
                    break;
                case "-d":
                    deleteRecord(args,connection);
                    break;
                default:System.exit(0);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static Connection getConnection() {
        Connection connection=null;
        try {
            Driver driver=new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
              e.printStackTrace();
        }
    return connection;
    }

    private static void deleteRecord(String[] args,Connection connection) {
        if (args.length != 2){
            return;}
        try {
            PreparedStatement statement=connection.prepareStatement(deleteSQL);
            statement.setInt(1,Integer.valueOf(args[1]));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void updateRecord(String[] args,Connection connection) throws SQLException {
        if (args.length != 4){
            return;}
        PreparedStatement statement=connection.prepareStatement(updateSQL);
        statement.setString(1,args[1]);
        statement.setString(2,args[2]);
        statement.setDate(3,Date.valueOf(args[3]));
        statement.executeUpdate();
        statement.close();
    }
    private static void readRecord(String[] args,Connection connection) throws SQLException {
        if (args.length != 5){
            return;}
        Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery(selectSQL);
        while (resultSet.next()){
            StringBuilder sb=new StringBuilder();
            sb.append(resultSet.getString(2)+" ");
            sb.append(resultSet.getString(3)+" ");
            sb.append(resultSet.getDate(4));
            System.out.println(sb);
        }
        statement.close();

    }

    private static void createRecord(String[] args,Connection connection) throws SQLException {
        if (args.length != 4){
            return;}
        PreparedStatement preparedStatement=connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,args[1]);
            preparedStatement.setString(2,args[2]);
            preparedStatement.setDate(3,Date.valueOf(args[3]));
            preparedStatement.executeUpdate();
            preparedStatement.close();
    }
}

import java.sql.*;

public class DB {

    static Connection con;
    static DatabaseMetaData dbmd;

    public static void connection() throws Exception {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_portal", "root", "");
            System.out.println((con != null) ? "Connection Successfully." : "Connection Failed.");
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    public static void generateDBMD() {
        try {
            dbmd = con.getMetaData();
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    public static void generatedTables(){
                
    }
}

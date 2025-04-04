import java.sql.*;
import java.util.*;

public class Batch {

    static Scanner sc = new Scanner(System.in);
    static int batch_id;
    static String batch;

    
    static void addNewBatch() {
        System.out.print("Enter first alphabet of batch : ");
        String alpha = sc.next().toUpperCase();
        System.out.print("Enter number of Batch : ");
        int bnum = sc.nextInt();
        batch = alpha + bnum;
        storeBatch();
    }

    static void storeBatch() {
        try {
            boolean exists = checkBatchExists(batch);
            if (!exists) {
                PreparedStatement pst = DB.con.prepareStatement("Insert into batch(batch_name) values(?)");
                pst.setString(1, batch);
                System.out.println((pst.executeUpdate() > 0) ? "Batch " + batch + " is inserted Successfully."
                        : "Batch " + batch + " is not inserted.");
            } else {
                System.out.println("Batch " + batch + " is already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    static void removeBatch() {
        try {
            System.out.print("Enter Batch name : ");
            String removeBatch = sc.next();
            boolean exists = checkBatchExists(removeBatch);
            if (exists) {
                DB.con.setAutoCommit(false);
                PreparedStatement pstRemove = DB.con.prepareStatement("Delete from batch where batch_name=?");
                pstRemove.setString(1, removeBatch);
                pstRemove.executeUpdate();
                System.out.print("You are sure for remove Batch `" + removeBatch + "` : ");
                String sureRemove = sc.next().toLowerCase();
                if (sureRemove.equals("yes")) {
                    DB.con.commit();
                    System.out.println("Batch `" + removeBatch + "` is Removed.");
                } else {
                    DB.con.rollback();
                    System.out.println("Then Batch `" + removeBatch + "` is not Removed.");
                }
            } else {
                System.out.println("Batch `" + removeBatch + "` is not Exist!");
            }
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    static void displayAllBatch() {
        try {
            PreparedStatement pst = DB.con.prepareStatement("Select * from batch");
            ResultSet rs = pst.executeQuery();
            System.out.println("\n\n==> BATCH DATA ");
            System.out.printf("%-12s %-12s%n", "Batch ID", "Batch name");
            while (rs.next()) {
                System.out.printf("%-12s %-12s%n", rs.getInt("batch_id"), rs.getString("batch_name"));
            }
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    static boolean checkBatchExists(String batch) {
        boolean BatchExists = false;
        try {
            PreparedStatement pst1 = DB.con.prepareStatement("Select batch_name from batch");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                while (rs1.next()) {
                    if (batch.equals(rs1.getString("batch_name"))) {
                        BatchExists = true;
                        return BatchExists;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return BatchExists;

    }

}

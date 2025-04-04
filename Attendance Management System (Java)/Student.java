import java.util.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Student {
     static Scanner sc = new Scanner(System.in);

     static Date date;
     static String enrollment_number;
     static int roll_number;
     static String st_first_name;
     static String st_middle_name;
     static String st_last_name;
     static String st_full_name;
     static String batch;

     public static void addNewStudent() {
          try {
               System.out.print("Enter Roll number : ");
               roll_number = Check.numCheck();
               boolean isDupRollNum = false;
               System.out.print("Enter First Name : ");
               st_first_name = Check.withoutSpecialCharacter();
               System.out.print("Enter Middle Name : ");
               st_middle_name = Check.withoutSpecialCharacter();
               System.out.print("Enter Last Name(surname) : ");
               st_last_name = Check.withoutSpecialCharacter();
               st_full_name = st_first_name + " " + st_middle_name + " " + st_last_name;
               System.out.print("This Student enrolled in which Batch : ");

               // Check for, Batch is Exists or not?
               boolean isValidBatch = false;
               while (!isValidBatch) {
                    batch = sc.next().toUpperCase();
                    PreparedStatement pstValidBacth = DB.con.prepareStatement("Select batch_name from batch");
                    ResultSet rsValidBatch = pstValidBacth.executeQuery();
                    while (rsValidBatch.next()) {
                         if (rsValidBatch.getString("batch_name").equalsIgnoreCase(batch)) {
                              isValidBatch = true;
                         }
                    }
                    if (!isValidBatch) {
                         System.out.println(batch + " Batch is not exists");
                         System.out.print("Re-enter Batch : ");
                    }
               }

               // Check for, Given roll number is already have or not?
               PreparedStatement pstValidRollNUm = DB.con.prepareStatement("Select * from student where st_batch=?");
               pstValidRollNUm.setString(1, batch);
               ResultSet rsValidRollNum = pstValidRollNUm.executeQuery();
               while (rsValidRollNum.next()) {
                    if (rsValidRollNum.getInt("roll_number") == roll_number) {
                         isDupRollNum = true;
                    }
               }

               if (!isDupRollNum) {
                    CallableStatement cstAddStudent = DB.con.prepareCall("{call Student_Insertion(?,?,?)}");
                    cstAddStudent.setInt(1, roll_number);
                    cstAddStudent.setString(2, st_full_name);
                    cstAddStudent.setString(3, batch);

                    System.out.println(
                              (cstAddStudent.executeUpdate() > 0) ? "Student Records Stored."
                                        : "Student Records not stored.");
               } else {
                    System.out.println("Roll Number `" + roll_number + "` is taken by any other student in Batch `"
                              + batch + "`.");
                    System.out.println("Then, Your records are not saved.");
               }
          } catch (Exception e) {
               System.out.println("An error occur : " + e);
          }
     }

     static void removeStudent() {
          try {
               boolean isValidBatch = false;
               boolean isValidRollNum = false;
               String batch = "";
               int roll_number = 0;
               System.out.print("Student enrolled in which Batch : ");
               while (!isValidBatch) {
                    batch = sc.next().toUpperCase();
                    PreparedStatement pstValidBacth = DB.con.prepareStatement("Select batch_name from batch");
                    ResultSet rsValidBatch = pstValidBacth.executeQuery();
                    while (rsValidBatch.next()) {
                         if (rsValidBatch.getString("batch_name").equalsIgnoreCase(batch)) {
                              isValidBatch = true;
                         }
                    }
                    if (!isValidBatch) {
                         System.out.println(batch + " Batch is not exists");
                         System.out.print("Re-enter Batch : ");
                    }
               }

               System.out.println("Enter Roll Number : ");
               while (!isValidRollNum) {
                    roll_number = Check.numCheck();
                    PreparedStatement pstValidRollNUm = DB.con
                              .prepareStatement("Select roll_number from student where batch_name=?");
                    pstValidRollNUm.setString(1, batch);
                    ResultSet rsValidRollNum = pstValidRollNUm.executeQuery();
                    while (rsValidRollNum.next()) {
                         if (rsValidRollNum.getInt("roll_number") == roll_number) {
                              isValidRollNum = true;
                         }
                    }
                    if (!isValidRollNum) {
                         System.out.println("`" + roll_number + "` is Not exists in Batch `" + batch + "`");
                         System.out.println("Re-enter Roll number");
                    }
               }

               if (isValidBatch && isValidRollNum) {
                    DB.con.setAutoCommit(false);
                    PreparedStatement pstr = DB.con
                              .prepareStatement("Delete from student where roll_number = ? and st_batch = ?");
                    pstr.setInt(1, roll_number);
                    pstr.setString(2, batch);
                    pstr.executeUpdate();
                    System.out.println("Are you sure for...");
                    System.out.print("Remove `" + roll_number + "` from Batch `" + batch + "`? : ");
                    String sure = sc.next().toLowerCase();
                    if (sure.equalsIgnoreCase("yes")) {
                         DB.con.commit();
                         System.out.println("Removed Successfully.");
                    } else {
                         DB.con.rollback();
                         System.out.println("Then Not Removed.");
                    }
               }
          } catch (Exception e) {
               System.out.println("An error occur : " + e);
          }
     }

     static void showAllStudent() {
          try {
               PreparedStatement pst = DB.con.prepareStatement("Select * from student");
               ResultSet rs = pst.executeQuery();
               System.out.println("\n\n==>STUDENT DATA");
               System.out.printf("%-12s %-12s %-35s %-10s%n", "Student Id", "Roll Number", "Full Name", "Batch");
               while (rs.next()) {

                    System.out.printf("%-12d %-12d %-35s %-10s%n", rs.getInt("st_id"), rs.getInt("roll_number"),
                              rs.getString("st_full_name") , rs.getString("st_batch"));
               }
          } catch (Exception e) {
               System.out.println("An error occur : " + e);
          }
     }
}
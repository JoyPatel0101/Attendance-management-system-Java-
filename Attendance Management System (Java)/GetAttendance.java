import java.util.*;
import java.sql.*;

public class GetAttendance {

    static Scanner sc = new Scanner(System.in);

    public static void getParticularStudentAttendance() {
        String batch = "";
        int roll_number = 0;
        int total = 0;
        int present = 0;
        try {
            // For get batch name.
            boolean isValidBatch = false;
            while (!isValidBatch) {
                System.out.print("Enter your batch: ");
                batch = sc.next().toUpperCase();

                PreparedStatement pstGetBatch = DB.con
                        .prepareStatement("SELECT batch_name FROM batch WHERE batch_name = ?");
                pstGetBatch.setString(1, batch);
                ResultSet rsGetBatch = pstGetBatch.executeQuery();
                if (rsGetBatch.next()) {
                    isValidBatch = true;
                } else {
                    System.out.println(batch + " does not exist as per data.");
                    System.out.println("Re-enter...");
                }
            }

            // for get Sudent's roll number.
            boolean isValidRollNumber = false;
            if (isValidBatch) {
                while (!isValidRollNumber) {
                    System.out.print("Enter roll number : ");
                    roll_number = sc.nextInt();

                    PreparedStatement pstGetRollNum = DB.con
                            .prepareStatement("SELECT roll_number FROM student WHERE st_batch = ? ");
                    pstGetRollNum.setString(1, batch);
                    ResultSet rsGetRollNum = pstGetRollNum.executeQuery();
                    if (rsGetRollNum.next()) {
                        isValidRollNumber = true;
                    } else {
                        System.out.println(
                                "`" + roll_number + "` does not exists in batch `" + batch + "` ,as per data.");
                        System.out.print("Re-enter...");
                    }
                }
            }

            if (isValidRollNumber) {
                // Fetching data of attendance of given student.
                PreparedStatement pstGetAttendSt = DB.con
                        .prepareStatement("SELECT * FROM attendance where st_batch=? AND roll_number=?");
                pstGetAttendSt.setString(1, batch);
                pstGetAttendSt.setInt(2, roll_number);
                ResultSet rsGetAttendSt = pstGetAttendSt.executeQuery();

                System.out.printf("%-25s %-30s %-10s %-8s%n", "Date & time", "Faculty name", "Subject", "Status");
                System.out.println(
                        "-----------------------------------------------------------------------------------------------------------------------");
                while (rsGetAttendSt.next()) {
                    total++;
                    if (rsGetAttendSt.getString("status").equalsIgnoreCase("p")) {
                        present++;
                    }
                    System.out.printf("%-25s %-30s %-10s %-8s%n",
                            rsGetAttendSt.getTimestamp("date_time").toString(),
                            rsGetAttendSt.getString("fac_name"),
                            rsGetAttendSt.getString("subject"),
                            rsGetAttendSt.getString("status"));
                }
                System.out.println("Total Attendance is " + (present * 100.00 / total) + "%");
            }
        } catch (NullPointerException e) {
            if (roll_number > 0)
                System.out.println("Not attendance records of Roll number `" + roll_number + "`.");
        } catch (SQLException e) {
            System.out.println("An SQL error occur : " + e);
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    public static void getSubjectWiseAttendance() {
        String subject = "";
        int total = 0;
        int present = 0;
        try {
            boolean isValidSubject = false;
            while (!isValidSubject) {
                System.out.print("Enter Subject : ");
                subject = sc.next().toUpperCase();

                // Entered subject is exists or not?
                PreparedStatement pstGetSub = DB.con.prepareStatement("SELECT sub_name FROM subjects");
                ResultSet rsGetSub = pstGetSub.executeQuery();
                while (rsGetSub.next()) {
                    if (rsGetSub.getString("sub_name").equalsIgnoreCase(subject)) {
                        isValidSubject = true;
                    }
                }

                if (isValidSubject) {
                    // Fetching data of attendance by subject
                    PreparedStatement pstGetAttendSub = DB.con
                            .prepareStatement("SELECT * FROM attendance where subject=?");
                    pstGetAttendSub.setString(1, subject);
                    ResultSet rsGetAttendSub = pstGetAttendSub.executeQuery();

                    System.out.printf("%-25s %-10s %-30s %-12s %-30s %-8s%n", "Date & time", "Batch", "Faculty name",
                            "Roll Number",
                            "Student Name", "Status");
                    System.out.println(
                            "-----------------------------------------------------------------------------------------------------------------------");
                    while (rsGetAttendSub.next()) {
                        total++;
                        if (rsGetAttendSub.getString("status").equalsIgnoreCase("p")) {
                            present++;
                        }
                        System.out.printf("%-25s %-10s %-30s %-12d %-30s %-8s%n",
                                rsGetAttendSub.getTimestamp("date_time").toString(),
                                rsGetAttendSub.getString("st_batch"),
                                rsGetAttendSub.getString("fac_name"),
                                rsGetAttendSub.getInt("roll_number"),
                                rsGetAttendSub.getString("st_name"),
                                rsGetAttendSub.getString("status"));
                    }
                    System.out.println("Total Attendance is " + (present * 100.00 / total) + "%");
                } else {
                    System.out.println("`" + subject + "` is not exits as per data.");
                    System.out.println("Re-enter...");
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Sessions were not scheduled fo  subject `" + subject + "`.");
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    public static void getBatchWiseAttendance() {
        String batch = "";
        int total = 0;
        int present = 0;
        try {
            boolean isValidBatch = false;
            while (!isValidBatch) {
                System.out.print("Enter your batch: ");
                batch = sc.next().toUpperCase();

                PreparedStatement pstGetBatch = DB.con
                        .prepareStatement("SELECT batch_name FROM batch WHERE batch_name = ?");
                pstGetBatch.setString(1, batch);
                ResultSet rsGetBatch = pstGetBatch.executeQuery();
                if (rsGetBatch.next()) {
                    isValidBatch = true;
                } else {
                    System.out.println(batch + " does not exist as per data.");
                    System.out.println("Re-enter...");
                }
            }

            if (isValidBatch) {
                // Fetching data of attendance of given Batch.
                PreparedStatement pstGetAttendBatch = DB.con
                        .prepareStatement("SELECT * FROM attendance where st_batch=?");
                pstGetAttendBatch.setString(1, batch);
                ResultSet rsGetAttendBatch = pstGetAttendBatch.executeQuery();

                System.out.printf("%-25s %-30s %-10s %-12s %-30s %-8s%n", "Date & time", "Faculty name", "Subject",
                        "Roll Number", "Student Name", "Status");
                System.out.println(
                        "-----------------------------------------------------------------------------------------------------------------------");
                while (rsGetAttendBatch.next()) {
                    total++;
                    if (rsGetAttendBatch.getString("status").equalsIgnoreCase("p")) {
                        present++;
                    }
                    System.out.printf("%-25s %-30s %-10s %-12s %-30s %-8s%n",
                            rsGetAttendBatch.getTimestamp("date_time").toString(),
                            rsGetAttendBatch.getString("fac_name"),
                            rsGetAttendBatch.getString("subject"),
                            rsGetAttendBatch.getInt("roll_number"),
                            rsGetAttendBatch.getString("st_name"),
                            rsGetAttendBatch.getString("status"));
                }
                System.out.println("Total Attendance is " + (present * 100.00 / total) + "%");
            }

        } catch (NullPointerException e) {
            System.out.println("Sessions were not scheduled in batch `" + batch + "`.");
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

}

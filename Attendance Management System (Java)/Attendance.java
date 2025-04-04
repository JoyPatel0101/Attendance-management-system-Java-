import java.util.*;
import java.util.Date;
import java.text.*;
import java.sql.*;

public class Attendance {
    static Scanner sc = new Scanner(System.in);

    static Date date_time;
    static String batch;
    static int fac_id;
    static String fac_name;
    static String subject;
    static int st_id;
    static String st_name;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void manageAttendance() {
        // Get date with time.
        System.out.println("\nYou have 2 Options for Date and Time.");
        System.out.println("1 --> Automatic get Current Date & Time.");
        System.out.println("2 --> You want to enter Date & Time.");
        System.out.print("Enter Your choice for Date & Time: ");
        try {
            boolean numdt = false;
            while (!numdt) {
                int chdt = Check.numCheck();
                switch (chdt) {
                    case 1:
                        date_time = new Date();
                        numdt = true;
                        break;
                    case 2:
                        date_time = getDate();
                        numdt = true;
                        break;
                    default:
                        System.out.println("Enter 1 or 2 number.");
                        System.out.print("Re-enter: ");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
        System.out.println("Date --> " + date_time);

        // Check if the user exists
        System.out.print("Enter Username: ");
        try {
            String username = sc.next();
            boolean exist = false;
            fac_id = 0;
            String password = "";
            PreparedStatement pst = DB.con.prepareStatement("SELECT * FROM faculty WHERE username = ?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                fac_id = rs.getInt("fac_id");
                password = rs.getString("password");
                fac_name = rs.getString("fac_full_name");
                exist = true;
            }

            if (exist) {
                // Verify password
                int chance = 1;
                while (chance <= 3) {
                    System.out.print("Enter password: ");
                    String epass = sc.next();
                    if (password.equals(epass)) {
                        chance = 4;
                        System.out.println("\n`" + username + "` logged in successfully.");

                        // Check if the subject is taught by the faculty
                        boolean isValidSession = false;
                        try {
                            while (!isValidSession) {
                                System.out.print("Enter Session (Subject): ");
                                subject = Check.withoutSpecialCharacter().toUpperCase();
                                Set<String> facSub = new HashSet<>();

                                PreparedStatement pstGetSubId = DB.con.prepareStatement("SELECT sub_id FROM faculty_subjects WHERE fac_id = ?");
                                pstGetSubId.setInt(1, fac_id);
                                ResultSet rsGetSubId = pstGetSubId.executeQuery();
                                while (rsGetSubId.next()) {
                                    PreparedStatement pstGetSub = DB.con.prepareStatement("SELECT sub_name FROM subjects WHERE sub_id = ?");
                                    pstGetSub.setInt(1, rsGetSubId.getInt("sub_id"));
                                    ResultSet rsGetSub = pstGetSub.executeQuery();
                                    while (rsGetSub.next()) {
                                        facSub.add(rsGetSub.getString("sub_name"));
                                    }
                                }
                                if (facSub.contains(subject)) {
                                    isValidSession = true;
                                } else {
                                    System.out.println("You can't take " + subject + " as per Data.");
                                    System.out.println("Re-enter...");
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e);
                        }

                        // Check if the batch exists
                        boolean isValidBatch = false;
                        try {
                            if (isValidSession) {
                                while (!isValidBatch) {
                                    System.out.print("Your " + subject + " session has taken in which batch: ");
                                    batch = sc.next().toUpperCase();

                                    PreparedStatement pstGetBatch = DB.con.prepareStatement("SELECT batch_name FROM batch WHERE batch_name = ?");
                                    pstGetBatch.setString(1, batch);
                                    ResultSet rsGetBatch = pstGetBatch.executeQuery();
                                    if (rsGetBatch.next()) {
                                        isValidBatch = true;
                                    } else {
                                        System.out.println(batch + " does not exist as per data.");
                                        System.out.println("Re-enter...");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e);
                        }

                        // Get present numbers only
                        if (isValidBatch) {
                            try {
                                HashSet<Integer> preSt = new HashSet<>();
                                System.out.println("\nEnter Roll numbers of Present students only in Batch `" + batch + "`.[Type anything (except numbers) to finish.]");
                                while (sc.hasNextInt()) {
                                    int ps = sc.nextInt();
                                    if (ps > 0) {
                                        PreparedStatement pstCheckRollNum = DB.con.prepareStatement("SELECT * FROM student WHERE roll_number = ? AND st_batch = ?");
                                        pstCheckRollNum.setInt(1, ps);
                                        pstCheckRollNum.setString(2, batch);
                                        ResultSet rsCheckRollNum = pstCheckRollNum.executeQuery();
                                        if (rsCheckRollNum.next()) {
                                            preSt.add(ps);
                                        } else {
                                            System.out.println(ps + " does not exist in Batch `" + batch + "`.");
                                        }
                                    } else {
                                        System.out.println("Enter only positive numbers for roll number.");
                                    }
                                }

                                if (!sc.hasNextInt()) {
                                    // Fetch all students in the particular batch
                                    Stack<Integer> totalStudents = new Stack<>();
                                    try {
                                        PreparedStatement pstTotalStudent = DB.con.prepareStatement("SELECT roll_number FROM student WHERE st_batch = ?");
                                        pstTotalStudent.setString(1, batch);
                                        ResultSet rsTotalStudent = pstTotalStudent.executeQuery();
                                        while (rsTotalStudent.next()) {
                                            totalStudents.add(rsTotalStudent.getInt("roll_number"));
                                        }
                                    } catch (Exception e) {
                                        System.out.println("An error occurred: " + e);
                                    }

                                    Stack<Integer> presentStudent = new Stack<>();
                                    presentStudent = removeDuplicate(preSt);
                                    
                                    Stack<Integer> absentStudent = new Stack<>();
                                    absentStudent = totalStudents;
                                    remove(absentStudent, presentStudent);

                                    // Save present students to DB
                                    System.out.println("Presented Students are " + presentStudent.size());
                                    System.out.println(preSt);

                                    Timestamp timestamp = new Timestamp(date_time.getTime());

                                    for (int roll : presentStudent) {
                                        saveAttendance(roll, "p", timestamp);
                                    }

                                    for (int roll : absentStudent) {
                                        saveAttendance(roll, "ab", timestamp);
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("An error occurred: " + e);
                            }
                        } else {
                            System.out.println("Entered Password was wrong " + chance + "-time.");
                            chance++;
                        }
                    }
                }
            } else {
                System.out.println("`" + username + "` does not exist as per data.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
    }

    static Date getDate() {
        String d;
        Date dd = new Date();
        boolean isValidDate = false;
        while (!isValidDate) {
            try {
                System.out.println("\n\n*Enter Date & Time Information*.");
                System.out.print("Enter Year: ");
                String year = Check.yearCheck();
                System.out.print("Enter Month: ");
                String month = Check.monthCheck();
                System.out.print("Enter Day: ");
                String day = Check.dayCheck();
                System.out.print("Enter Hour: ");
                String hour = Check.hourCheck();
                System.out.print("Enter Minute: ");
                String minute = Check.minuteCheck();
                System.out.print("Enter Second: ");
                String second = Check.secondCheck();

                d = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                isValidDate = Check.dateCheck(year, month, day, hour, minute, second);
                dd = sdf.parse(d);
                if (!isValidDate) {
                    System.out.println("Entered Date & Time is in the future.");
                    System.out.println("Re-enter");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e);
            }
        }
        return dd;
    }

    static Stack<Integer> removeDuplicate(HashSet<Integer> preSt) {
        Stack<Integer> temp = new Stack<>();
        temp.addAll(preSt);
        return temp;
    }

    static void remove(Stack<Integer> absentStudent, Stack<Integer> presentStudent) {
        Stack<Integer> temp = new Stack<>();
        while (!absentStudent.isEmpty()) {
            int rn = absentStudent.pop();
            if (!presentStudent.contains(rn)) {
                temp.push(rn);
            }
        }
        while (!temp.isEmpty()) {
            absentStudent.push(temp.pop());
        }
    }

    private static void saveAttendance(int roll_number, String status, Timestamp timestamp) {
        try {
            PreparedStatement pstStName = DB.con.prepareStatement("SELECT * FROM student WHERE roll_number = ?");
            pstStName.setInt(1, roll_number);
            ResultSet rsStName = pstStName.executeQuery();
            if (rsStName.next()) {
                st_name = rsStName.getString("st_full_name");
                st_id = rsStName.getInt("st_id");
            }
            String Atsql = "INSERT INTO attendance(date_time, st_batch, fac_name, subject, st_id, roll_number, st_name, status) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pstAttendance = DB.con.prepareStatement(Atsql);
            pstAttendance.setTimestamp(1, timestamp);
            pstAttendance.setString(2, batch);
            pstAttendance.setString(3, fac_name);
            pstAttendance.setString(4, subject);
            pstAttendance.setInt(5, st_id);
            pstAttendance.setInt(6, roll_number);
            pstAttendance.setString(7, st_name);
            pstAttendance.setString(8, status);
            pstAttendance.executeUpdate();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
    }
}

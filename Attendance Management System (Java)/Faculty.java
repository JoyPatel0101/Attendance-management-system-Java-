import java.util.*;

import com.mysql.*;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.io.*;

public class Faculty {
    static Scanner sc = new Scanner(System.in);
    static int fac_id;
    static String fac_first_name;
    static String fac_middle_name;
    static String fac_last_name;
    static String fac_full_name;
    static String user_name;
    static private String password;
    static String[] fac_subjects;

    Faculty(String user_name, String password, String fac_full_name, String[] fac_subjects) {
        this.user_name = user_name;
        this.password = password;
        this.fac_full_name = fac_full_name;
        this.fac_subjects = fac_subjects;
        storeFacultyData();
    }

    static void addNewFaculty() {
        try {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.print("Enter your First_Name : ");
            fac_first_name = Check.withoutSpecialCharacter();
            System.out.print("\nEnter your Middle_Name : ");
            fac_middle_name = Check.withoutSpecialCharacter();
            System.out.print("\nEnter your Last_Name(surname) : ");
            fac_last_name = Check.withoutSpecialCharacter();
            fac_full_name = fac_first_name + " " + fac_middle_name + " " + fac_last_name;

            int favNum = 0;
            boolean f1 = true;
            while (f1) {
                try {
                    System.out.print("\nWhat's your faviourate Number?(in 4 digits): ");
                    favNum = sc.nextInt();
                    if (favNum >= 0 && favNum <= 9999) {
                        f1 = false;
                    } else {
                        throw new FourDigitsException();
                    }
                    f1 = false;
                } catch (FourDigitsException e) {
                    System.out.println(e + ":please enter 4 digit Number.");
                    System.out.print("Re-Enter, ");
                } catch (Exception e) {
                    System.out.println("An error occur :" + e);
                    sc.next();
                    System.out.print("Re-Enter, ");
                }
            }
            System.out.println("\n*Below Conditions for Password*");
            System.out.println(" 1.Characters must be 8-15.");
            System.out.println(" 2.Must include Uper_char & lower_char");
            System.out.println(" 3.Must include special character");
            System.out.println(" 4.Space is/are not count with start & end.");
            System.out.print("Enter a password that meets the criteria : ");
            password = Check.withSpecialCharacter();

            System.out.print("\nHow many subjects? You are teaching : ");
            int numOfSubjects = Check.numCheck();
            fac_subjects = new String[numOfSubjects];
            System.out
                    .println("\nPlease, Subject name enter in only 'Single word (allowed '-' ; but,not any number)'.");
            for (int i = 0; i < numOfSubjects; i++) {
                System.out.print("Enter suject-" + (i + 1) + " : ");
                fac_subjects[i] = Check.withoutSpecialCharacter().toUpperCase();
            }
            fac_subjects = removeDuplicates(fac_subjects); // for remove duplicate subjects.

            user_name = generateUserName(favNum);
            storeFacultyData();
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Hey," + fac_full_name);
            System.out.println("Your generated, UserName --> `" + user_name + "`");
            System.out.println("       Entered, Password -->  " + password);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            try {
                Thread t = new Thread();
                t.sleep(5000);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void storeFacultyData() {
        try {
            String q1 = "Insert into faculty(username,password,fac_full_name) values(?,?,?)";
            PreparedStatement pst1 = DB.con.prepareStatement(q1);
            pst1.setString(1, user_name);
            pst1.setString(2, password);
            pst1.setString(3, fac_full_name);
            System.out.println(
                    "Your data was " + ((pst1.executeUpdate() > 0) ? "entered Successfully." : "not entered."));
            storeSubjectdata(user_name);
        } catch (Exception e) {
            System.out.println("An error occur :" + e);
        }
    }

    // Store subject data but, if not in DataBase.
    static void storeSubjectdata(String username) {
        try {
            // Check existing subjects
            PreparedStatement pstSubjects = DB.con.prepareStatement("SELECT sub_name FROM subjects");
            Set<String> existingSubjects = new HashSet<>();
            try (ResultSet rsSubjects = pstSubjects.executeQuery()) {
                while (rsSubjects.next()) {
                    existingSubjects.add(rsSubjects.getString("sub_name").toUpperCase());
                }
            }

            // Insert new subjects.
            PreparedStatement pstInsertSubject = DB.con.prepareStatement("INSERT INTO subjects (sub_name) VALUES (?)");
            for (String subject : fac_subjects) {
                if (!existingSubjects.contains(subject)) {
                    pstInsertSubject.setString(1, subject);
                    pstInsertSubject.executeUpdate();
                }
            }

            // Get fac_id & insert in faculty_subjects.
            int facId;
            PreparedStatement pstFacultyId = DB.con.prepareStatement("SELECT fac_id FROM faculty WHERE username = ?");

            pstFacultyId.setString(1, username);

            try (ResultSet rsSelectFacultyId = pstFacultyId.executeQuery()) {
                if (rsSelectFacultyId.next()) {
                    facId = rsSelectFacultyId.getInt("fac_id");
                    for (int i = 0; i < fac_subjects.length; i++) {
                        int facSubId = 0;
                        PreparedStatement pstSubId = DB.con
                                .prepareStatement("Select sub_id from subjects where sub_name=?");
                        pstSubId.setString(1, fac_subjects[i]);
                        ResultSet rsFacSub = pstSubId.executeQuery();
                        if (rsFacSub.next()) {
                            facSubId = rsFacSub.getInt("sub_id");
                        }

                        PreparedStatement pstFacSub = DB.con
                                .prepareStatement("Insert into faculty_subjects values(?,?)");
                        pstFacSub.setInt(1, facId);
                        pstFacSub.setInt(2, facSubId);
                        pstFacSub.executeUpdate();
                    }
                } else {
                    throw new SQLException("Faculty ID not found.");
                }
            }

        } catch (Exception e) {
            System.out.println("An error occur :" + e);
        }
    }

    static String generateUserName(int favNum) {
        String code = setFourNumber(favNum);
        String un = fac_first_name + "_" + code;
        return un;
    }

    static String setFourNumber(int x) {
        String fourDigitNum = String.format("%04d", x);
        return fourDigitNum;
    }

    static String[] removeDuplicates(String[] fac_subjects) {
        Set<String> setFacSub = new HashSet<>(Arrays.asList(fac_subjects));
        return setFacSub.toArray(new String[0]);
    }

    static void removeFaculty() {
        try {

            System.out.print("Enter username : ");
            String username = sc.next();
            boolean exist = false;
            int fac_id = 0;
            String password = "";
            PreparedStatement pst = DB.con.prepareStatement("Select fac_id,username,password from faculty");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (username.equals(rs.getString("username"))) {
                    fac_id = rs.getInt("fac_id");
                    password = rs.getString("password");
                    exist = true;
                    break;
                }
            }
            if (exist) {
                DB.con.setAutoCommit(false);
                PreparedStatement pstr = DB.con.prepareStatement("Delete from faculty where username=?");
                pstr.setString(1, username);
                pstr.executeUpdate();
                int chance = 1;
                while (chance <= 3) {
                    System.out.print("Enter password : ");
                    String epass = sc.next();
                    if (password.equals(epass)) {
                        chance = 4;
                        System.out.print("You are sure for remove username `" + username + "` : ");
                        String sure = sc.next().toLowerCase();
                        if (sure.equals("yes")) {
                            PreparedStatement pstRemoveSub = DB.con
                                    .prepareStatement("Delete from faculty_subjects where fac_id=?");
                            pstRemoveSub.setInt(1, fac_id);
                            pstRemoveSub.executeUpdate();
                            DB.con.commit();
                            System.out.println("Deleted successfully.");
                        } else {
                            DB.con.rollback();
                            System.out.println("Then `" + username + "` is not deleted.");
                        }
                    } else {
                        System.out.println("Entered Password was wrong " + chance + "-time.");
                        chance++;
                    }
                }
            } else {
                System.out.println(username + " Not Exist.");
            }

        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
    }

    static void displayAllFaculty() {
        try {
            String getSub = "Select sub_name from faculty Inner Join faculty_subjects On faculty.fac_id=faculty_subjects.fac_id Inner join subjects on faculty_subjects.sub_id=subjects.sub_id where faculty.fac_id=?";
            PreparedStatement pstSub = DB.con.prepareStatement(getSub);
            ResultSet rsSub;

            String sql = "Select fac_id,fac_full_name,username from faculty ";
            PreparedStatement pst = DB.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            System.out.println("\n\n==> FACULTY DATA ");
            System.out.printf("%-12s %-30s %-20s %-30s%n", "Faculty Id", "Full Name", "username" , "Subjects");            
            while (rs.next()) {
                int fac_id = rs.getInt("fac_id");
                
                Set<String> sub = new HashSet<>();
                pstSub.setInt(1, fac_id);
                rsSub = pstSub.executeQuery();
                while (rsSub.next()) {
                    sub.add(rsSub.getString("sub_name"));
                }

                System.out.printf("%-12d %-30s %-20s %-30s%n" , fac_id , rs.getString("fac_full_name") , rs.getString("username") , sub);
            }

        } catch (Exception e) {
            System.out.println("An error occur ;" + e);
        }
    }
}

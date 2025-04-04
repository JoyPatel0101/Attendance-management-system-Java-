import java.sql.*;
import java.io.*;
import java.util.*;

import com.mysql.cj.SimpleQuery;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        DB.connection();
        Boolean b = true;
        while (b) {
            System.out.println("\n\n************************");
            System.out.println("************************");
            System.out.println("1 --> Conduct Attendance.");
            System.out.println("2 --> Manage Faculties.");
            System.out.println("3 --> Manage Students.");
            System.out.println("4 --> Manage Batches.");
            System.out.println("5 --> Get Attendance.");
            System.out.println("0 --> EXIT.");
            System.out.print("Enter your choice for Main Menu : ");
            int ch = 0;
            try {
                ch = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Your entred data will Mis-Match.");
            } catch (Exception e) {
                System.out.println(e);
            }

            switch (ch) {
                case 0: {
                    try {
                        Thread t = new Thread();
                        System.out.print("Exiting");
                        for (int i = 1; i <= 4; i++) {
                            System.out.print(".");
                            t.sleep(500);
                        }
                        b = false;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                }
                case 1: {
                    Attendance.manageAttendance();
                    break;
                }
                case 2: {
                    boolean bf = true;
                    while (bf) {
                        System.out.println("\n************************");
                        System.out.println("1 --> Register new Faculty.");
                        System.out.println("2 --> Remove Faculty.");
                        System.out.println("3 --> Show All Faculty.");
                        System.out.println("0 --> Back to main menu.");
                        System.out.print("Enter your choice for Faculty Menu : ");
                        int chf = 0;
                        try {
                            chf = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Your entred data will Mis-Match.");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        switch (chf) {
                            case 0: {
                                try {
                                    Thread t = new Thread();
                                    System.out.print("Back to Main Menu.");
                                    for (int i = 1; i <= 4; i++) {
                                        System.out.print(".");
                                        t.sleep(200);
                                    }
                                    bf = false;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            }
                            case 1:{
                                Faculty.addNewFaculty();
                                break;
                            }
                            case 2:{
                                Faculty.removeFaculty();
                                break;
                            }
                            case 3:{
                                Faculty.displayAllFaculty();
                                break;
                            }
                            default:{
                                System.out.println("Please,Enter any number 0 to 3 as a Input.");
                            }
                        }
                    }
                    break;
                }
                case 3: {
                    boolean bs = true;
                    while (bs) {
                        System.out.println("\n************************");
                        System.out.println("1 --> Register new Student.");
                        System.out.println("2 --> Remove Student.");
                        System.out.println("3 --> Show All Student.");
                        System.out.println("0 --> Back to main menu.");
                        System.out.print("Enter your choice for Student Menu : ");
                        int chs = 0;
                        try {
                            chs = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Your entred data will Mis-Match.");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        switch (chs) {
                            case 0: {
                                try {
                                    Thread t = new Thread();
                                    System.out.print("Back to Main Menu.");
                                    for (int i = 1; i <= 4; i++) {
                                        System.out.print(".");
                                        t.sleep(200);
                                    }
                                    bs = false;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            }
                            case 1:{
                                Student.addNewStudent();
                                break;
                            }
                            case 2:{
                                Student.removeStudent();
                                break;
                            }
                            case 3:{
                                Student.showAllStudent();
                                break;
                            }
                            default:{
                                System.out.println("Please,Enter any number 0 to 3 as a Input.");
                            }
                        }
                    }
                    break;
                }
                case 4:{
                    boolean bb = true;
                    while (bb) {
                        System.out.println("\n************************");
                        System.out.println("1 --> Register new Batch.");
                        System.out.println("2 --> Remove Batch.");
                        System.out.println("3 --> Show All Batch.");
                        System.out.println("0 --> Back to main menu.");
                        System.out.print("Enter your choice for Faculty Menu : ");
                        int chb = 0;
                        try {
                            chb = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Your entred data will Mis-Match.");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        switch (chb) {
                            case 0: {
                                try {
                                    Thread t = new Thread();
                                    System.out.print("Back to Main Menu.");
                                    for (int i = 1; i <= 4; i++) {
                                        System.out.print(".");
                                        t.sleep(200);
                                    }
                                    bb = false;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            }
                            case 1:{
                                Batch.addNewBatch();
                                break;
                            }
                            case 2:{
                                Batch.removeBatch();
                                break;
                            }
                            case 3:{
                                Batch.displayAllBatch();
                                break;
                            }
                            default:{
                                System.out.println("Please,Enter any number 0 to 3 as a Input.");
                            }
                        }
                    }
                    break;
                }
                case 5:{
                    boolean ab = true;
                    while (ab) {
                        System.out.println("\n************************");
                        System.out.println("1 --> Get Particular Student Attendance.");
                        System.out.println("2 --> Get Subject wise Attendance.");
                        System.out.println("3 --> Get Batch wise Attendance.");
                        System.out.println("0 --> Back to main menu.");
                        System.out.print("Enter your choice for get attendance Menu : ");
                        int chb = 0;
                        try {
                            chb = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Your entred data will Mis-Match.");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        switch (chb) {
                            case 0: {
                                try {
                                    Thread t = new Thread();
                                    System.out.print("Back to Main Menu.");
                                    for (int i = 1; i <= 4; i++) {
                                        System.out.print(".");
                                        t.sleep(200);
                                    }
                                    ab = false;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            }
                            case 1:{
                                GetAttendance.getParticularStudentAttendance();
                                break;
                            }
                            case 2:{
                                GetAttendance.getSubjectWiseAttendance();
                                break;
                            }
                            case 3:{
                                GetAttendance.getBatchWiseAttendance();
                                break;
                            }
                            default:{
                                System.out.println("Please,Enter any number 0 to 3 as a Input.");
                            }
                        }
                    }
                    break;
                }
                default: {
                    System.out.println("Please,Enter any number 0 to 4 as a Input.");
                }
            }
        }

    }
}

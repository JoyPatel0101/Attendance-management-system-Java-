import java.util.*;
import java.text.*;

public class Check {

    static Date date = new Date();
    static int currentDay = date.getDay();
    static int currentMonth = date.getMonth();
    static int currentYear = date.getYear() + 1900;

    static Scanner sc = new Scanner(System.in);

    static String withoutSpecialCharacter() {
        String s;
        while (true) {
            // System.out.print("Enter a string without special characters: ");
            s = sc.next().trim();

            // Check if the string contains only alphabetic characters
            if (s.matches("^[a-zA-Z-]+$")) {
                return s;
            } else {
                System.out.println("Special characters are not allowed.");
                System.out.print("Re-Enter : ");
            }
        }
    }

    // For password...
    static String withSpecialCharacter() {
        String pas = "";
        sc.nextLine();
        while (true) {
            pas = sc.nextLine().trim();
            if (isValidPassword(pas)) {
                return pas;
            } else {
                System.out.println("*CAUTION*");
                System.out.println("Invalid password. Please meet the following criteria:");
                System.out.println("1. Length must be between 8 and 15 characters.");
                System.out.println("2. Must include at least one digit.");
                System.out.println("3. Must include at least one lowercase letter.");
                System.out.println("4. Must include at least one uppercase letter.");
                System.out.println("5. Must include at least one special character.");
                System.out.print("Re-Enter Password : ");
            }
        }
    }

    private static boolean isValidPassword(String password) {
        String reg = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&*+-])" + "" + ".{8,15}$";
        return password.matches(reg);
    }

    static int numCheck() {
        int num = 0;
        boolean b = false;
        while (!b) {
            try {
                num = sc.nextInt();
                if (num < 0) {
                    throw new NegativeNumberException();
                }
                b = true;
                return num;
            } catch (InputMismatchException e) {
                System.out.println("Enter only Integer number as a Input.An error occur:" + e);
                System.out.print("Re-Enter number : ");
                sc.nextLine();
            } catch (NegativeNumberException e) {
                System.out.println("Enter only positive Integer number as a Input.An error occur:" + e);
                System.out.print("Re-Enter number : ");
            } catch (Exception e) {
                System.out.println("An error occurs:" + e);
                System.out.print("Re-Enter number : ");
                sc.nextLine();
            }
        }
        return num;
    }

    static String yearCheck() {
        int y = numCheck();
        String yf = "";
        try {
            while ((!(y <= currentYear))) {
                if (y != 0) {
                    System.out.println("Enterd year is greater then the Current year.");
                    System.out.print("Re-enter Year : ");
                    y = numCheck();
                } else {
                    System.out.println("Your 3enterd year is zero.");
                    System.out.print("Re-enter Year : ");
                    y = numCheck();
                }
            }
            yf = String.format("%04d", y);
            return yf;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return yf;
    }

    static String monthCheck() {
        int m = numCheck();
        String mf = "";
        try {
            while ((!((m <= 12)))) {
                if (m != 0) {
                System.out.println("Maximum 12 Months in a Year.");
                System.out.print("Re-enter Month : ");
                m = numCheck();
                } else {
                System.out.println("Your entered Month is zero.");
                System.out.print("Re-enter Month : ");
                m = numCheck();
                }
            }
            mf = String.format("%02d", m);
            return mf;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return mf;
    }

    static String dayCheck() {
        int d = numCheck();
        String df = "";
        try {
            while ((!(d <= 31))) {
                if (d != 0) {
                    System.out.println("Maximum 31 days in a month.");
                    System.out.print("Re-enter day : ");
                    d = numCheck();
                } else {
                    System.out.println("Your entered day is zero.");
                    System.out.print("Re-enter day : ");
                    d = numCheck();
                }
            }
            df = String.format("%02d", d);
            return df;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return df;
    }

    static String hourCheck() {
        int h = numCheck();
        String hf ="";
        try {
            while (!(h <= 23)) {
                System.out.println("Maximum 23 hours in a day.");
                System.out.print("Re-enter hour : ");
                h = numCheck();
            }
            hf = String.format("%02d", h);
            return hf;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return hf;
    }

    static String minuteCheck() {
        int m = numCheck();
        String mf = "";
        try {
            while (!(m <= 59)) {
                System.out.println("Maximum 59 Minutes in a Hour.");
                System.out.print("Re-enter Minutes : ");
                System.out.println();
                m = numCheck();
            }
            mf = String.format("%02d", m);
            return mf;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return mf;
    }

    static String secondCheck() {
        int s = numCheck();
        String sf ="";
        try {
            while (!(s <= 59)) {
                System.out.println("Maximum 59 Seconds in a Minute.");
                System.out.print("Re-enter Second : ");
                System.out.println();
                s = numCheck();
            }
            sf = String.format("02%d", s);
            return sf;
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
        }
        return sf;
    }

    static boolean dateCheck(String year, String month, String day ,String hour , String minute , String second) {
        Date currentDate = new Date();
        String givenDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            boolean isDate = true;
            Date parsedDate = sdf.parse(givenDate);
            if (parsedDate.before(currentDate)) {
                isDate = false;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("An error occur : " + e);
            return false;
        }
    }
}
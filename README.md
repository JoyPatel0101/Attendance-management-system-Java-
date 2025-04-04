
# Attendance Management System 📋

The **Attendance Management System** is a Java-based console application designed to manage and record student attendance efficiently for academic institutions. The project focuses on batch-wise student tracking, robust validation, and streamlined database interaction.

## 🚀 Features

- 🧑‍🎓 Student Registration and Batch Management  
- ✅ Mark and Retrieve Attendance  
- ⚠️ Custom Exception Handling (e.g., four-digit roll number, negative value checks)  
- 💾 Database Connectivity via JDBC

## 🛠️ Tech Stack

- **Programming Language:** Java  
- **Database:** MySQL (via JDBC)  
- **Concepts Used:** OOP, Exception Handling, File I/O, Modular Classes

## 📂 Project Structure

```
/AttendanceManagementSystem
│
├── Main.java                  # Entry point of the program
├── Attendance.java            # Attendance logic
├── Student.java               # Student data structure
├── Batch.java                 # Batch-related functions
├── Check.java                 # Validation logic
├── DB.java                    # JDBC database connection
├── GetAttendance.java         # Fetch attendance from DB
├── FourDigitsException.java   # Custom exception for roll numbers
├── NegativeNumberException.java  # Custom exception for negative inputs
```

## 💻 How to Run

1. **Compile the program:**
   ```
   javac *.java
   ```

2. **Run the main file:**
   ```
   java Main
   ```

3. **Ensure MySQL is running and configured properly in `DB.java`.**

## 👨‍💻 Author

**Joy Patel**  
Artificial Intelligence & Data Science Student  
LJ University

---

*This project is built for academic use and learning purposes.* 🎓

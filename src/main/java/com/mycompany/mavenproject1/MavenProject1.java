package com.mycompany.mavenproject1; // আপনার প্যাকেজ নাম ঠিক রাখুন

import java.sql.*;
import java.util.*;

public class MavenProject1{
    // Database configuration - XAMPP এর জন্য ডিফল্ট সেটিংস
    static final String URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = ""; 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // আইডি এবং ৩টি বিষয়ের মার্কস সংরক্ষণের জন্য HashMap
        HashMap<Integer, Integer[]> studentMarksMap = new HashMap<>();

        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine(); // বাফার ক্লিয়ার করার জন্য

            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.println("Enter Marks for 3 Subjects:");
            Integer[] marks = new Integer[3];
            int total = 0;
            for (int i = 0; i < 3; i++) {
                System.out.print("Subject " + (i + 1) + ": ");
                marks[i] = sc.nextInt();
                total += marks[i];
            }

            // HashMap-এ ডাটা রাখা
            studentMarksMap.put(id, marks);

            // গড় নাম্বার অনুযায়ী গ্রেড বের করা
            String grade = calculateGrade(total / 3);

            // ডাটাবেসে সেভ করা
            insertData(id, name, total, grade);

            // ডাটাবেস থেকে সব রেকর্ড দেখানো
            displayRecords();

        } catch (Exception e) {
            System.out.println("Error in Input: " + e.getMessage());
        }
    }

    // গ্রেড লজিক
    public static String calculateGrade(int avg) {
        if (avg >= 80) return "A+";
        else if (avg >= 70) return "A";
        else if (avg >= 60) return "A-";
        else if (avg >= 50) return "B";
        else return "F";
    }

    // JDBC ব্যবহার করে ডাটা ইনসার্ট করা
    public static void insertData(int id, String name, int total, String grade) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO students (id, name, total, grade) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, total);
            pstmt.setString(4, grade);
            pstmt.executeUpdate();
            System.out.println("\n✅ Data inserted into database successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }

    // ডাটাবেস থেকে রেকর্ড রিড করা
    public static void displayRecords() {
        System.out.println("\n--- Current Student Records in Database ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Total: " + rs.getInt("total") + 
                                   " | Grade: " + rs.getString("grade"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching data: " + e.getMessage());
        }
    }
}
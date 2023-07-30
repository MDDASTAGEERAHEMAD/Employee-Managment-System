package employeemanagementsystem;
import java.sql.*;
import java.util.Scanner;

public class EmployeeManagementSystem {
	
	    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeedb";
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "root";

	    private static Connection connection;
	    private static Statement statement;
	    private static PreparedStatement preparedStatement;
	    private static ResultSet resultSet;
	    private static Scanner scanner;

	    public static void main(String[] args) {
	        try {
	        	//Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	            statement = connection.createStatement();
	            scanner = new Scanner(System.in);
	            
	            int choice;
	            do {
	                System.out.println("Employee Management System");
	                System.out.println("1. Register Employee");
	                System.out.println("2. Display All Employees");
	                System.out.println("3. Search Employee");
	                System.out.println("4. Update Employee");
	                System.out.println("5. Delete Employee");
	                System.out.println("6. Exit");
	                System.out.print("Enter your choice: ");
	                choice = scanner.nextInt();
	                scanner.nextLine(); // Consume newline character

	                switch (choice) {
	                    case 1:
	                        registerEmployee();
	                        break;
	                    case 2:
	                        displayAllEmployees();
	                        break;
	                    case 3:
	                        searchEmployee();
	                        break;
	                    case 4:
	                        updateEmployee();
	                        break;
	                    case 5:
	                        deleteEmployee();
	                        break;
	                    case 6:
	                        System.out.println("Exiting Employee Management System. Goodbye!");
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please try again.");
	                }
	            } while (choice != 6);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources();
	        }
	    }

	    private static void registerEmployee() {
	        try {
	            System.out.println("Enter employee details:");
	            System.out.print("Name: ");
	            String name = scanner.nextLine();
	            System.out.print("Address: ");
	            String address = scanner.nextLine();
	            System.out.print("Contact Information: ");
	            String contactInfo = scanner.nextLine();

	            String sql = "INSERT INTO employees (name, address, contact_info) VALUES (?, ?, ?)";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, name);
	            preparedStatement.setString(2, address);
	            preparedStatement.setString(3, contactInfo);
	            preparedStatement.executeUpdate();

	            System.out.println("Employee registered successfully!");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void displayAllEmployees() {
	        try {
	            String sql = "SELECT * FROM employees";
	            resultSet = statement.executeQuery(sql);

	            System.out.println("Employee List:");
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                String address = resultSet.getString("address");
	                String contactInfo = resultSet.getString("contact_info");

	                System.out.println("ID: " + id);
	                System.out.println("Name: " + name);
	                System.out.println("Address: " + address);
	                System.out.println("Contact Information: " + contactInfo);
	                System.out.println("---------------------------");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void searchEmployee() {
	        System.out.println("Enter search criteria:");
	        System.out.print("Name: ");
	        String name = scanner.nextLine();

	        try {
	            String sql = "SELECT * FROM employees WHERE name LIKE ?";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, "%" + name + "%");
	            resultSet = preparedStatement.executeQuery();

	            System.out.println("Search Results:");
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String empName = resultSet.getString("name");
	                String address = resultSet.getString("address");
	                String contactInfo = resultSet.getString("contact_info");

	                System.out.println("ID: " + id);
	                System.out.println("Name: " + empName);
	                System.out.println("Address: " + address);
	                System.out.println("Contact Information: " + contactInfo);
	                System.out.println("---------------------------");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void updateEmployee() {
	        System.out.println("Enter employee ID to update:");
	        System.out.print("ID: ");
	        int id = scanner.nextInt();
	        scanner.nextLine(); // Consume newline character

	        try {
	            String sql = "SELECT * FROM employees WHERE id = ?";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, id);
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                System.out.println("Enter updated details:");
	                System.out.print("Name: ");
	                String name = scanner.nextLine();
	                System.out.print("Address: ");
	                String address = scanner.nextLine();
	                System.out.print("Contact Information: ");
	                String contactInfo = scanner.nextLine();

	                sql = "UPDATE employees SET name = ?, address = ?, contact_info = ? WHERE id = ?";
	                preparedStatement = connection.prepareStatement(sql);
	                preparedStatement.setString(1, name);
	                preparedStatement.setString(2, address);
	                preparedStatement.setString(3, contactInfo);
	                preparedStatement.setInt(4, id);
	                preparedStatement.executeUpdate();

	                System.out.println("Employee updated successfully!");
	            } else {
	                System.out.println("Employee with ID " + id + " does not exist.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void deleteEmployee() {
	        System.out.println("Enter employee ID to delete:");
	        System.out.print("ID: ");
	        int id = scanner.nextInt();
	        scanner.nextLine(); // Consume newline character

	        try {
	            String sql = "SELECT * FROM employees WHERE id = ?";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, id);
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                sql = "DELETE FROM employees WHERE id = ?";
	                preparedStatement = connection.prepareStatement(sql);
	                preparedStatement.setInt(1, id);
	                preparedStatement.executeUpdate();

	                System.out.println("Employee deleted successfully!");
	            } else {
	                System.out.println("Employee with ID " + id + " does not exist.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void closeResources() {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	            if (statement != null) {
	                statement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	            if (scanner != null) {
	                scanner.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


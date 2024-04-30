import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class EmployeeRepository {
    private static final String DATABASE_NAME = "employees";
    private static final String COLLECTION_NAME = "employee";
    private static final String EMPLOYEE_DIRECTORY = "employees/";

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public EmployeeRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        this.collection = database.getCollection(COLLECTION_NAME);
    }

    public void insertEmployee(Employee employee) {
        Document doc = new Document("id", employee.getId())
                           .append("name", employee.getName())
                           .append("position", employee.getPosition())
                           .append("salary", employee.getSalary());
        collection.insertOne(doc);
    }

    // Implement other CRUD operations (retrieve, update, delete) as needed
    public synchronized void createEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee object cannot be null.");
        }
        // Generate a unique filename for the employee
        String filename = EMPLOYEE_DIRECTORY + "employee_" + employee.getId() + ".txt";
        File file = new File(filename);

        // Check if the file already exists
        if (file.exists()) {
            throw new IllegalArgumentException("Employee with ID " + employee.getId() + " already exists.");
        }
        try  (FileChannel channel = FileChannel.open(new File(EMPLOYEE_DIRECTORY).toPath());
            FileLock lock = channel.lock())
        {
            // Open a FileWriter for the new file
            FileWriter writer = new FileWriter(filename);

            // Write employee data to the file
            writer.write("ID: " + employee.getId() + "\n");
            writer.write("Name: " + employee.getName() + "\n");
            writer.write("Position: " + employee.getPosition() + "\n");
            writer.write("Salary: " + employee.getSalary() + "\n");

            // Close the FileWriter
            writer.close();

            System.out.println("Employee created and saved to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error creating employee file: " + e.getMessage());
        }
    }
    
    public Employee readEmployeeById(int id) {
        String filename = EMPLOYEE_DIRECTORY + "employee_" + id + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String name = null;
            String position = null;
            double salary = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    name = line.substring(6).trim();
                } else if (line.startsWith("Position:")) {
                    position = line.substring(10).trim();
                } else if (line.startsWith("Salary:")) {
                    salary = Double.parseDouble(line.substring(8).trim());
                }
            }
            return new Employee(id, name, position, salary);
        } catch (IOException | NumberFormatException e) {
            // Handle exceptions (e.g., file not found, invalid data format)
            System.err.println("Error reading employee file: " + e.getMessage());
            return null;
        }
    }

    public List<Employee> readEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        // Loop through all files in the directory
        for (int id = 1; ; id++) {
            String filename = EMPLOYEE_DIRECTORY + "employee_" + id + ".txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                String employeeName = null;
                String position = null;
                double salary = 0;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Name:") && line.substring(6).trim().equals(name)) {
                        employeeName = name;
                    } else if (line.startsWith("Position:")) {
                        position = line.substring(10).trim();
                    } else if (line.startsWith("Salary:")) {
                        salary = Double.parseDouble(line.substring(8).trim());
                    }
                }
                if (employeeName != null) {
                    employees.add(new Employee(id, employeeName, position, salary));
                }
            } catch (IOException | NumberFormatException e) {
                // Handle exceptions
                break; // Break the loop if no more files found
            }
        }
        return employees;
    }

    public List<Employee> readEmployeesByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        // Loop through all files in the directory
        for (int id = 1; ; id++) {
            String filename = EMPLOYEE_DIRECTORY + "employee_" + id + ".txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                String name = null;
                String employeePosition = null;
                double salary = 0;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Position:") && line.substring(6).trim().equals(position)) {
                        employeePosition = line.substring(10).trim();
                    }else if (line.startsWith("Name:")) {
                        name  = line.substring(6).trim();
                    }
                     else if (line.startsWith("Salary:")) {
                        salary = Double.parseDouble(line.substring(8).trim());
                    }
                }
                if (employeePosition != null) {
                    employees.add(new Employee(id, name, employeePosition, salary));
                }
            } catch (IOException | NumberFormatException e) {
                // Handle exceptions
                break; // Break the loop if no more files found
            }
        }
        return employees;
    }

    // Implement similar methods for reading employees by position, etc.

    public void updateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee object cannot be null.");
        }
        String filename = EMPLOYEE_DIRECTORY + "employee_" + employee.getId() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ID: " + employee.getId() + "\n");
            writer.write("Name: " + employee.getName() + "\n");
            writer.write("Position: " + employee.getPosition() + "\n");
            writer.write("Salary: " + employee.getSalary() + "\n");
            System.out.println("Employee updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating employee file: " + e.getMessage());
        }
    }

    public synchronized void deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive integer.");
        }
        String filename = EMPLOYEE_DIRECTORY + "employee_" + id + ".txt";
        File file = new File(filename);
        try (FileChannel channel = FileChannel.open(new File(EMPLOYEE_DIRECTORY).toPath());
             FileLock lock = channel.lock()) 
        {
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Employee with ID " + id + " deleted successfully.");
                } else {
                    System.err.println("Failed to delete employee with ID " + id);
                }
            } else {
                System.err.println("Employee with ID " + id + " does not exist.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
        
    }
}

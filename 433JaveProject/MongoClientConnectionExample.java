import com.mongodb.client.MongoClient;

public class MongoClientConnectionExample {
     
        public static void main(String[] args) {
            MongoClient mongoClient = MongoDBConnection.connect();
            EmployeeRepository repository = new EmployeeRepository(mongoClient);
    
            Employee employee = new Employee(1, "John Doe", "Developer", 50000);
            repository.insertEmployee(employee);
    
            // Perform other operations as needed
        }
    
    
}

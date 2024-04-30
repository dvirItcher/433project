public class EmployeeFactory {
    public static Employee createEmployee(int id, String name, String position, double salary) {
        // Add validation if needed
        return new Employee(id, name, position, salary);
    }
}

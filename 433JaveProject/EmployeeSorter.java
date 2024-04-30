import java.util.List;

public class EmployeeSorter {
    // Merge sort implementation for sorting employees based on salary
    public static void mergeSortBySalary(List<Employee> employees) {
        if (employees.size() <= 1) {
            return;
        }

        int middle = employees.size() / 2;
        List<Employee> left = employees.subList(0, middle);
        List<Employee> right = employees.subList(middle, employees.size());

        mergeSortBySalary(left);
        mergeSortBySalary(right);

        merge(left, right, employees);
    }

    private static void merge(List<Employee> left, List<Employee> right, List<Employee> result) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getSalary() <= right.get(j).getSalary()) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }
}

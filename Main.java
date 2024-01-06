import java.util.*;

public class Main {

    private static String savedUsername;
    private static String savedPassword;
    private static String name;
    private static int age;
    private static String profession;
    private static String location;
    private static String mobileNumber;
    private static String email;

    private static String[] locations = {"Mohakhali", "Mirpur", "Cantornment", "Ghulshan", "Dhanmondi"};

    private static String[][] users = {
        {"Alice", "25", "Engineer", "Mohakhali", "01600000000", "alice@gmail.com"},
        {"Eve", "22", "Student", "Mirpur", "01611111111", "eve@gmail.com"},
        {"Bob", "30", "Teacher", "Cantornment", "01822222222", "bob@gmail.com"},
        {"David", "35", "Doctor", "Ghulshan", "01933333333", "david@gmail.com"},
        {"Charlie", "28", "Artist", "Dhanmondi", "01744444444", "charlie@gmail.com"}
    };

    private static List<int[]>[] adjacencyList = new List[5];

    static {
        for (int i = 0; i < 5; i++) {
            adjacencyList[i] = new ArrayList<>();
        }

        adjacencyList[0].add(new int[]{1, 3});
        adjacencyList[0].add(new int[]{2, 2});
        adjacencyList[0].add(new int[]{3, 5});
        adjacencyList[0].add(new int[]{4, 7});

        adjacencyList[1].add(new int[]{0, 3});
        adjacencyList[1].add(new int[]{2, 1});
        adjacencyList[1].add(new int[]{3, 4});

        adjacencyList[2].add(new int[]{0, 2});
        adjacencyList[2].add(new int[]{1, 1});
        adjacencyList[2].add(new int[]{4, 8});

        adjacencyList[3].add(new int[]{0, 5});
        adjacencyList[3].add(new int[]{1, 4});
        adjacencyList[3].add(new int[]{4, 4});

        adjacencyList[4].add(new int[]{0, 7});
        adjacencyList[4].add(new int[]{2, 8});
        adjacencyList[4].add(new int[]{3, 4});
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();

        System.out.println("Welcome! Let's create your account.");
        System.out.print("Enter a username: ");
        savedUsername = scanner.nextLine();

        System.out.print("Enter a password: ");
        savedPassword = scanner.nextLine();

        clearScreen();

        System.out.println("\n=== Login ===");
        System.out.print("Enter your username: ");
        String enteredUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String enteredPassword = scanner.nextLine();

        if (login(enteredUsername, enteredPassword)) {
            System.out.println("\nLogin Successful! Welcome back, " + savedUsername + "!");
            collectUserInfo(scanner);
            displayAccounts();
        } else {
            System.out.println("\nLogin Failed! Incorrect username or password.");
        }
    }

    private static boolean login(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword);
    }

    private static void collectUserInfo(Scanner scanner) {
        clearScreen();
        System.out.println("\nPlease provide the following information:");
        System.out.print("Name: ");
        name = scanner.nextLine();

        System.out.print("Age: ");
        age = Integer.parseInt(scanner.nextLine());

        System.out.print("Profession: ");
        profession = scanner.nextLine();

        System.out.print("Mobile Number: ");
        mobileNumber = scanner.nextLine();

        System.out.print("Email: ");
        email = scanner.nextLine();
        
        System.out.println("\nChoose your location from the following options:");
        System.out.println("1. Mohakhali");
        System.out.println("2. Cantornment");
        System.out.println("3. Dhanmondi");
        System.out.println("4. Ghulshan");
        System.out.println("5. Mirpur");
        System.out.print("Enter the number corresponding to your location choice: ");

        int locationChoice = Integer.parseInt(scanner.nextLine());

        switch (locationChoice) {
            case 1:
                location = "Mohakhali";
                break;
            case 2:
                location = "Cantornment";
                break;
            case 3:
                location = "Dhanmondi";
                break;
            case 4:
                location = "Ghulshan";
                break;
            case 5:
                location = "Mirpur";
                break;
            default:
                location = "Unknown";
                break;
        }

        System.out.println("\nThank you for providing your information!");
        System.out.println("Location selected: " + location);
        clearScreen();
    }

    private static void displayAccounts() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nChoose an account to view:");

        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". Name: " + users[i][0]);
        }
        System.out.println("6. Sort accounts by age");

        System.out.print("Enter the number corresponding to the account you want to view or '6' to sort by age: ");
        int choice = Integer.parseInt(scanner.nextLine());
        clearScreen();

        if (choice == 6) {
            sortAccountsByAge();
            displaySortedAccounts();
        } else if (choice >= 1 && choice <= users.length) {
            int selectedUserIndex = choice - 1;
            String[] selectedUser = users[selectedUserIndex];

            System.out.println("\nAccount Details:");
            System.out.println("Name: " + selectedUser[0]);
            System.out.println("Age: " + selectedUser[1]);
            System.out.println("Profession: " + selectedUser[2]);
            System.out.println("Location: " + selectedUser[3]);

            int userLocation = getLocationNumber(location);
            int chosenAccountLocation = getLocationNumber(selectedUser[3]);

            int[] shortestPaths = dijkstra(userLocation);
            System.out.println("\nShortest path from user to chosen account: " + shortestPaths[chosenAccountLocation] + "km");
            
            System.out.println("\nAll possible paths from user to chosen account:");
            boolean[] visited = new boolean[adjacencyList.length];
            List<List<Integer>> allPaths = new ArrayList<>();
            List<Integer> currentPath = new ArrayList<>();
            currentPath.add(userLocation);
            visited[userLocation] = true;
            dfs(userLocation, chosenAccountLocation, visited, currentPath, allPaths);
            displayAllPaths(allPaths);

            System.out.println("\nOptions:");
            System.out.println("1. Show additional information");
            System.out.println("2. Back to account");

            System.out.print("Enter your choice: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    System.out.println("Mobile Number: " + selectedUser[4]);
                    System.out.println("Email: " + selectedUser[5]);
                    break;
                case 2:
                    displayAccounts();
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } else {
            System.out.println("Invalid choice.");
        }
        scanner.close();
    }

    private static void displaySortedAccounts() {
        System.out.println("\nAccounts sorted by age:");
        for (String[] user : users) {
            System.out.println("Name: " + user[0] + ", Age: " + user[1]);
        }
        displayAccounts();
    }

    private static int[] dijkstra(int source) {
        int n = adjacencyList.length;
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
    
        boolean[] visited = new boolean[n];
    
        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;
    
            for (int[] edge : adjacencyList[u]) { 
                int v = edge[0];
                int weight = edge[1];
    
                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }
    
        return dist;
    }
    
    private static int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        int n = dist.length;
    
        for (int v = 0; v < n; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
    
        return minIndex;
    }
    

    private static void sortAccountsByAge() {
        mergeSort(users, 0, users.length - 1);
    }

    private static void mergeSort(String[][] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(String[][] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        String[][] leftArr = new String[n1][];
        String[][] rightArr = new String[n2][];

        for (int i = 0; i < n1; ++i) {
            leftArr[i] = arr[left + i].clone();
        }
        for (int j = 0; j < n2; ++j) {
            rightArr[j] = arr[mid + 1 + j].clone();
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (Integer.parseInt(leftArr[i][1]) <= Integer.parseInt(rightArr[j][1])) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    private static int getLocationNumber(String location) {
        switch (location) {
            case "Mohakhali":
                return 0;
            case "Mirpur":
                return 1;
            case "Cantornment":
                return 2;
            case "Ghulshan":
                return 3;
            case "Dhanmondi":
                return 4;
            default:
                return -1;
        }
    }
    private static void dfs(int current, int destination, boolean[] visited, List<Integer> currentPath, List<List<Integer>> allPaths) {
        if (current == destination) {
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }

        for (int[] neighbor : adjacencyList[current]) {
            int nextNode = neighbor[0];
            if (!visited[nextNode]) {
                visited[nextNode] = true;
                currentPath.add(nextNode);
                dfs(nextNode, destination, visited, currentPath, allPaths);
                visited[nextNode] = false;
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    private static void displayAllPaths(List<List<Integer>> allPaths) {
        for (List<Integer> path : allPaths) {
            int distance = calculateDistance(path);
            System.out.print("Path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(locations[path.get(i)]); // Display location corresponding to node number
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println(" (Distance: " + distance + " km)");
        }
    }

    private static int calculateDistance(List<Integer> path) {
        int distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int nodeA = path.get(i);
            int nodeB = path.get(i + 1);
            for (int[] edge : adjacencyList[nodeA]) {
                if (edge[0] == nodeB) {
                    distance += edge[1];
                    break;
                }
            }
        }
        return distance;
    }
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}

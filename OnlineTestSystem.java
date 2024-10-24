import java.util.*;
class User {
    String username;
    String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

class Question {
    String questionText;
    String[] options;
    int correctAnswerIndex;
    public Question(String questionText, String[] options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }
    public String getQuestionText() {
        return questionText;
    }
    public String[] getOptions() {
        return options;
    }
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

class Test {
    List<Question> questions;
    int duration; // in minutes
    public Test(List<Question> questions, int duration) {
        this.questions = questions;
        this.duration = duration;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public int getDuration() {
        return duration;
    }
}

class AuthController {
    private HashMap<String, User> users = new HashMap<>();
    public void register(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            System.out.println("Registration successful for user: " + username);
        } else {
            System.out.println("Username already exists. Please choose a different one.");
        }
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}

class ProfileController {
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }
    public void changeProfile(User user, String newUsername) {
        user.setUsername(newUsername);
    }
}

class TestController {
    Test test;
    int[] userAnswers;
    boolean timeUp = false;

    public TestController(Test test) {
        this.test = test;
        this.userAnswers = new int[test.getQuestions().size()];
    }

    public void startTest() {
        // Timer setup
        long durationMillis = test.getDuration() * 60 * 1000;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationMillis;

        // Start countdown timer thread
        Thread timerThread = new Thread(() -> {
            try {
                Thread.sleep(durationMillis);
                timeUp = true;
                System.out.println("\nTime is up! Test auto-submitted.");
                submitTest(); // Call submitTest here to display the score when time is up
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        timerThread.start();

        Scanner sc = new Scanner(System.in);
        List<Question> questions = test.getQuestions();
        System.out.println("\nYou have " + test.getDuration() + " minutes for this test with " + questions.size() + " questions.");
        for (int i = 0; i < questions.size() && !timeUp; i++) {
            Question q = questions.get(i);
            System.out.println("\n" + (i + 1) + ". " + q.getQuestionText());
            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println(options[j]);
            }
            System.out.print("Your answer (A/B/C/D): ");
            char answer = sc.nextLine().toUpperCase().charAt(0);
            userAnswers[i] = answer - 'A'; 
        }
        if (!timeUp) {
            System.out.println("\nYou have finished answering all questions.");
            System.out.print("Do you want to submit the test now? (y/n): ");
            String submitChoice = sc.nextLine();
            if (submitChoice.equalsIgnoreCase("y")) {
                submitTest();
                return; 
            } else {
                System.out.println("Continuing until time is up...");
            }
        }

        // Wait for the timer thread to complete if time is not up
        if (!timeUp) {
            try {
                timerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // If the time is up and the test is auto-submitted, handled score calculation in the timer thread.
    }

    public void submitTest() {
        int score = 0;
        List<Question> questions = test.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectAnswerIndex()) {
                score++;
            }
        }
        System.out.println("\nYour score: " + score + "/" + questions.size());
    }
}

public class OnlineTestSystem {
    public static void main(String[] args) {
        AuthController authController = new AuthController();
        ProfileController profileController = new ProfileController();
        Scanner sc = new Scanner(System.in);
        Map<String, User> loggedInUsers = new HashMap<>();

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("Select an option (or type 'exit' to quit): ");
            String option = sc.nextLine();

            if (option.equalsIgnoreCase("exit")) {
                break; 
            }
            switch (option) {
                case "1": // Register new user
                    System.out.print("Enter new username: ");
                    String newUsername = sc.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = sc.nextLine();
                    authController.register(newUsername, newPassword);
                    break;

                case "2": // Login existing user
                    System.out.print("Username: ");
                    String username = sc.nextLine();
                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    User user = authController.login(username, password);
                    if (user != null) {
                        System.out.println("Login successful!");
                        loggedInUsers.put(username, user);

                        // Optional password change
                        System.out.print("Do you want to change your password? (y/n): ");
                        String changePasswordChoice = sc.nextLine();
                        if (changePasswordChoice.equalsIgnoreCase("y")) {
                            System.out.print("Enter new password: ");
                            String changedPassword = sc.nextLine();
                            profileController.changePassword(user, changedPassword);
                            System.out.println("Password changed!");
                        }

                        // Optional profile update 
                        System.out.print("Do you want to update your profile? (y/n): ");
                        String updateProfileChoice = sc.nextLine();
                        if (updateProfileChoice.equalsIgnoreCase("y")) {
                            System.out.print("Enter new password: ");
                            String newProfilePassword = sc.nextLine();
                            profileController.changePassword(user, newProfilePassword);
                            System.out.println("Profile updated successfully!");
                        }

                        // Starting the test
                        List<Question> questions = createQuestions();
                        Test test = new Test(questions, 1); // Set duration in minutes
                        TestController testController = new TestController(test);
                        testController.startTest();
                        loggedInUsers.remove(username);
                        System.out.println("User logged out successfully.");
                    } else {
                        System.out.println("Login failed! Please try again.");
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please choose a valid option.");
            }
        }
        sc.close();
    }

    private static List<Question> createQuestions() {
        Question q1 = new Question("What does HTML stand for?",
                new String[]{"A) Hyper Text Markup Language", "B) Hyperlinks and Text Markup Language", "C) Home Tool Markup Language", "D) Hyper Text Multiple Language"}, 0);
        Question q2 = new Question("Which programming language is commonly used for web development?",
                new String[]{"A) Java", "B) Python", "C) C++", "D) JavaScript"}, 3);
        Question q3 = new Question("What is the output of 2 + 2 * 3?",
                new String[]{"A) 6", "B) 8", "C) 10", "D) 12"}, 1);
        Question q4 = new Question("Which data structure uses LIFO (Last In, First Out) ordering?",
                new String[]{"A) Queue", "B) Stack", "C) Linked List", "D) Tree"}, 1);
        return Arrays.asList(q1, q2, q3, q4);
    }
}

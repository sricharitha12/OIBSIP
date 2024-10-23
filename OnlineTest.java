import java.util.*;
class User 
{
    String username;
    String password;
    String profile;
    public User(String username, String password, String profile) 
    {
        this.username= username;
        this.password= password;
        this.profile= profile;
    }
    public String getUsername() {
        return username; 
    }
    public void setUsername(String username) {
        this.username = username; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
    public String getProfile() { 
        return profile; 
    }
    public void setProfile(String profile) { 
        this.profile = profile; 
    }
}

class Question 
{
    String questionText;
    String[] options;
    int correctAnswerIndex;

    public Question(String questionText, String[] options, int correctAnswerIndex) 
    {
        this.questionText= questionText;
        this.options= options;
        this.correctAnswerIndex= correctAnswerIndex;
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

class Exam 
{
    List<Question> questions;
    int duration; 
    public Exam(List<Question> questions, int duration) 
    {
        this.questions= questions;
        this.duration= duration;
    }
    public List<Question> getQuestions() { 
        return questions; 
    }
    public int getDuration() { 
        return duration; 
    }
}

class AuthController 
{
    private HashMap<String, User> users=new HashMap<>();
    public void register(String username, String password, String profile) {
        users.put(username, new User(username, password, profile));
    }

    public User login(String username, String password) 
    {
        User user=users.get(username);
        if(user!=null && user.getPassword().equals(password)) 
            return user;
        return null;
    }
}

class ProfileController 
{
    public void updateProfile(User user, String newProfile) {
        user.setProfile(newProfile);
    }
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }
}

class ExamController 
{
    Exam exam;
    int[] userAnswers;
    boolean timeUp = false;
    public ExamController(Exam exam) 
    {
        this.exam = exam;
        this.userAnswers=new int[exam.getQuestions().size()];
    }
    public void startExam() 
    {
        Timer timer=new Timer();
        long durationMillis=exam.getDuration() * 60 * 1000;
        long startTime=System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() 
        {
            @Override
            public void run() 
            {
                long elapse=System.currentTimeMillis()-startTime;
                long remaining=durationMillis-elapse;
                if(remaining<=0) 
                {
                    timeUp = true;
                    System.out.println("\nTime is up! Exam auto-submitted.");
                    submitExam();
                    timer.cancel();
                } 
                else {
                    long remainingSeconds=remaining / 1000;
                    long minutes=remainingSeconds / 60;
                    long seconds=remainingSeconds % 60;
                    System.out.printf("\rTime remaining: %02d:%02d", minutes, seconds);
                }
            }
        }, 0, 1000);
    
        Scanner sc=new Scanner(System.in);
        List<Question> questions=exam.getQuestions();
        for(int i=0;i<questions.size() && !timeUp;i++) 
        {
            Question q=questions.get(i);
            System.out.println("\n" + (i + 1) + ". " + q.getQuestionText());
            String[] options = q.getOptions();
            for(int j=0;j<options.length;j++) 
                System.out.println(options[j]);
            System.out.print("Your answer: ");
            char answer=sc.nextLine().toUpperCase().charAt(0);
            System.out.println("Your answer: " + answer); 
            userAnswers[i]=answer-'A';
            System.out.print("Time remaining: ");
            System.out.printf("%02d:%02d", (durationMillis - (System.currentTimeMillis() - startTime)) / (60 * 1000), ((durationMillis-(System.currentTimeMillis() - startTime)) / 1000) % 60);
            System.out.println();
        }
        if (!timeUp) {
            submitExam();
            timer.cancel();
        }
    }
    
    void submitExam() 
    {
        int score=0;
        List<Question> questions=exam.getQuestions();
        for(int i=0;i<questions.size();i++) 
        {
            if(userAnswers[i]==questions.get(i).getCorrectAnswerIndex()) 
                score++;
        }
        System.out.println("\nYour score: " + score + "/" + questions.size());
    }
}

public class OnlineTest 
{
    public static void main(String[] args) 
    {
        AuthController authController=new AuthController();
        ProfileController profileController=new ProfileController();
        authController.register("student", "password", "Student Profile");

        Scanner sc=new Scanner(System.in);
        System.out.print("Username: ");
        String username=sc.nextLine();
        System.out.print("Password: ");
        String password=sc.nextLine();
        User user=authController.login(username, password);
        if (user!=null) 
        {
            System.out.println("Login successful!");
            System.out.print("Update username/profile: ");
            String newProfile=sc.nextLine(); 
            profileController.updateProfile(user, newProfile);
            System.out.println("Profile updated!");
            System.out.print("Change password: ");
            String newPassword=sc.nextLine(); 
            profileController.changePassword(user, newPassword);
            System.out.println("Password changed!");

Question q1 = new Question("What does HTML stand for?",
new String[]{"A) Hyper Text Markup Language",
             "B) Hyperlinks and Text Markup Language",
             "C) Home Tool Markup Language",
             "D) Hyper Text Multiple Language"}, 0);
Question q2 = new Question("Which programming language is commonly used for web development?",
new String[]{"A) Java",
             "B) Python",
             "C) C++",
             "D) JavaScript"}, 3);
Question q3 = new Question("What is the output of 2 + 2 * 3?",
new String[]{"A) 6",
             "B) 8",
             "C) 10",
             "D) 12"}, 1);
Question q4 = new Question("Which data structure uses LIFO (Last In, First Out) ordering?",
new String[]{"A) Queue",
             "B) Stack",
             "C) Linked List",
             "D) Tree"}, 1);
Question q5 = new Question("Which of the following is not a programming paradigm?",
new String[]{"A) Object-Oriented Programming (OOP)",
             "B) Procedural Programming",
             "C) Functional Programming",
             "D) Structured Programming"}, 3);
Question q6 = new Question("What does SQL stand for?",
new String[]{"A) Simple Query Language",
             "B) Standard Query Language",
             "C) Structured Query Language",
             "D) Scripted Query Language"}, 2);
Question q7 = new Question("Which sorting algorithm has the worst-case time complexity of O(n^2)?",
new String[]{"A) Merge Sort",
             "B) Quick Sort",
             "C) Bubble Sort",
             "D) Insertion Sort"}, 2);
Question q8 = new Question("What is the function of a compiler?",
new String[]{"A) Converts high-level code to machine code",
             "B) Executes code line by line",
             "C) Interprets code directly without compilation",
             "D) Optimizes code for better performance"}, 0);
Question q9 = new Question("In object-oriented programming, what is encapsulation?",
new String[]{"A) Combining data and functions into a single unit",
             "B) Hiding the implementation details of an object",
             "C) Inheriting properties and behaviors from a superclass",
             "D) Allowing multiple objects to share the same code"}, 1);
Question q10 = new Question("Which of the following is not a type of loop in programming?",
new String[]{"A) for loop",
             "B) while loop",
             "C) do-while loop",
             "D) if-else loop"}, 3);

List<Question> questions = Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10);
Exam exam = new Exam(questions, 10); 

            ExamController examController=new ExamController(exam);
            examController.startExam();
            System.out.println("You have taken the exam within time");
        } 
        else 
            System.out.println("Login failed!");
        sc.close();
    }
}

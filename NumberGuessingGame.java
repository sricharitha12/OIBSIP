import java.util.Scanner;
import java.util.Random;
public class NumberGuessingGame {
    static int minrange=1, maxrange=100, maxattempts=8, maxrounds=2;
    public static void main(String[] args)
    {
        Random r = new Random();
        Scanner sc = new Scanner(System.in);
        int score=0;
        System.out.println("You have 8 attempts in each round");
        
        for(int i=1;i<=maxrounds;i++)
        {
            int attempts=0;
            int genrandom= r.nextInt(maxrange)+minrange;
            System.out.println("Guess the number within "+minrange+" to "+maxrange);

            while(attempts < maxattempts)
            {
                System.out.print("Enter the number to guess the random number generated: ");
                int guess= sc.nextInt();
                attempts++;
                if(guess==genrandom)
                {
                    int round= maxattempts-attempts;
                    score+= round;
                    System.out.print("Yay! You guessed the number successfully, Attempts= "+attempts+", Roundscore= "+round);
                    break;
                }
                else if(guess<genrandom)
                {
                    System.out.println("The number is greater than "+guess+" you have still "+(maxattempts-attempts)+" attempts left.");
                }
                else if(guess>genrandom)
                {
                    System.out.println("The number is less than "+guess+" you have still "+(maxattempts-attempts)+" attempts left.");
                }
                else 
                {
                    System.out.println("Please enter number within range.");
                    sc.next(); 
                }
            }
            if(maxattempts==attempts)
            {
                System.out.println("Attempts left: 0");
                System.out.println("The random number is:"+genrandom);
            }
        }
        System.out.println(" Thankyou for playing. The game is over with round score as "+score);
        sc.close();
    }
}

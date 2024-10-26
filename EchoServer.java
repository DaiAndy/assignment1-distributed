import java.net.*;
import java.io.*;
import java.util.*;

public class EchoServer {

    static ServerSocket serverSocket;
    static Socket clientSocket;
    static DataInputStream br;
    static DataOutputStream dos;

    public static void average() throws IOException {
        br = new DataInputStream(clientSocket.getInputStream());
        dos = new DataOutputStream(clientSocket.getOutputStream());
        int total = 0;
        int count = 0;
        double avg;

        while (true) {
            dos.writeUTF("Please Enter the numbers you would like to average: (Type 'exit' when you are done)");
            String input = br.readUTF();

            if (input.equalsIgnoreCase("exit")) {
                if (count == 0) {
                    dos.writeUTF("no numbers were entered!!!");
                } else {
                    avg = (double) total / count;
                    dos.writeUTF("Your average after " + count + " numbers is: " + avg);
                }
                break;
            } else {
                try {
                    int number = Integer.parseInt(input);
                    total += number;
                    count++;
                } catch (NumberFormatException e) {
                    dos.writeUTF("invalid number");
                }
            }
        }
        dos.writeUTF("Done with average calculator!");
    }

    public static void blackJack() throws IOException {
        // initialization of variables
        br = new DataInputStream(clientSocket.getInputStream());
        dos = new DataOutputStream(clientSocket.getOutputStream());
        int totalHand = 0;
        int compHand = 0;
        int currentCard;
        Random rand = new Random();

        // main function of the game
        while(true) {
            totalHand = 0;
            compHand= 0;
            // generates first two number cards
            currentCard = rand.nextInt(1,11);
            dos.writeUTF("First Card is: " + currentCard);
            totalHand += currentCard;
            currentCard = rand.nextInt(1,11);
            dos.writeUTF("Second Card is: " + currentCard);
            totalHand += currentCard;

            // generates card for dealer
            currentCard = rand.nextInt(11);
            compHand += currentCard;

            dos.writeUTF("Your hand total right now is: " + totalHand);
            dos.writeUTF("The shown number card for the dealer is: " + currentCard);
            compHand += currentCard;

            boolean drawHand = true;
            // asks if user would like another card
            while (drawHand) {
                String deal = br.readUTF().toLowerCase();
                if (deal.equalsIgnoreCase("y")) {
                    // draws another number card for the user
                    currentCard = rand.nextInt(1,11);
                    dos.writeUTF("You Drawed a: " + currentCard);
                    totalHand += currentCard;
                    dos.writeUTF("New Total Hand is: " + totalHand);
                } else if (deal.equalsIgnoreCase("n")) {
                    // user ends their turn
                    drawHand = false;
                }
            }
            while (compHand <= 16) {
                currentCard = rand.nextInt(1,11);
                dos.writeUTF("The next card for the dealer is: " + currentCard);
                compHand += currentCard;
                if (compHand <= 16) {
                    dos.writeUTF("Dealer will be drawing another card!");
                } else {
                    dos.writeUTF("Dealer will be staying!");
                }
            }
            if (totalHand > 21) {
                dos.writeUTF("You went over 21, you lose!");
            } else if (compHand > 21) {
                dos.writeUTF("The dealer went over 21, you win!");
            } else if (totalHand > compHand){
                dos.writeUTF("You have the higher hand, you win!");
            } else if (compHand > totalHand) {
                dos.writeUTF("Dealer hand is higher, you lose");
            }
            dos.writeUTF("Would you like to play again? (y/n)");
            String continueInput = br.readUTF().toLowerCase();

            boolean valid = true;

            while (valid) {
                if (continueInput.equalsIgnoreCase("y")) {
                    dos.writeUTF("Starting next game!");
                    valid = false;
                } else if (continueInput.equalsIgnoreCase("n")) {
                    // break the program
                    valid = false;
                    break;
                } else {
                    dos.writeUTF("Thats an invalid answer!!!");
                }
            }

        }
    }

    public static void main(String[] args) {

        // if the user doesnt add the right amount of parameters
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        // the argument passed will represent as the portNumber
        int portNumber = Integer.parseInt(args[0]);

        // trys the code below
        try {

            // puts in port number to connect to the server socket
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            clientSocket = serverSocket.accept();

            // after server socket accepts, shows user that the client has conncted to the server
            System.out.println("Client connected from IP: " + clientSocket.getInetAddress().getHostAddress() + ", Port " + clientSocket.getPort());
            //System.out.println("Please Choose 1 for BlackJack, or 2 for a average calculator: ");

            br = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());

            dos.writeUTF("Please Choose 1 for BlackJack, or 2 for a average calculator: ");
            int choice = br.readInt();
            System.out.println("Client Chose: " + choice);

            if (choice == 1) {
                blackJack();
            } else if (choice == 2) {
                average();
            }
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            // error output to the sever
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
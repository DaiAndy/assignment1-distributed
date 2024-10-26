import java.io.*;
import java.net.*;
import java.util.Scanner;
 
public class EchoClient {
    public static void main(String[] args) {

        // if the number of parameterse are not met when client trys to connect to server
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        // sets up the received parameters to connect to server
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        // client side replys to the user when runnning
        try {

            // sets up the sockets
            Socket echoSocket = new Socket(hostName, portNumber);
            DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());
            DataInputStream in = new DataInputStream(echoSocket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            String serverReply;
            String userInput;

            // asks user for their choice of program
            System.out.println(in.readUTF());
            int choice = scanner.nextInt();
            out.writeInt(choice);

            // if number blackjack is chosen
            if (choice == 1) {
                System.out.println("Welcome to number blackjack!");

                while (true) {
                    // first user card
                    serverReply = in.readUTF();
                    System.out.println(serverReply);
                    // second user card
                    serverReply = in.readUTF();
                    System.out.println(serverReply);
                    // total number of user hand
                    serverReply = in.readUTF();
                    System.out.println(serverReply);
                    // dealer shown card
                    serverReply = in.readUTF();
                    System.out.println(serverReply);
                    // asks if user would like another card
                    System.out.println("Would you like another card? (y/n)");

                    scanner.nextLine();
                    while (true) {
                        // takes y or n for extra card
                        userInput = scanner.nextLine();
                        out.writeUTF(userInput);

                        if (userInput.equalsIgnoreCase("y")) {
                            // user's new card
                            serverReply = in.readUTF();
                            System.out.println(serverReply);
                            // users new total
                            serverReply = in.readUTF();
                            System.out.println(serverReply);
                        } else if (userInput.equalsIgnoreCase("n")) {
                            System.out.println("User is staying!");
                            break;
                        } else {
                            serverReply = in.readUTF();
                            System.out.println(serverReply);
                            break;
                        }

                    }
                    // dealers second card
                    serverReply = in.readUTF();
                    System.out.println(serverReply);

                    // if dealer is going or staying
                    boolean dealerContinue = true;
                    while (dealerContinue) {
                        serverReply = in.readUTF();
                        System.out.println(serverReply);
                        if (serverReply.contains("Dealer will be staying!")) {
                            dealerContinue = false;
                        }
                    }
                    // win or lose results
                    serverReply = in.readUTF();
                    System.out.println(serverReply);

                    // asks user if they would like to keep playing
                    serverReply = in.readUTF();
                    System.out.println(serverReply);
                    userInput = scanner.nextLine();
                    out.writeUTF(userInput);

                    if (userInput.contains("y")) {
                        System.out.println("Starting new game!");
                    } else if (userInput.contains("n")) {
                        break;
                    }
                }

            // runs avg calculator
            } else if (choice == 2) {

                System.out.println("Please Enter the numbers you would like to average: (Type 'exit' when you are done)");
                while (true) {
                    // skips line to clean print output
                    serverReply = in.readUTF();

                    // takes number from user
                    String num = scanner.nextLine();

                    // writes number to server side, if user writes exit, it exits and calculates average
                    out.writeUTF(num);
                    if (num.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
                // skips line
                serverReply = in.readUTF();

                // prints out average to the user
                serverReply = in.readUTF();
                System.out.println("Server: " + serverReply);
            }

            scanner.close();
            in.close();
            out.close();
            echoSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import game.TTTWebService;
import game.TTTWebService_Service;
import java.awt.Color;

/**
 *
 * @author Vineeth
 */
public class TicTacToeMain extends JFrame implements ActionListener {

    private int gameId, UserId;
    private String gstatereturn, username;

    private String[] Moves;
    private int currentMovesValue = 0;
    private String updated_Move;
    private int updatedMovesValue;
    private String[] latestboard_array;
    private String actionValues;
    private JButton[][] buttons = new JButton[3][3];
    private boolean gameCreator;
    private boolean gameOver;

    private JButton ResignButton = new JButton("Resign"); // Creating a button with name resign
    private JLabel statusLabel = new JLabel("");
    private JPanel ErrorPanel = new JPanel();
    private JLabel ErrorMsg = new JLabel("");
    private JLabel statusmsg = new JLabel("");

    private ImageIcon X = new ImageIcon(this.getClass().getResource("X.png")); // Loading the path of image x in variable X
    private ImageIcon O = new ImageIcon(this.getClass().getResource("O.png"));// Loading the path of image o in variable

    // Making an instance of TTT webservice 
    TTTWebService_Service webservice = new TTTWebService_Service();
    TTTWebService proxy = webservice.getTTTWebServicePort();

    // Constuctot taking usrname, gameID, usserID and a boolean value which will be true 
    //or false deping updon the game state like created game or joined game.
    public TicTacToeMain(String usrname, int gmID, int usrID, boolean creator) {
        gameId = gmID; // Assigning the value to the local global variable
        UserId = usrID; // Assigning the value to the local global variable
        gameCreator = creator;// Assigning the value to the local global variable
        username = usrname;// Assigning the value to the local global variable

        setTitle("Tic Tac Toe"); // Setting title of the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // on clicking the close it will dispose the frame
        setResizable(false); // not allowing the user to resize the frame.

        // Creating the board layout
        JPanel centerPanel = new JPanel(new GridLayout(3, 3));
        // Font font = new Font("Arial", Font.BOLD, 32);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                // buttons[i][j].setFont(font);
                buttons[i][j].addActionListener((ActionEvent e) -> {
                    //System.out.println("clicked resign");
                    //Calling the Action lisner function:
                    actionPerformed(e);

                });
                buttons[i][j].setFocusable(false);
                centerPanel.add(buttons[i][j]);
            }
        }
        // performing action while clicking on resign button
        ResignButton.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

            }
        }));
        JPanel southPanel = new JPanel();
        // Adding the buttons in the panel
        southPanel.add(ResignButton);
        southPanel.add(ErrorMsg, "South");
        southPanel.add(statusmsg, "North");

        JPanel northPanel = new JPanel();
        northPanel.add(statusLabel);
        statusLabel.setText("Player " + username); // Setting the username in the jlabel

        add(northPanel, "North");
        add(centerPanel, "Center");
        add(southPanel, "South");

        setSize(400, 400); // size of the frame is difined
        // Checking the condition if is created or joined., if created then disabling the buttons and calling the thread.
        if (gameCreator) {
            disableButtons();
            TrackSecondPlayer();
            // if joined then setting the text and asking the second player to start the move.
        } else {
            statusmsg.setText("Please make the Move to start the Game");

        }
    }

    // A method which calls the thread and keeps the track of ssecond player.
    private void TrackSecondPlayer() {
        Thread Checking = new Thread() {
            @Override
            public void run() {
                boolean end = false;
                // creating the thread and keeping the track of the second user.
                while (!end) {
                    try {
                        System.out.println("Thread calletd");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("Thread sleep error");
                    }
                    // Getting the gameId from webservice and checking the condotions.
                    String gameStartStatus = proxy.getGameState(gameId);
                    switch (gameStartStatus) {
                        case "-1":
                            statusmsg.setText("Waiting for Other Player to join");

                            break;
                        case "0":
                            statusmsg.setText("Waiting for other player to move");
                           
                            break;
                        case "1":
                            disableButtons();
                            statusmsg.setText("Player One has Won!!!");
                            gameOver = true;
                            break;

                        case "2":
                            disableButtons();
                            statusmsg.setText("Player two has Won!!!");
                            gameOver = true;
                            //statusmsg.setForeground(Color.red);
                            break;
                        case "3":
                            disableButtons();
                            statusmsg.setText("Draw Match");
                            gameOver = true;
                            //statusmsg.setForeground(Color.red);
                            break;
                        case "ERROR-NOGAME":
                            statusmsg.setText("Problem in finding Game");
                            //ErrorMsg.setForeground(Color.red);
                            break;
                        case "ERROR-DB":
                            statusmsg.setText("Problem in accessing the Database");
                            //ErrorMsg.setForeground(Color.red);

                            break;

                        default:

                            break;
                    }
                    // Once the conditions are  checked calling the getboard fuctions to check the moves.
                    // and make the appropriate move in the board.

                    updated_Move = proxy.getBoard(gameId);
                    String[] updatedMoves = updated_Move.split("\\n");
                    Moves = (updatedMoves[updatedMoves.length - 1]).split(",");
                    switchCheck(updated_Move, false);
                    if (updatedMovesValue > currentMovesValue && UserId != Integer.valueOf(Moves[0])) {
                        // Setting the icon o in the board if the came is created by the user.
                        if (gameCreator) {
                            buttons[Integer.valueOf(Moves[1])][Integer.valueOf(Moves[2])].setIcon(O);
                        } // Setting the icon x in the board if the game is joined by the user.
                        else {
                            buttons[Integer.valueOf(Moves[1])][Integer.valueOf(Moves[2])].setIcon(X);
                        }
                        // Checking if game is over or not
                        if (!gameOver) {
                            setButtonsEnabled();
                            buttons[Integer.valueOf(Moves[1])][Integer.valueOf(Moves[2])].setEnabled(false);
                            currentMovesValue = updatedMovesValue;

                            statusmsg.setText("Your Turn");
                        }
                        end = true;
                    }
                }

            }
        };
        Checking.start(); // starting the thread checking.
    }
    // Action performed to check the which row and column is selected.
    public void actionPerformed(ActionEvent event) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (event.getSource() == buttons[i][j]) {
                    // Calling the click function
                    click(i, j);
                    disableButtons();
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    // Click fucntion takes the value of row and colum clicked by the user given by the actionListner
    private void click(int i, int j) {

        // Calling the webservice function to get the square values from database
        actionValues = proxy.takeSquare(i, j, gameId, UserId);
        // Checking the condtions
        switch (actionValues) {
            case "0":
                ErrorMsg.setText(" unsucessfull in adding square to the move table  ");
                ErrorMsg.setForeground(Color.red);
                break;

            case "ERROR-TAKEN":
                ErrorMsg.setText("Selected Square is already taken, please select other");
                ErrorMsg.setForeground(Color.red);
                break;
            case "ERROR-DB":
                ErrorMsg.setText("Problem in accessing the Database");
                ErrorMsg.setForeground(Color.red);
                break;
            case "ERROR":
                ErrorMsg.setText("Some Error ");
                ErrorMsg.setForeground(Color.red);
                break;
            default:
                // Calling the update function
                updateMove(i, j);
                currentMovesValue++; // incrementing the current moves values
                CheckWin();
                try {
                    if (!gameOver) {
                        TrackSecondPlayer(); // Calling the fucntion which contains thread if game is not over
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

        }

    }
    /// SwitchCheck functions takes the value last move value and chce

    public void switchCheck(String Value, boolean firstMove) {
        switch (Value) {
            case "ERROR-NOMOVES":
                //ErrorMsg.setText(" No moves taken yet");
                //ErrorMsg.setForeground(Color.red);
                break;
            case "ERROR-DB":
                //ErrorMsg.setText("Problem in accessing the Database");
                //ErrorMsg.setForeground(Color.red);
                break;
            default:
                latestboard_array = Value.split("\\n");

                if (!firstMove) {
                    updatedMovesValue = latestboard_array.length;
                }
        }

    }
  //  Checking the win, Loss and Draw condition calling the checkwin method from webservice
    private void CheckWin() { 
        String winStatus = proxy.checkWin(gameId);
        switch (winStatus) {
            case "ERROR-RETRIEVE":
                ErrorMsg.setText(" No moves taken yet");
                ErrorMsg.setForeground(Color.red);
                break;
            case "ERROR-NOGAME":
                ErrorMsg.setText("Problem in accessing the Database");
                ErrorMsg.setForeground(Color.red);
                break;
            case "0":
                break;
            case "1":
                disableButtons();
                statusmsg.setText("Player One Won!!");
                setGameState(gameId, 1);
                gameOver = true;
                break;
            case "2":
                disableButtons();
                statusmsg.setText("Player Two Won!!");
                setGameState(gameId, 2);
                gameOver = true;
                break;
            case "3":
                disableButtons();
                statusmsg.setText("Draw");
                gameOver = true;
                setGameState(gameId, 3);
                break;
            default:

        }

    }
    // Update move will take the value of row and coun and set that to an image icon

    public void updateMove(int x, int y) {

        if (gameCreator) {
            buttons[x][y].setIcon(X);
        } else {
            buttons[x][y].setIcon(O);
        }

    }
    // setting the status 
    private void setStatus(String s) {
        statusLabel.setText(s);
    }
   // Diasable the buttons once the user has made the moves
    public void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);

            }
        }

    }
    // enabling the button to make the move
    private void setButtonsEnabled() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(true);

            }
        }
    }
    
    // After playing the game the setting the game state value in data base.
    private void setGameState(int gid, int gst) {
        gstatereturn = proxy.setGameState(gid, gst);
        switch (gstatereturn) {
            case "ERROR-NOGAME":
                ErrorMsg.setText(" Unable to find the game ");
                ErrorMsg.setForeground(Color.red);
                break;
            case "ERROR-DB":
                ErrorMsg.setText("Problem in accessing the Database");
                ErrorMsg.setForeground(Color.red);
                break;
            case "1":
                break;

        }

    }

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TicTacToeMain(8,2,false).setVisible(true);
//            }
//        });
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import game.TTTWebService;
import game.TTTWebService_Service;
/**
 *
 * @author Vineeth
 */
public class Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Creating the object of th startup page and setting is visible
        StartUp startup = new StartUp(); // Creating object og the class startup and calling
        startup.setVisible(true); // setting the startup screen visible
        startup.setLocationRelativeTo(null); // setting the location of the screen in centre
        
        
    }
    
}

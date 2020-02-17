/*
Daniel Krouguerski
06/04/2019
This is the code for space invaders, a game in which a space ship is controlled 
by the user and the objective is to shoot down all of the aliens
 */
package a5_space; 
// importing libraries
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class A5_Space extends JPanel implements KeyListener{
    
    int x, y; // variables for the spaceship
    int numBullets = 3; // max number of bullets you are able to have on the screen
    int numAliens = 1; // number of aliens - also score to win
    int score = 0; // initial score
    boolean Win = false; // boolean to win
    boolean Lose = false; // boolean to lose 
    boolean showScore = true; // boolean to show the score
    
    Font FontBig = new Font ("Arial",Font.BOLD, 80); // font for "you won" and "you lost"
    Font FontScore = new Font ("Arial",Font.BOLD, 30); // font for score
    
    boolean shipAlive = true; // boolean for whether or not the ship is alive
    
    boolean[] Bullets = new boolean[numBullets]; // boolean for bullets - array
    int[]     BulletsX = new int[numBullets]; // array for the x-values of bullets
    int[]     BulletsY = new int[numBullets]; // array for the y-values of bullets
    
    boolean[] AliensAlive = new boolean[numAliens]; // boolean for whether or not each alien is alive - array
    boolean[] AliensVisible = new boolean[numAliens]; // boolean for whether or not each alien is visible - array 
    int[]     AliensDirection = new int[numAliens]; // array for the movement value of each alien
    int[]     AliensX = new int[numAliens]; // array for the x-values of each alien
    int[]     AliensY = new int[numAliens]; // array for the x-values of each alien
    
    Image ship, scaledShip, alien, scaledAlien; // declare images used
    
    A5_Space() {
    this.addKeyListener (this); //add listener to keyboard 
    setFocusable (true);
    
    try {
        ship = ImageIO.read(new File("ship.jpeg")); // open ship file
        for (int i = 0; i < numBullets; i++) { // for each bullet
            Bullets[i] = false; // each bullet is false because it is not being currently used
            BulletsX[i] = 0; // initialize x and y values
            BulletsY[i] = 0;            
         }
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
    }
    scaledShip = ship.getScaledInstance(150, 150, Image.SCALE_DEFAULT); // scale the ship
    try {
        alien = ImageIO.read(new File("alien.gif")); // open alien file
        for (int i = 0; i < numAliens; i++) { // for each alien
            AliensAlive[i] = true; // the alien is alive
            AliensX[i] = (-i * 150) + 1000; // all aliens are off the screen, 150 points apart
            AliensVisible[i] = AliensX[i] >=0; // aliens are only visible if x value is greater or equal to 0
            AliensY[i] = 0; // aliens start on the top of the screen           
            AliensDirection[i] = 5; // each start with a movement value of 5
         }
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
    }
    scaledAlien = alien.getScaledInstance(100, 100, Image.SCALE_DEFAULT); // scale the alien
    }
    boolean pressedLeft, pressedRight, pressedSpace; //declare booleans
    
    public void paintComponent(Graphics g) {
                
	super.paintComponent(g); // clears the screen
	Dimension s = this.getSize(); // gets the size of this (the current) window
        
        g.setColor(Color.black); // draws frame 
	g.fillRect(0, 0, s.width, s.height);
        
        if (showScore){ // display the score if showScore is true
            g.setColor(Color.white);
            g.setFont(FontScore);
            g.drawString("Score: " + score, 1100, 925);
        }
        if (shipAlive) // draw the ship if it's alive
            g.drawImage(scaledShip, x + 600, y + 772, null);
        
        if (pressedLeft){ // move the ship left if you hit the left arrow key
            x = x - 20;
            if (x + 600 < 0) { //if ship is hitting left wall
                x = x + 20;
            }    
        }
        else if (pressedRight){ // move the ship right if you hit the right arrow key
            x = x + 20;
            if (x + 740 > s.width) { //if ship is hitting left wall
                x = x - 20;
            } 
        }  
        else if (pressedSpace){ // shoot the bullet if you hit space
            for (int i = 0; i < numBullets; i++){ // for each bullet, set the appropriate x and y values based on the position of the ship
                if (!Bullets[i]) { // bullets aren't used
                    Bullets[i] = true; // set that bullet to true to later draw it
                    BulletsX[i] =  x + 672;
                    BulletsY[i] = 792;
                    break;
                }
            }
            pressedSpace = false; // set pressedSpace to false so that you can shoot again
        }
        alien(g); 
        bullet(g);
        status(g);
    }
    
    public void alien(Graphics g){
                
        Dimension s = this.getSize(); // get size of current frame
        
        for (int i = 0; i < numAliens; i++){ // for each alien
            
            if(!AliensAlive[i]) // if the alien is dead
                continue; // move onto the next alien
             
            if (AliensVisible[i]) // draw the alien if it's visible
                g.drawImage(scaledAlien, AliensX[i], AliensY[i], null);
            
            AliensX[i] = AliensX[i] + AliensDirection[i]; // move the alien right or left
            
            if (AliensX[i] + 100 >= s.width || (AliensX[i] <= 0) && AliensVisible[i]){ // if alien hits the right or left wall
                AliensDirection[i] *= -1; // direction value is inverted
                if (AliensDirection[i] < 0) // alien speeds up if it moves down a row
                    AliensDirection[i] -= 2;
                else
                    AliensDirection[i] += 2;
                
                AliensY[i] += 100; // move each alien down a row individually 
            }
            if (AliensX[i] >= x + 600 && AliensX[i] <= x + 700 && AliensY[i] + 100 >= y + 801 && AliensY[i] + 100 <= y + 901){ // if an alien hits a ship
                shipAlive = false; // ship is no longer alive, subsequently doesn't draw ship 
                Lose = true; // set lose to true
                showScore = false; // no longer display score
            }
            AliensVisible[i] = AliensX[i] > 0; // if the x value of the alien is greater than 0, it's visible
        }
        
        delay(20);
        repaint(); // Schedule a call to repaint
    }
    public void status(Graphics g){ // sees if you won or lost the game
        if (Win){ // if you won, display appropraite message
            g.setColor(Color.green);
            g.setFont(FontBig);
            g.drawString("You Won!", 500, 500);                
        }
        else if (Lose){ // if you lost, display appropriate message
            g.setColor(Color.red);
            g.setFont(FontBig);
            g.drawString("You Lost!", 500, 500);
            }
    }   
    public void bullet(Graphics g){
        g.setColor(Color.red); // set bullet color to red
        if (shipAlive){ // only consider bullets if the ship is alive
            for (int i = 0; i < numBullets; i++){ // for each bullet
                if(!Bullets[i]) // if bullet is not being used
                    continue; // move onto the next bullet

                g.fillRect(BulletsX[i], BulletsY[i], 5, 10); // draw the bullet
                BulletsY[i] -= 20; // move the bullet up the screen
                if (BulletsY[i] + 10 < 0){ // if bullet is entirely off the screen
                    Bullets[i] = false; // set that bullet to false
                }
            }
            for (int i = 0; i < numBullets; i++){ // for each bullet
                for (int j = 0; j < numAliens; j++){ // for each alien
                    if (!AliensAlive[j]) // if alien is dead
                        continue; // move onto the next alien

                    if (Bullets[i] && BulletsY[i] > AliensY[j] && BulletsY[i]< AliensY[j] + 100 && BulletsX[i] > AliensX[j] && BulletsX[i] < AliensX[j] + 100){ // collision detection 
                       Bullets[i] = false; // set bullet to false - no longer displays it
                       AliensAlive[j] = false; // "kill" the alien - no longer displays it
                       score += 1; // add one to score
                        if (score == numAliens){ // if all aliens are killed
                            Win = true; // you win
                        }        
                    }
                }
            }
        }
        delay(10);
        repaint(); // Schedule a call to repaint
   }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders!"); //Creates frame
	frame.getContentPane().add(new A5_Space());
	frame.setSize(1300, 975); //dimensions for frame
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void delay(int mili) {
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            System.out.println("ERROR IN SLEEPING");
        }
    }
    public void keyPressed (KeyEvent e) //detects when keys are hit 
    {
	char key = (char) e.getKeyCode ();
       
	if (key == e.VK_LEFT) // left key pressed
	    pressedLeft = true;
	else if (key == e.VK_RIGHT) //right key pressed
	    pressedRight = true;
	else if (key == e.VK_SPACE) //space key pressed
	    pressedSpace = true;
	repaint ();
    }
  
    public void keyReleased (KeyEvent e) //detects when keys are released
    {
	pressedLeft = pressedRight = pressedSpace = false; //sets booleans to false
	repaint (); //repaints frame
    }
    
    public void keyTyped (KeyEvent e) //manditory but doesn't do anything 
    {
	 
    }
}

/*
Daniel Krouguerski
June 14, 2019
This is the code for the game pacman
*/
package pacman;
// importing libraries
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class Pacman extends JPanel implements KeyListener{
    // naming all images
    Image PacmanRight, PacmanLeft, PacmanUp, PacmanDown, Ghost1, Ghost2, Ghost3, Ghost4, ScaledpacmanRight, ScaledpacmanLeft, ScaledpacmanUp, ScaledpacmanDown, Scaledghost1, Scaledghost2, Scaledghost3, Scaledghost4;
    boolean pressedRight, pressedLeft, pressedUp, pressedDown, pacmanRight, pacmanLeft, pacmanUp, pacmanDown, win, lose, initialized = false; // all non array booleans
    Dimension s;
    
    int numGhosts = 4; // how many ghosts there will be
    int score = 0; // player's score
    // all variables for ghosts
    boolean[] GhostsRight = new boolean[numGhosts]; // if ghost is going up down left or right
    boolean[] GhostsLeft = new boolean[numGhosts];
    boolean[] GhostsUp = new boolean[numGhosts];
    boolean[] GhostsDown = new boolean[numGhosts];
    boolean[] GhostsNewDirection = new boolean[numGhosts]; // how fast ghosts will be going
    int[]     GhostsXDirection = new int[numGhosts]; // array for the movement value of each ghost
    int[]     GhostsYDirection = new int[numGhosts]; // array for the movement value of each ghost
    int[]     GhostsX = new int[numGhosts]; // array for the x-values of each ghost
    int[]     GhostsY = new int[numGhosts]; // array for the y-values of each ghost
    boolean[] Ghosts = new boolean[numGhosts];
    
    boolean[][] Pellets = new boolean[20][20]; // boolean for whether or not pellets are true
    int[]       PelletsX = new int[20]; // x-value for each pellet
    int[]       PelletsY = new int[20];  // y-value for each pellet
    
    int pacmanX = 0; // x and y-values for pacman
    int pacmanY = 0;
    int pacmanXDirection = 4; // how fast pacman will be going
    int pacmanYDirection = 4;
    
    Font font = new Font ("Arial", Font.BOLD, 60); // font for win or lose 
    Font FontScore = new Font ("Arial", Font.BOLD, 40); // font for score            
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    Pacman() {
    this.addKeyListener (this); //add listener to keyboard 
    setFocusable (true);
    // opens all images    
    try {
        PacmanRight = ImageIO.read(new File("PacmanRight.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        PacmanLeft = ImageIO.read(new File("PacmanLeft.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        PacmanUp = ImageIO.read(new File("PacmanUp.gif"));
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        PacmanDown = ImageIO.read(new File("PacmanDown.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        Ghost1 = ImageIO.read(new File("ghost1.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        Ghost2 = ImageIO.read(new File("ghost2.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        Ghost3 = ImageIO.read(new File("ghost3.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    try {
        Ghost4 = ImageIO.read(new File("ghost4.gif")); 
        } 
        catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
    // scales all images
    ScaledpacmanRight = PacmanRight.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    ScaledpacmanLeft = PacmanLeft.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    ScaledpacmanUp = PacmanUp.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    ScaledpacmanDown = PacmanDown.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    Scaledghost1 = Ghost1.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    Scaledghost2 = Ghost2.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    Scaledghost3 = Ghost3.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    Scaledghost4 = Ghost4.getScaledInstance(50, 50, Image.SCALE_DEFAULT);

 }
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public void paintComponent (Graphics g){
        super.paintComponent(g); // clears the screen	       
        s = this.getSize(); // gets the size of this (the current) window
    
        if (!initialized) // initialize all values
            Initialize();
        // calls all functions
        delay(10);
        MovePacman(g);
        stage(g);
        Pellets(g);
        ghosts(g);
        repaint();
    }
    
    public void Initialize(){
        for (int i = 0; i < numGhosts; i++){ // for each ghost, give x-value and y-value
            if (i == 0){
                GhostsX[i] = 0;
                GhostsY[i] = 800;
                Ghosts[i] = true;
            }
            else if (i == 1){
                GhostsX[i] = 462;
                GhostsY[i] = 0;
                Ghosts[i] = true;
            }
            else if (i == 2){
                GhostsX[i] = s.width - 50;
                GhostsY[i] = 800;
                Ghosts[i] = true;
            }
            else if (i == 3){
                GhostsX[i] = 362;
                GhostsY[i] = 650;
                Ghosts[i] = true;
            }
            GhostsXDirection[i] = 4;
            GhostsYDirection[i] = 4;
            GhostsNewDirection[i] = true;    
        }
         
        for(int i = 0; i < 20; i++) { // for each pellet, give x-value and y-value
            PelletsX[i] = 50 * i + 25;
            PelletsY[i] = 50 * i + 25;
            if (PelletsX[i] > 400)
                PelletsX[i] += 13;
        }
         
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                Pellets[i][j] = WithinMaze(PelletsX[i], PelletsY[j], 5); // makes all pellets which are not within the boundaries false
        initialized = true; // make this boolean true so that values are only initialized once
    }    
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void stage(Graphics g){
        
        g.setColor(Color.blue);
        // draw the stage
        g.drawLine(0, s.height - 200, s.width, s.height - 200);
        g.drawLine(0, 0, 0, s.height - 200);
        g.drawLine(s.width - 1, 0, s.width - 1, s.height - 200);
        g.drawLine(0, 0, s.width, 0);
        g.drawRect(50, 50, 200, 200);
        g.drawRect(50, 600, 200, 200);
        g.drawRect(625, 50, 200, 200);
        g.drawRect(629, 600, 200, 200);
        g.drawRect(412, -1, 50, 251);
        g.drawRect(-1, 412, 251, 50);
        g.drawRect(629, 412, 251, 50);
        g.drawRect(412, 600, 50, 252);
        g.drawRect(412, 412, 50, 50);
        
        if (!lose){ // if player didn't lose
            if (!WithinMaze(pacmanX, pacmanY, 50)) { // if pacman is in the maze, and boolean for directions are true, move pacman
                if(pacmanRight)
                    pacmanX -= pacmanXDirection;
                if(pacmanLeft)
                    pacmanX += pacmanXDirection;
                if(pacmanUp)
                    pacmanY += pacmanYDirection;
                if(pacmanDown)
                    pacmanY -= pacmanYDirection;
            }
        }
        if (win){ // if you won all aliens are false and message is printed
            for (int i = 0; i < numGhosts; i++) {
                g.setFont(font);
                g.setColor(Color.green);
                Ghosts[i] = false;
                g.drawString("You won!", 300, s.height - 100);
            }    
        }
        else if (lose){ // if you lost a message is printed
            g.setFont(font);
            g.setColor(Color.red);
            g.drawString("You lost!", 300, s.height - 150);
        }
        g.setColor(Color.white); // display score
        g.setFont(FontScore);
        g.drawString("Score: " + score, s.width - 300, s.height - 150);
    }    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void MovePacman (Graphics g){
        
        g.setColor(Color.black); // draws frame 
	g.fillRect(0, 0, s.width, s.height);
        
        if (!lose){ // if you have not lost, see when arrow keys are pressed and make the corresponding boolean true
            
            if (pressedRight){
                pacmanRight = true;
                pacmanLeft = false;
                pacmanUp = false;
                pacmanDown = false;
            }
            else if (pressedLeft){
                pacmanLeft = true;
                pacmanRight = false;
                pacmanUp = false;
                pacmanDown = false;      
            }
            else if (pressedUp){
                pacmanUp = true;
                pacmanDown = false;
                pacmanRight = false;
                pacmanLeft =  false;
            }
            else if (pressedDown){
                pacmanDown = true;
                pacmanRight = false;
                pacmanLeft = false;
                pacmanUp = false;
            }
            if (!pacmanRight && !pacmanLeft && !pacmanUp && !pacmanDown){ // pacman at the start of the game
                g.drawImage(ScaledpacmanRight, pacmanX, pacmanY, null);
            }
            // draws pacman "going" into the direction he's supposed to be going, makes him go in that direction, and assigns left right top and bottom wall boundaries
            else if (pacmanRight){
                g.drawImage(ScaledpacmanRight, pacmanX, pacmanY, null);
                pacmanX += pacmanXDirection;
                if (pacmanX + 50 > s.width){
                    pacmanX -= pacmanXDirection;
                }
            }
            else if (pacmanLeft){
                g.drawImage(ScaledpacmanLeft, pacmanX, pacmanY, null);
                pacmanX -= pacmanXDirection;
                if (pacmanX < 0){
                    pacmanX += pacmanXDirection;
                }
            }
            else if (pacmanUp){
                g.drawImage(ScaledpacmanUp, pacmanX, pacmanY, null);
                pacmanY -= pacmanYDirection;
                if (pacmanY < 0){
                    pacmanY += pacmanYDirection;
                }
            }
            else if (pacmanDown){
                g.drawImage(ScaledpacmanDown, pacmanX, pacmanY, null);
                pacmanY += pacmanYDirection;
                if(pacmanY + 50> s.height - 200){
                    pacmanY -= pacmanYDirection;
                }
            }
            for (int i = 0; i < numGhosts; i++){ // collision for each ghost and pacman
                if (Math.abs(GhostsX[i] - pacmanX) <= 50 && Math.abs(GhostsY[i] - pacmanY) <= 50)
                    lose = true; // if ghosts hit pacman, player loses
                } 
        }
            for (int i = 0; i < 20; i++){ // collision for each pellet and pacman
                for (int j = 0; j < 20; j++){
                    if (Pellets[i][j] && Math.abs(PelletsX[i] - pacmanX - 25) <= 20 && Math.abs(PelletsY[j] - pacmanY - 25) <= 20){
                        score += 1; // add to score if pacman ate a pellet
                        if (score == 204) // max number of points, you won
                            win = true;
                        Pellets[i][j] = false; // make each pellet false
                    } 
                }
            }    
        }
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void Pellets (Graphics g){
        g.setColor(Color.yellow);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (Pellets[i][j]) {
                    g.fillOval(PelletsX[i], PelletsY[j], 5, 5); // draws each pellet
                }
            }
        }
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void ghosts (Graphics g){
        
        for (int i = 0; i < numGhosts; i++){ // draws each ghost, max number of ghosts is 4
            if (Ghosts[i]){
                if (i == 0)
                    g.drawImage(Scaledghost1, GhostsX[i], GhostsY[i], null);
                else if (i == 1)
                    g.drawImage(Scaledghost2, GhostsX[i], GhostsY[i], null);
                else if (i == 2)
                    g.drawImage(Scaledghost3, GhostsX[i], GhostsY[i], null);
                else if (i == 3){
                    g.drawImage(Scaledghost4, GhostsX[i], GhostsY[i], null);
                }
                GhostMovement(i);
            }
        }
    }    
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pacman!"); //Creates frame
	frame.getContentPane().add(new Pacman());
	frame.setSize(900, 1100); //dimensions for frame
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
         
    }
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void keyPressed (KeyEvent e) { //detects when keys are hit 
	char key = (char) e.getKeyCode ();
       
	if (key == e.VK_LEFT) // left key pressed
	    pressedLeft = true;
	else if (key == e.VK_RIGHT) // right key pressed
	    pressedRight = true;
	else if (key == e.VK_UP) // up key pressed
	    pressedUp = true;
        else if (key == e.VK_DOWN) // down key pressed
	    pressedDown = true;
    }
  
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void keyReleased (KeyEvent e){ //detects when keys are released
	pressedLeft = pressedRight = pressedUp = pressedDown = false; //sets booleans to false
    }
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public void keyTyped (KeyEvent e){ //manditory but doesn't do anything 
	 
    }
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    
    public static void delay(int mili) { // delay for movement
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            System.out.println("ERROR IN SLEEPING");
        }
    }
    
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        
    public boolean WithinMaze(int x, int y, int size){ // boolean for whether or not something is in the maze - can be used for pellets, pacman, and ghosts - returns false if not within the stage
        if (x + size > s.width)
            return false;
        if (x < 0)
            return false;        
        if (y + size > s.height - 200)
            return false;
        if (y < 0)
            return false;
       
        if(y + size > 50 && y < 250 && x + size > 50 && x < 250)
            return false;
        if(y + size > 600 && y < 800 && x + size > 50 && x < 250)
            return false;
        if(y + size > 50 && y < 250 && x + size > 629 && x < 829)
            return false;
        if(y + size > 600 && y < 800 && x + size > 629 && x < 829)
            return false;
        if(y + size > 0 && y < 250 && x + size > 412 && x < 462)
            return false;
        if(y + size > 412 && y < 462 && x + size > 0 && x < 250)
            return false;
        if(y + size > 412 && y < 462 && x + size > 629 && x < 869)
            return false;
        if(y + size > 600 && y < 850 && x + size > 412 && x < 462)
            return false;
        if(y + size > 412 && y < 462 && x + size > 412 && x < 462)
            return false;
        
        return true; // return true if it is in within the boundaries
    }
    
    public void GhostMovement(int i) {
       
        if (GhostsNewDirection[i]){ // if the ghost needs to pick a new direction
            Random a = new Random();
            int GhostsMovement = a.nextInt(4) + 1; // generate random number from 1 to 4

            if (GhostsMovement == 1 && CanMoveUp(GhostsX[i],GhostsY[i])){ // if 1 is generated, and ghost can go up, set up boolean to true
                GhostsUp[i] = true;
                GhostsDown[i] = false;
                GhostsRight[i] = false;
                GhostsLeft[i] = false;
                
               GhostsNewDirection[i] = false; // ghost no longer needs to pick a new direction
            }
            else if (GhostsMovement == 2 && CanMoveDown(GhostsX[i],GhostsY[i])){ // same thing but for the other directinos
                GhostsUp[i] = false;
                GhostsDown[i] = true;
                GhostsRight[i] = false;
                GhostsLeft[i] = false;
                
                GhostsNewDirection[i] = false;
            }
            else if (GhostsMovement == 3 && CanMoveRight(GhostsX[i],GhostsY[i])){
                GhostsUp[i] = false;
                GhostsDown[i] = false;
                GhostsRight[i] = true;
                GhostsLeft[i] = false;
                
                GhostsNewDirection[i] = false;
            }
            else if (GhostsMovement == 4 && CanMoveLeft(GhostsX[i], GhostsY[i])){
                GhostsUp[i] = false;
                GhostsDown[i] = false;
                GhostsRight[i] = false;
                GhostsLeft[i] = true;
                
                GhostsNewDirection[i] = false;
            }  
        }
             
        if (!GhostsNewDirection[i]){ // if ghost doesnt need to pick a new direction - kind of does the same as CanMoveRight(), or other directions, without actually moving the ghost
            int possibleX = GhostsX[i];
            int possibleY = GhostsY[i];
            if (GhostsUp[i])
                possibleY -= GhostsYDirection[i];
            else if (GhostsDown[i])
                possibleY += GhostsYDirection[i];
            else if (GhostsRight[i])
                possibleX += GhostsXDirection[i];
            else if (GhostsLeft[i])
                possibleX -= GhostsXDirection[i];
            
            if(!WithinMaze(possibleX, possibleY, 50)) // if ghost proceeds to move, and it won't be in the maze, make it pick a new direction
                GhostsNewDirection[i] = true;
            else { // if the ghost's future position is still within the maze, move the ghost
                GhostsX[i] = possibleX;
                GhostsY[i] = possibleY;
            }
        }        
    }
    
    public boolean CanMoveRight(int x, int y) // sees if something can go right without leaving the boundaries
    {
        return WithinMaze(x, y, 50);
    }
    
    public boolean CanMoveLeft(int x, int y) // sees if something can go left without leaving the boundaries
    {
        return WithinMaze(x, y, 50);
    }
    
    public boolean CanMoveDown(int x, int y) // sees if something can go down without leaving the boundaries
    {
        return WithinMaze(x, y, 50);
    }
    
    public boolean CanMoveUp(int x, int y) // sees if something can go up without leaving the boundaries
    {
        return WithinMaze(x, y, 50);
    }    
}                                                                                                                                                                                                                                                        
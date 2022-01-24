/****************************************************************************************
* Name:       	Poker Game																*
* Author:    	Connie He and Matthew Smith												*
* Date:        	Nov 22, 2018															*
* Purpose:     	1 or 2 players will be dealt 5 cards and the players will select which	*
*				cards to discard in attempt to create the highest ranking hands			*
*           	The 2 hands will them be compared and a winner will be determined. 		*
****************************************************************************************/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class GameTemplate1 extends JPanel {
    //window
    static JPanel panel; // main drawing panel
    static JFrame frame; // window frame which contains the panel
    static final int WINDOW_WIDTH = 1200; // width of display window
    static final int WINDOW_HEIGHT = 800; // height of display window
	
	//game stages
    static int gameStage = 0; 
    static final int WELCOME_SCREEN = 0;
    static final int MENU = 1;
    static final int INSTRUCTIONS = 2;
	static final int INSTRUCTIONS2 = 11;
    static final int PLAY = 3;
	static final int P1CHOOSE = 5;
	static final int P2CHOOSE = 6;
	static final int reDeal = 7; 
	static final int P1WINS = 8; 
	static final int P2WINS = 9; 
	static final int TIE = 10; 
    static final int END_GAME = 4;
	
	//images
    static Image[] card = new Image [53]; //images of all 52 cards in the deck
    static Image MainMenu;            
    static Image WelcomeScreen; 
    static Image Instructions1; 
    static Image Instructions2;
    static Image Background;
    static Image Chip; 	
	
	//cards & hands
    static int [] playerHand = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //players hand, updated throughout game
																		 //first 5 cards are Player 1's, last 5 cards are Computer/Player 2's
    static int [] newCardSet = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //set of cards that replace the discarded cards
    static int [] cardLocation = {200, 200, 200, 200, 200, 200, 200, 200, 200, 200}; //number of pixles the face down cards should be printed along the x axis
    static int [] backOrFront = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; // values to tell if a card is face up or face down (odd is face up, even if face down) 
    static String [] cardName = {"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13",
								 "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10", "H11", "H12", "H13",
								 "S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10", "S11", "S12", "S13",
								 "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11", "C12", "C13",
								 "cardBack"}; //all card images + card back
   	static String[] hand1 = {"X", "X", "X", "X", "X"}; //player 1's hand after update
    static String[] hand2 = {"X", "X", "X", "X", "X"}; //player 2's hand after update 
    static int[] cardValues1 = {0, 0, 0, 0, 0}; //player 1's hand's card values after update
    static int[] cardValues2 = {0, 0, 0, 0, 0}; //player 2's hand's card values after update
    static int[] handrank1 = {0, 0, 0, 0}; //player 1's hand type, lowest value of pairs/four of a kind, highest value of pairs/three of a kind
    static int[] handrank2 = {0, 0, 0, 0}; //player 2's hand type, lowest value of pairs/four of a kind, highest value of pairs/three of a kind
	
    static int numPlayers = 0; //number of players
	static String playerOneName = ""; //player 1's name
    static String playerTwoName = ""; //player 2's name
    
	
	//initialize window for game
    public static void main(String args[]) {

        // Create Image Object
        Toolkit tk = Toolkit.getDefaultToolkit();
        // Load background images

        // This loop loads all the cards and assignes them to the card array (image) 
        for (int i = 0; i < 53; i++) {
            URL url = GameTemplate1.class.getResource(cardName[i] + ".png"); // Loads card images into game
            card[i] = tk.getImage(url); // assigns card images to the array
        } //for

        // importing all the images
        URL url2 = GameTemplate1.class.getResource("MainMenu.jpg");
        MainMenu = tk.getImage(url2);
        URL url3 = GameTemplate1.class.getResource("WelcomeScreen.gif");
        WelcomeScreen = tk.getImage(url3);
        URL url4 = GameTemplate1.class.getResource("InstructionsPt1.jpg");
        Instructions1 = tk.getImage(url4);
        URL url5 = GameTemplate1.class.getResource("InstructionsPt2.jpg");
        Instructions2 = tk.getImage(url5);
        URL url7 = GameTemplate1.class.getResource("Background.jpg");
        Background = tk.getImage(url7);
        URL url8 = GameTemplate1.class.getResource("CHIP.png");
        Chip = tk.getImage(url8);

        // Create Frame and Panel to display graphics in

        panel = new GameTemplate1();

        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // set size of application window
        frame = new JFrame("Poker"); // set title of window
        frame.add(panel);

        // add a key input listener (defined below) to our canvas so we can respond to key pressed
        frame.addKeyListener(new KeyInputHandler());

        frame.addMouseListener(new MouseListenerExample());

        // exits window if close button pressed
        frame.addWindowListener(new ExitListener());


        // request the focus so key events come to the frame
        frame.requestFocus();
        frame.pack();
        frame.setVisible(true);

    } // main
	
	
    // paintConponent draws everything in the graphics window. Image, shape or string that you see is drawn from here.   
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // calls the paintComponent method of JPanel to display the background

        // welcome screen
        if (gameStage == WELCOME_SCREEN) {
            // draw background
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.setColor(new Color(24, 160, 202));
            g.drawImage(WelcomeScreen, 0, 0, this);
        } //if
		
        // menu
		else if (gameStage == MENU) {
            g.drawImage(MainMenu, 0, 0, this);
        } //else if
		
        // instructions
		else if (gameStage == INSTRUCTIONS) {
            g.drawImage(Instructions1, 0, 0, this);
        } //else if
		
		else if (gameStage == INSTRUCTIONS2) {
            g.drawImage(Instructions2, 0, 0, this);
        } //else if
		
        // game play
		else if (gameStage == PLAY) {
            int y = 100; // pixles on the y axis the cards are displayed at

            // draw screen with all the images, printed strings for this stage of the game
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.drawImage(Background, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 400);
            drawString(g, "Click anywhere to start.", 420, 700);
            g.setFont(new Font("Serif", Font.BOLD, 100));
            drawString(g, "POKER GAME", 250, 250);

            // loop to print the 10 randomized cards on the screen
            for (int i = 0; i < 10; i++) {
                if (i > 4) {
                    y = 450;
                } // if
                g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this); // display the image for the card value randomized with the "hand" method and assigned to "playerHand" int array. 
            } // for
            panel.repaint();
        } // PLAY

        // Player 1 chooses which cards they want to discard by toggling their hand with keys 1-5
        else if (gameStage == P1CHOOSE) {
            int y = 100; // y axis value

            // draw screen
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.drawImage(Background, 0, 0, this);
            g.drawImage(Chip, 540, 300, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 400);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawString(g, playerOneName + ": Please select the cards you wish to discard and click CONFIRM to finalize.", 100, 250);

            // loop to print the 10 randomized cards on the screen
            for (int i = 0; i < 10; i++) {
                if (i > 4) {
                    y = 450;
                } // if
                g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this); // display the image for the card value randomized with the "hand" method and assigned to "playerHand" int array. 
            } // for	

            // loop to show the cards face up or face down
            for (int i = 0; i < 5; i++) {
                if (backOrFront[i] % 2 == 0) {
                    g.drawImage(card[52], cardLocation[i] * 100 + 100, 100, this); // display the back side of the card when the number of times the user has toggled the number key is even
                } else {
                    g.drawImage(card[playerHand[i]], cardLocation[i] * 100 + 100, 100, this); // display the original card for the user to see if the number of times the user has toggled the number key is odd
                } // else
            } // for
        } // P1CHOOSE

        // Player 2 chooses which cards they want to discard by toggling their hand with keys 1-5
        else if (gameStage == P2CHOOSE) {
            int y = 100; // y axis value

            // draw screen 
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.drawImage(Background, 0, 0, this);
            g.drawImage(Chip, 540, 300, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 400);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));

            drawString(g, playerTwoName + ": Please select the cards you wish to discard and click CONFIRM\nto finalize.", 600, 610);

            // loop to print the 10 randomized cards on the screen
            for (int i = 0; i < 10; i++) {
                if (i > 4) {
                    y = 450;
                } // if
				
                g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this); // display the image
            } // for

            // loop to show the cards face up or face down
            for (int i = 5; i < 10; i++) {
                if (backOrFront[i] % 2 == 0) {
                    g.drawImage(card[52], cardLocation[i] * 100 + 100, 450, this); // display the image
                } else {
                    g.drawImage(card[playerHand[i]], cardLocation[i] * 100 + 100, 450, this); // display the image
                } // else
            } // for
        } // P2CHOOSE


        // The cards that were face down when the user selected "6" (the enter button) get replaced by new random cards not used in the game yet
        else if (gameStage == reDeal) {
            // 1 player mode
            int y = 100; // pixles on the y axis the cards are displayed at

            // draw screen with all the images, printed strings for this stage of the game
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.drawImage(Background, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 270);

            if (numPlayers == 1) {

                // Computer Chooses which cards to discard.

				for (int i = 5; i < 10; i++){
					for (int j = 0; j < 5; j++){
						if (i + j == i){
							continue;
						} //if
						
						else if (playerHand[i] % 13 == playerHand[j + 5]){
							backOrFront[i] = 1;
						} //else if
						
						else {
							backOrFront[i] = 2;
						} //else
					} //nested for
				} //for
				
                // If any card is higher than a 10 it keeps it. 
                for (int i = 5; i < 10; i++) {
                    if (playerHand[i] % 13 < 5) {
                        backOrFront[i] = 1;
                    } else {
                        backOrFront[i] = 2;
                    } //if else
                } //for

                // finding the position of the face down cards and replacing their original values (playerHand) with new, never used before values (newCardSet)
                for (int i = 0; i < 10; i++) {
                    if (backOrFront[i] % 2 == 0) {
                        playerHand[i] = newCardSet[i]; // if the card is face down (or has an even "backOrFront" value) then that position card (1, 2, 3, 4 or 5) in the players hand get replaced with a new card in newCardSet  
						for (int j = 5; j < 10; j++){
							if (backOrFront[j] % 2 == 0){
								g.drawImage(card[52], cardLocation[j] * 100 + 100, 450, this);
							} // if
						} // for
					} // if
                } // for
				
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}

                winner();

                // Draws the now updated player 1's hand
                for (int i = 0; i < 5; i++) {
                    g.drawImage(card[playerHand[i]], i * 100 + 100, 100, this);
                } // for

                // Draws the now updated computer's hand
                for (int i = 5; i < 10; i++) {
                    g.drawImage(card[playerHand[i]], i * 100 + 100, 450, this);
                } // for

            } else {
                // 2 player mode

                // finding the position of the face down cards and replacing their original values (playerHand) with new, never used before values (newCardSet) IN BOTH HANDS 
                for (int i = 0; i < 10; i++) {
                    if (backOrFront[i] % 2 == 0) {
                        playerHand[i] = newCardSet[i]; // if the card is face down (or has an even "backOrFront" value) then that position card (1, 2, 3, 4 or 5) in the players hand get replaced with a new card in newCardSet   
                    } //if
                } //for

                winner();
				
				//draw both players' updated hands
                for (int i = 0; i < 10; i++) {
                    if (i > 4) {
                        y = 450;
                    } // if

                    g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this);
                } // for

            } // else
		} // reDeal
	
		else if (gameStage == P1WINS) {
			int y = 100; // pixles on the y axis the cards are displayed at

            // draw screen with all the images, printed strings for this stage of the game
            g.drawImage(Background, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 270);

            // Draw players' hands randomized cards on the screen
            for (int i = 0; i < 10; i++) {
                if (i > 4) {
                    y = 320;
                } // if
                g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this); // display the image for the card value randomized with the "hand" method and assigned to "playerHand" int array. 
            } // for
			
            // Prints player 1's winning hand and high card
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 32));	
			drawString(g, playerOneName + " wins with a " + getRankName(handrank1[0]) + " with a High Card of " + getHighCardName(handrank1[3]), 150,500);
            
			// Prints player 2 (or computers) hand and high card
			drawString(g, playerTwoName + " has a " + getRankName(handrank2[0]) + " with a High Card of " + getHighCardName(handrank2[3]), 150,550);
			
			g.setFont(new Font("SansSerif", Font.BOLD, 20));
			drawString(g, "Click anywhere to return to main menu.", 420, 750);
		} // P1WINS
      
      	else if (gameStage == P2WINS) {
            int y = 100; // pixles on the y axis the cards are displayed at

            g.drawImage(Background, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            drawString(g, playerOneName, 100, 50);
            drawString(g, playerTwoName, 600, 270);

            // Draw players' hands randomized cards on the screen
            for (int i = 0; i < 10; i++) {
                if (i > 4) {
                    y = 320;
                } // if
                g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this); // display the image for the card value randomized with the "hand" method and assigned to "playerHand" int array. 
            } // for

			
            // Prints player 2 (or computers) winning hand and high card
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 32));	
			drawString(g, playerTwoName + " wins with a " + getRankName(handrank2[0]) + " and a High Card of " + getHighCardName(handrank2[3]), 150, 500);
            
            // Prints player 1's hand name and high card
			drawString(g, playerOneName + " has a " + getRankName(handrank1[0]) + " and a High Card of " + getHighCardName(handrank1[3]), 150, 550);
			
			g.setFont(new Font("SansSerif", Font.BOLD, 20));
			drawString(g, "Click anywhere to return to main menu.", 420, 750);	
		} // P2WINS
      
      	else if (gameStage == TIE) {
			int y = 100;  // pixles on the y axis the cards are displayed at
			
			// draw screen with all the images, printed strings for this stage of the game
            g.fillRect (0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.drawImage(Background, 0, 0, this);
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 28));
			drawString(g, playerOneName, 100, 50);
			drawString(g, playerTwoName, 600, 270);
			
			// Draw players' hands randomized cards on the screen
			for (int i = 0; i < 10; i++) {
				if (i >	4) {
					y = 320; 
				} // if
				g.drawImage(card[playerHand[i]], (i + 1) * 100, y, this);  // display the image for the card value randomized with the "hand" method and assigned to "playerHand" int array. 
			} // for
			
			// setting handType to the player 1 and player 2's hand based on the numarical rank it was given.
			String handType = getRankName(handrank1[0]); 
			
			// Prints player 1's and 2's equal hand
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 32));	
			drawString(g, "It's a tie. Both players had an equal " + handType, 350, 500);
			
			g.setFont(new Font("SansSerif", Font.PLAIN, 20));
			drawString(g, "Click anywhere to return to main menu.", 420, 750);
		} // TIE
      
      	else {
            g.setColor(Color.white);
        } // else
    } // paintComponent

	// generates 10 DIFFERENT random numbers between 0 and 51 and fills the array playerHand with these 10 values, each to represent a card
	public static void hand () {
		int rand = 0; // random card 
			
		// loop to draw 10 random numbers, none repeating
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++){
				rand = (int)(Math.random() * 51);
				
				if (rand == playerHand[j]){
					continue; 
				} //if
				
				break; 
			} //for
			playerHand[i] = rand; 			
		} // for
	} // hand
	
	// generates 10 random new cards NOT IN EITHER HAND to replace cards the user(s) wants to discard
	public static void newCards () {
		int rand = 0; // random card
		
		//draw 10 random numbers, none repeating OR found in either original hand
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++){
				rand = (int)(Math.random() * 51);
				
				if (rand == playerHand[j]){
					continue;
				} // if		
				
				break;
			} //for 
			newCardSet[i] = rand;
		} //for
	} // newCards
	
	private static class MouseListenerExample extends Frame implements MouseListener {

        MouseListenerExample() {
            addMouseListener(this);
        }

        public void mouseClicked(java.awt.event.MouseEvent ev) {

            if (gameStage == WELCOME_SCREEN) {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                //click anywhere to begin program
                if (x > 0 && x < 1200 && y > 0 && y < 800) {
                    showMenu();
                } // if
            } // WELCOME_SCREEN
			
            else if (gameStage == MENU) {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                //Show Instructions 
                if (x > 180 && x < 530 && y > 345 && y < 380) {
                    showInstructions();
                } //if

                //One Player Game Mode
                else if (x > 180 && x < 375 && y > 410 && y < 445) {
                    numPlayers = 1;
                    reset();
                    hand();
                    newCards();
                    startGame();
                    getPlayerOneName();
                    playerTwoName = "Computer";
                } //else if

                //Two Player Game Mode
                else if (x > 180 && x < 400 && y > 470 && y < 510) {
                    numPlayers = 2;
                    reset();
                    hand();
                    newCards();
                    startGame();
                    getPlayerOneName();
                    getPlayerTwoName();
                } //else if

                //Exit Game
                else if (x > 180 && x < 380 && y > 530 && y < 570) {
                    System.exit(0);
                } //else if
            } // MENU
			
            else if (gameStage == INSTRUCTIONS) {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                //Show next page
                if (x > 0 && x < 1200 && y > 0 && y < 800) {
                    showInstructions2();
                } //if
            } //INSTRUCTIONS
			
            else if (gameStage == PLAY) {
                startP1();
            } // PLAY
			
            else if (gameStage == P1CHOOSE) {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                // detects when a key is pressed and sets the card location(for displaying the card Back image) and increments backOrFront (to determine if the card is face down or face up)
                if (x > 110 && x < 205 && y > 100 && y < 310) {
                    cardLocation[0] = 0;
                    backOrFront[0]++;
                    panel.repaint();
                } //if
				
                else if (x > 210 && x < 305 && y > 100 && y < 310) {
                    cardLocation[1] = 1;
                    backOrFront[1]++;
                    panel.repaint();
                } //else if
				
                else if (x > 310 && x < 405 && y > 100 && y < 310) {
                    cardLocation[2] = 2;
                    backOrFront[2]++;
                    panel.repaint();
                } //else if
				
                else if (x > 410 && x < 505 && y > 100 && y < 310) {
                    cardLocation[3] = 3;
                    backOrFront[3]++;
                    panel.repaint();
                } //else if
				
                else if (x > 510 && x < 605 && y > 100 && y < 310) {
                    cardLocation[4] = 4;
                    backOrFront[4]++;
                    panel.repaint();
                } //else if
				
                else if (x > 540 && x < 740 && y > 300 && y < 500) {
                    if (numPlayers == 2) {
                        endP1();
                    } else {
                        endP2();
                    } //if else

                    panel.repaint();
                } //else if
            } // P1CHOOSE
			
            else if (gameStage == P2CHOOSE) {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                // detects when a key is pressed and sets the card location(for displaying the card Back image) and increments backOrFront (to determine if the card is face down or face up)
                if (x > 610 && x < 705 && y > 450 && y < 660) {
                    cardLocation[5] = 5;
                    backOrFront[5]++;
                    panel.repaint();
                } //if
				
                else if (x > 710 && x < 805 && y > 450 && y < 660) {
                    cardLocation[6] = 6;
                    backOrFront[6]++;
                    panel.repaint();
                } //else if
				
                else if (x > 810 && x < 905 && y > 450 && y < 660) {
                    cardLocation[7] = 7;
                    backOrFront[7]++;
                    panel.repaint();
                } //else if
				
                else if (x > 910 && x < 1005 && y > 450 && y < 660) {
                    cardLocation[8] = 8;
                    backOrFront[8]++;
                    panel.repaint();
                } //else if
				
                else if (x > 1010 && x < 1105 && y > 450 && y < 660) {
                    cardLocation[9] = 9;
                    backOrFront[9]++;
                    panel.repaint();
                } //else if
				
                else if (x > 540 && x < 740 && y > 300 && y < 500) {
                    endP2();
                    panel.repaint();
                } //else if
            } //P2CHOOSE
			
            else {
                int x, y;
                x = ev.getX();
                y = ev.getY();

                if (x > 0 && x < 1200 && y > 0 && y < 800) {
                    showMenu();
                } //if
            } //else 			
        } //mouseClicked


        public void mouseEntered(MouseEvent e) {} //mouseEntered

        public void mouseExited(MouseEvent e) {} //mouseExited

        public void mousePressed(MouseEvent e) {} //mousePressed

        public void mouseReleased(MouseEvent e) {} //mouseReleased

    } //MouseListener


    /* A class to handle keyboard input from the user.
     * Implemented as a inner class because it is not
     * needed outside the EvenAndOdd class.
     */
    private static class KeyInputHandler extends KeyAdapter {
        public void keyTyped(KeyEvent e) {
            // quit when the user presses "escape"
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        } // keyTyped
    } // KeyInputHandler class

    /* Shuts program down when close button pressed */
    private static class ExitListener extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.exit(0);
        } // windowClosing
    } // ExitListener

    private static void showMenu() {
        gameStage = MENU;
        panel.repaint();
    } // showMenu

    // sets game up to display instructions
    private static void showInstructions() {
        gameStage = INSTRUCTIONS;
        panel.repaint();
    } // showInstructions

    private static void showInstructions2() {
        gameStage = INSTRUCTIONS2;
        panel.repaint();
    } // showInstructions

    // sets game up to instruct players to start game
    private static void startGame() {
        gameStage = PLAY;
        panel.repaint();

    } // playGame
	
	private static void getPlayerOneName() {
        String error = "";
        do {
            playerOneName = JOptionPane.showInputDialog(error + "\nPlayer 1, please enter a username: ");

            if (playerOneName == null) {
                gameStage = MENU;
            } //if
			
			else if (playerOneName.length() == 0) {
                error = "<html><font color=#FF3333>**Invalid name, please try again**</font>";
                continue;
            } //else if

            break;
        } while (true);
    } //getPlayerOneName
	
	private static void getPlayerTwoName() {
		String error = ""; 
		do {
			playerTwoName = JOptionPane.showInputDialog(error + "\nPlayer 2, please enter a username: ");
			
			if (playerTwoName == null) {
				gameStage = MENU;
			} //if
			
			else if (playerTwoName.length() == 0){
				error = "<html><font color=#FF3333>**Invalid name, please try again**</font>"; 
				continue;
			} //else if
			
			break;
		} while(true);
	} //getPlayerTwoName
	
	// Player 1 chooses cards to discard
	private static void startP1() {
        gameStage = P1CHOOSE; 
    } //startP1
	
	// Player 2 chooses cards to discard
	private static void endP1() {
        gameStage = P2CHOOSE;
    } //endP1
	
	// new cards are dealt
	private static void endP2() {
        gameStage = reDeal; 
    } //endP2
	
	//determine the winner of the game
	private static void winner() {
        for (int i = 0; i < 5; i++) {
            hand1[i] = cardName[playerHand[i]];

            hand2[i] = cardName[playerHand[i + 5]];
        } //for
		
		//get each player's values of cards in hand
        cardValues1 = getCardValues(hand1);
        cardValues2 = getCardValues(hand2);
		
		//determine the hand rank of each player
        handrank1 = evaluateHand(hand1);
        handrank2 = evaluateHand(hand2);

        compareHands(handrank1[0], handrank2[0]);
    } //winner
	
	//get the name of the High Card
	private static String getHighCardName(int cardNum) {
		String highCardName = ""; //name of the High Card
		
		switch (cardNum) {
			case 1: highCardName = "Two"; break; 
			case 2: highCardName = "Three"; break;
			case 3: highCardName = "Four"; break;
			case 4: highCardName = "Five"; break;
			case 5: highCardName = "Six"; break;
			case 6: highCardName = "Seven"; break;
			case 7: highCardName = "Eight"; break;
			case 8: highCardName = "Nine"; break;
			case 9: highCardName = "Ten"; break;
			case 10: highCardName = "Jack"; break; 
			case 11: highCardName = "Queen"; break;
			case 12: highCardName = "King"; break;
			case 13: highCardName = "Ace"; break;
		} //switch
		
		return highCardName; 
	} //getHighCardName
	
	//reset game
    private static void reset() {
        for (int i = 0; i < 10; i++) {
            playerHand[i] = 53;
            newCardSet[i] = 53;
            cardLocation[i] = 200;
            backOrFront[i] = 1;
        }

        playerOneName = "";
        playerTwoName = "";
    } //reset
	
    /*  draw multi-line Strings
     *  author: John Evans
     */
    private static void drawString(Graphics g, String text, int x, int y) {
        // draws each line on a new line
        for (String line : text.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        } // for
    } // drawString
	
	//get the value of every card in hand
    public static int[] getCardValues(String[] hand) {
        int[] handValue = new int[5]; //all card values in hand
        char[] cardValue = null; //contains both suit and value of a single card when updated

        //get numeric value of each card & sort from low to high
        for (int i = 0; i < 5; i++) {

            cardValue = hand[i].toCharArray();

            if (cardValue.length == 3) {
                handValue[i] = Character.getNumericValue(cardValue[2]) + 10;
            } else {
                handValue[i] = Character.getNumericValue((cardValue[1]));
            } //if else

        } //for
        Arrays.sort(handValue);

        return handValue;
    } //getCardValues
	
	//get the suits of every card in hand
    public static char[] getCardSuits(String[] hand) {
        char[] suits = new char[5]; //all card suits in hand
        char[] cardValue = null; //contains both suit and value of a single card when updated

        for (int i = 0; i < 5; i++) {

            cardValue = hand[i].toCharArray();
            suits[i] = cardValue[0];

        } //for

        return suits;
    } //getCardSuits
	
	//get the rank name of hand
	public static String getRankName (int rank){
		String rankName = "";
		switch (rank) {
			case 1: rankName = "Royal Flush"; break;
			case 2: rankName = "Straight Flush"; break;
			case 3: rankName = "Four of a Kind"; break;
			case 4: rankName = "Full House"; break;
			case 5: rankName = "Flush"; break;
			case 6: rankName = "Straight"; break;
			case 7: rankName = "Three of a Kind"; break;
			case 8: rankName = "Two Pair"; break;
			case 9: rankName = "Pair"; break;
			case 10: rankName = "High card"; break;
		} //switch
		return rankName;
	} //getRankName
	
	//evalute and rank player's hand
	public static int[] evaluateHand (String[] hand){
		int[] handRank = new int [4];   //index 0 contains rank of hand
										//index 1 contains the value of the highest valued Pair or value of the Four of a Kind
      									//index 2 contains the value of the lowest valued Pair or value of the Three of a Kind
      									//index 3 contains the value of the High Card
		int[] handValue = getCardValues(hand); //all card values in hand
		char[] cardSuit = getCardSuits(hand); //all card suits in hand
		boolean[][] counting = new boolean [5][14]; //row represents number of repetitions &
      												//column represents value of repeated card
		int[] royalArray = {9, 10, 11, 12, 13}; //all values needed for a Royal Flush
		int counter = 1; //number of repetitions
		int highCard = handValue[4]; //value of highest card
		boolean pair = false;
		boolean twoPair = false;
		boolean threeOfAKind = false;
		boolean straight = false;
		boolean flush = false;
		boolean fullHouse = false;
		boolean fourOfAKind = false;
		boolean straightFlush = false;
		boolean royalFlush = false;
			
		//check if rank of hand is High Card
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < (5 - i); j++) {
                if (handValue[i] != handValue[i + j]) {
                    handRank[0] = 10;
                } else {
                    handRank[0] = 0;
                    break;
                } //if else
            } //nested for
            if (handRank[0] == 0) {
                break;
            } //if
        } //for

        //count number of repetitions of a card
        if (handRank[0] == 0) {
            for (int i = 0; i < 4; i++) {
                if (handValue[i] == handValue[i + 1]) {
                    counting[counter][handValue[i]] = false;

                    counter++;

                    counting[counter][handValue[i]] = true;
                } else {
                    counter = 1;
                } //if else
            } //for
        } //if

        //determine if hand contains a Pair, Three of a Kind, or Four of a Kind
        for (int row = 0; row < counting.length; row++) {
            for (int col = 0; col < counting[row].length; col++) {
                if (counting[row][col] == true) {
                    switch (row) {
                        case 2:
                            pair = true;
                            handRank[1] = col;

                            for (int i = (col + 1); i < counting[2].length; i++) {
                                if (counting[row][col] == counting[row][i]) {
                                    twoPair = true;
                                    pair = false;
                                    handRank[2] = i;
                                    break;
                                } //if 
                            } //for

                            break;
                        case 3:
                            threeOfAKind = true;
                            handRank[2] = col;
                            break;
                        case 4:
                            fourOfAKind = true;
                            handRank[1] = col;
                            break;
                    } //switch
                } //if
            } //nested for
        } //for

        //*straight*
        for (int i = 0; i < 4; i++) {
            if (handValue[i + 1] == (handValue[i] + 1)) {
                straight = true;
            } else {
                straight = false;
                break;
            } //if else
        } //for

        //*flush*
        for (int i = 1; i < 5; i++) {
            if (cardSuit[i] != cardSuit[0]) {
                flush = false;
                break;
            } else {
                flush = true;
            } //if else
        } //for

        //*fourOfAKind*
        if (fourOfAKind == true) {
            handRank[0] = 3;
        } //if

        //*fullHouse*
        else if (threeOfAKind == true && pair == true) {
            handRank[0] = 4;
        } //else if

        //*threeOfAKind*
        else if (threeOfAKind == true && pair == false) {
            handRank[0] = 7;
        } //else if

        //*twoPair*
        else if (twoPair == true) {
            handRank[0] = 8;
        } //else if

        //*pair*
        else if (pair == true) {
            handRank[0] = 9;
        } //else if

        //*royal flush*
        else if (Arrays.equals(handValue, royalArray) && flush == true) {
            handRank[0] = 1;
            flush = false;
        } //if

        //*straight flush*
        else if (flush == true && straight == true) {
            handRank[0] = 2;
            flush = false;
            straight = false;
        } //else if

        //*flush*
        else if (flush == true) {
            handRank[0] = 5;
        } //else if

        //*straight*
        else if (straight == true) {
            handRank[0] = 6;
        } //else if

        handRank[3] = highCard;

        return handRank;
    } //evaluateHand
	
	//compare the players' hands
	public static int compareHands (int rank1, int rank2){
		String rankName1 = getRankName(rank1); //name of player one's hand rank
		String rankName2 = getRankName(rank2); //name of player two's hand rank
		
		if (rank1 < rank2){
			gameStage = P1WINS;
		} //if
		
		else if (rank1 > rank2){
			gameStage = P2WINS;
		} //else if
		
		else {
			compareHandValues(rank1);
		} //else
			
		return gameStage;
	} //compareHands

	//compare values in two equal hands
    public static void compareHandValues(int handType) {
        switch (handType) {
            case 8:
                if (handrank1[2] > handrank2[2]) {
                    gameStage = P1WINS;
                    break;
                } //if
                else if (handrank2[2] > handrank1[2]) {
                    gameStage = P2WINS;
                    break;
                } //else if
            case 9:
                if (handrank1[1] > handrank2[1]) {
                    gameStage = P1WINS;
                    break;
                } //if
                else if (handrank2[1] > handrank1[1]) {
                    gameStage = P2WINS;
                    break;
                } //else if
            case 2:
            case 5:
            case 6:
            case 10:
                for (int i = 4; i >= 0; i--) {
                    if (cardValues1[i] > cardValues2[i]) {
                        gameStage = P1WINS;
                        break;
                    } //if
                    else if (cardValues2[i] > cardValues1[i]) {
                        gameStage = P2WINS;
                        break;
                    } //else if
                } //for
                break;
            case 3:
                if (handrank1[1] > handrank2[1]) {
                    gameStage = P1WINS;
                } else {
                    gameStage = P2WINS;
                } //if else
                break;
            case 4:
            case 7:
                if (handrank1[2] > handrank2[2]) {
                    gameStage = P1WINS;
                } else {
                    gameStage = P2WINS;
                } //if else
                break;
            case 1:
                gameStage = TIE;
                break;
        } //switch

        panel.repaint();
    } //compareHandValues

} // Even and Odd
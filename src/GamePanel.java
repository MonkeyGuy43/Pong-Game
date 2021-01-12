import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
	
	static final int Game_Width = 1000;
	static final int Game_Height = (int)(Game_Width * (0.5555));
	static final Dimension Screen_Size = new Dimension(Game_Width, Game_Height);
	static final int Ball_Diameter = 20;
	static final int Paddle_Width = 25;
	static final int Paddle_Height = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
	private JFrame frame;
 
	GamePanel(){
		newPaddle();
		newBall();
		score = new Score(Game_Width, Game_Height);
		this.setFocusable(true);
		this.addKeyListener(new ActionListener());
		this.setPreferredSize(Screen_Size);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newBall() {
		 //creates a new ball on a random spot on the y axis
		 random = new Random();
		 ball = new Ball((Game_Width/2) - (Ball_Diameter/2),random.nextInt(Game_Height - Ball_Diameter),Ball_Diameter,Ball_Diameter);
	}
	
	public void newPaddle() {
		//creates new paddles
		paddle1 = new Paddle(0,(Game_Height/2)-(Paddle_Height/2),Paddle_Width,Paddle_Height,1);
		paddle2 = new Paddle(Game_Width-Paddle_Width,(Game_Height/2)-(Paddle_Height/2),Paddle_Width,Paddle_Height,2);

	}
	public void paint(Graphics g) {
		//creates the image
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	public void draw(Graphics g) {
		//adds in all the models
		paddle1.draw(g);
		paddle2.draw(g);
        ball.draw(g);		
		score.draw(g);
		Toolkit.getDefaultToolkit().sync();
	}
	public void move() {
		//makes all the models move
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	public void collisioncheck() {
		//causes ball to bounce of edges
		if(ball.y <= 0) {
			ball.yDirectionSet(-ball.yVelocity);
		}
		if(ball.y >= Game_Height - Ball_Diameter) {
			ball.yDirectionSet(-ball.yVelocity);
		}
		//makes ball bounce off paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++;
			if(ball.yVelocity > 0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.xDirectionSet(ball.xVelocity);
			ball.yDirectionSet(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++;
			if(ball.yVelocity > 0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.xDirectionSet(-ball.xVelocity);
			ball.yDirectionSet(ball.yVelocity);
		}
		//stops paddles from going off screen
		if(paddle1.y <= 0)
			paddle1.y = 0;
		if(paddle1.y >= (Game_Height - Paddle_Height))
			paddle1.y = Game_Height - Paddle_Height;
		
		if(paddle2.y <= 0)
			paddle2.y = 0;
		if(paddle2.y >= (Game_Height - Paddle_Height))
			paddle2.y = Game_Height - Paddle_Height;
		//gives a player 1 point a creates new paddles and a new ball
		if(ball.x <= 0) {
			score.player2++;
			newPaddle();
			newBall();
		}
		if(ball.x >= Game_Width - Ball_Diameter) {
			score.player1++;
			newPaddle();
			newBall();
		}
		//displays win message
		if(score.player1 == 10) {
			frame = new JFrame();
			if(JOptionPane.showConfirmDialog(frame, "Player 1 wins! Time to exit!", null, JOptionPane.CLOSED_OPTION) == JOptionPane.YES_OPTION);
			System.exit(0);
		}
		if(score.player2 == 10) {
			frame = new JFrame();
			if(JOptionPane.showConfirmDialog(frame, "Player 2 wins! Time to exit!", null, JOptionPane.CLOSED_OPTION) == JOptionPane.YES_OPTION);
			System.exit(0);
		}

  }
	public void run() {
		//loops the game
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if(delta >= 1) {
				move();
				collisioncheck();
				repaint();
				delta--;
			}
		}				
	}
	public class ActionListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			//determines what happens when a key is pressed
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			//determines what happens when a key is released
			paddle1.keyReleased(e);
		    paddle2.keyReleased(e);
		}
	}
}

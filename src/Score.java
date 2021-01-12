import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Score extends Rectangle {
	
	static int Game_Width;
	static int Game_Height;
	int player1;
	int player2;

	Score(int Game_Width, int Game_Height){
		Score.Game_Width = Game_Width;
		Score.Game_Height = Game_Height;
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Tahoma", Font.BOLD,60));
		
		g.drawLine(Game_Width/2, 0, Game_Width/2, Game_Height);
		
		g.drawString(String.valueOf(player1/10) + String.valueOf(player1%10), (Game_Width/2) - 85, 50);
		g.drawString(String.valueOf(player2/10) + String.valueOf(player2%10), (Game_Width/2) + 20, 50);
	}
}

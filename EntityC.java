package poop.linus.poopgame.entities.classes;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface EntityC {

  public void tick();
	public void render(Graphics g);
	public Rectangle getBounds();
	
	public double getX();
	public double getY();
	public int getSaturation();
}

package poop.linus.poopgame.entities.classes;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface EntityB {

  public void tick();
	public void render(Graphics g);
	public Rectangle getBounds();
	
	public double getX();
	public double getY();
	
	public void setVelY(double velY);
	public void addVelY(double velY);
	public void OnGround(boolean OnGround);
}

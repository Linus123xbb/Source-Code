package poop.linus.poopgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

import poop.linus.poopgame.IO.Read.ReadDecryptedFile;
import poop.linus.poopgame.IO.Read.ReadTextFile;
import poop.linus.poopgame.IO.Write.WriteDecryptedFile;
import poop.linus.poopgame.audio.Music;
import poop.linus.poopgame.audio.Sound;
import poop.linus.poopgame.entities.Controller;
import poop.linus.poopgame.entities.Poop;
import poop.linus.poopgame.entities.classes.EntityA;
import poop.linus.poopgame.entities.classes.EntityB;
import poop.linus.poopgame.entities.classes.EntityC;
import poop.linus.poopgame.entities.classes.EntityD;
import poop.linus.poopgame.entities.classes.EntityE;
import poop.linus.poopgame.entities.player.Player;
import poop.linus.poopgame.entities.player.Player.pSTATE;
import poop.linus.poopgame.main.gfx.BufferedImageLoader;
import poop.linus.poopgame.main.gfx.Textures;
import poop.linus.poopgame.main.libraries.Animation;
import poop.linus.poopgame.main.menus.DeathMenu;
import poop.linus.poopgame.main.menus.Menu;
import poop.linus.poopgame.main.userInput.KeyInput;
import poop.linus.poopgame.main.userInput.MouseInput;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Poop tha Game";
	public final String Version = " (Pre-Dev-Alpha) 2.9.3 (TESTING)";
	Animation EatAnim;

	public boolean is_shooting;
	private boolean isLooping = false;

	private int enemy;
	public int Animation;
	private int poop = 10;
	Random r = new Random();
	private int dir = r.nextInt(2);

	private int frame;
	private int update;
	private int score = 0;

	private int enemy_killed = 0;
	private int total_enemy_count = 0;

	public static int PlayerDir = 1;

	public int getEnemy_killed() {
		return enemy_killed;
	}

	public void setEnemy_killed(int enemy_killed) {
		this.enemy_killed = enemy_killed;
	}

	private boolean running = false;
	private Thread thread;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage lowHealth = null;
	private BufferedImage deathScreen = null;

	private Player p;
	private Controller c;
	private Textures tex;
	private Menu menu;
	private ReadTextFile rtf;
	private WriteDecryptedFile wdf;
	private ReadDecryptedFile rdf;
	private Sound s;
	private Music m;
	private DeathMenu death_menu;

	public LinkedList<EntityA> ea;
	public LinkedList<EntityB> eb;
	public LinkedList<EntityC> ec;
	public LinkedList<EntityD> ed;
	public LinkedList<EntityE> ee;

	public static enum STATE {
		MENU, GAME, DEATH_MENU
	};

	public static STATE State = STATE.MENU;

	public void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			spriteSheet = loader.loadImage("/gfx/Sprite_Sheet.png");
			lowHealth = loader.loadImage("/gfx/low_health.png");
			deathScreen = loader.loadImage("/gfx/deathScreen.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		tex = new Textures(this);
		rtf = new ReadTextFile();
		wdf = new WriteDecryptedFile();
		rdf = new ReadDecryptedFile();
		s = new Sound(rtf);
		m = new Music(rtf);
		c = new Controller(tex, this, s, rtf);
		p = new Player(200, 200, tex, this, c, s);
		menu = new Menu(c, s, m, rtf);
		death_menu = new DeathMenu(this, wdf, rdf);

		ea = c.getEntityA();
		eb = c.getEntityB();
		ec = c.getEntityC();
		ed = c.getEntityD();
		ee = c.getEntityE();

		this.addKeyListener(new KeyInput(this));
		this.addMouseListener(new MouseInput(menu, death_menu, this));
		System.out.println("HIGH SCORE: "
				+ rdf.readWords("/Files/gameData.dat", 0));

		EatAnim = new Animation(10, tex.Eat[0], tex.Eat[1], tex.Eat[2]);

		if (getDir() == 1)
			c.createEnemy(1, 0, 412 - 32, 1);
		if (getDir() == 0)
			c.createEnemy(1, 580, 412 - 32, 0);
		c.createHotdog(320, 412 + 40);
	}

	private synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop() {
		if (!running)
			return;

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	public void run() {
		requestFocus();
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println(updates + " Ticks, fps " + frames);
				if (p.State == pSTATE.ALIVE && State == STATE.GAME) {
					setScore(getScore() + 10);
				}
				update = updates;
				frame = frames;
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		if (getPoop() <= 0 || p.getHealth() <= 20)
			EatAnim.runAnimation();
		if (p.State == pSTATE.DEAD && State == STATE.GAME) {
			Animation++;
			if (Animation > 100)
				State = STATE.DEATH_MENU;
		}
		if (State == STATE.GAME) {
			int hx = r.nextInt(500) + 100;

			lowHealth();

			p.tick();
			c.tick();

			enemy++;
			if (enemy == 50) {
				dir = r.nextInt(3);
				if (getDir() == 1)
					c.createEnemy(1, 0, 412 - 32, 1);
				if (getDir() == 0)
					c.createEnemy(1, 580, 412 - 32, 0);
				if (getDir() == 2) {
					c.createEnemy(1, 580, 412 - 32, 1);
					c.createEnemy(1, 0, 412 - 32, 0);
				}
				enemy = 0;
			}
			if (r.nextInt(250) == 7) {
				c.createHotdog(hx, 412 + 40);
			}
			if (r.nextInt(400) == 25) {
				c.createHamburger(hx, 412 + 40);
			}
			if (r.nextInt(650) == 50) {
				c.createPizza(hx, 412);
			}
		}
		if (State == STATE.DEATH_MENU)
			death_menu.tick();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		c.render(g);
		if (State == STATE.MENU)
			g.drawImage(deathScreen, 0, 0, null);
		if (State == STATE.GAME || State == STATE.DEATH_MENU) {
			Font font = new Font("Arial", Font.PLAIN, 20);

			g.setColor(Color.MAGENTA);
			g.setFont(font);
			g.drawString("Version: " + Version, 5, 15);
			g.drawString("FPS: " + Integer.toString(frame), 5, 35);
			g.drawString("TICKS: " + Integer.toString(update), 5, 55);
			g.drawString("TOTAL KILLS: " + Integer.toString(enemy_killed), 5,
					75);
			g.drawString(
					"PLAYER POS: X "
							+ (Double.toString(p.getX()) + " ,Y " + (Double
									.toString(p.getY()))), 5, 95);
			g.drawString(
					"Enemies: " + (Integer.toString(getTotal_enemy_count())),
					5, 115);
			g.drawString("Poop: " + (Integer.toString(poop)), 5, 135);
			g.drawString("Health: " + (Double.toString(p.getHealth())), 5, 155);
			g.setColor(Color.GREEN);
			g.drawString("Score: " + (Integer.toString(getScore())), WIDTH
					* SCALE - 150, 15);
			g.setColor(Color.RED);
			g.drawString("Made by Linus and Pladmitry", 5, 465);
			g.setColor(Color.BLACK);
			if (getPoop() <= 0 || p.getHealth() <= 20)
				EatAnim.drawAnimation(g, WIDTH * SCALE / 2 - 64, 35, 0);
			p.render(g);
			if (p.getHealth() <= 20) {
				g.drawImage(lowHealth, 0, 0, null);
			}
		} else if (State == STATE.MENU)
			menu.render(g);
		if (State == STATE.DEATH_MENU) {
			g.drawImage(deathScreen, 0, 0, null);
			death_menu.render(g, score);
		}
		g.dispose();
		bs.show();
	}

	public void lowHealth() {
		if (p.getHealth() <= 20 && !isLooping) {
			// s.start(s.lowHealth);
			isLooping = true;
		} else if (p.getHealth() > 20 && isLooping) {
			// s.stop();
			isLooping = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (State == STATE.GAME && p.State == pSTATE.ALIVE) {
			if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
				// Sound.start(Sound.Step);
				PlayerDir = 1;
				p.setVelX(5);
			} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
				// Sound.start(Sound.Step);
				PlayerDir = 0;
				p.setVelX(-5);
			} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
				if (p.OnGround) {
					s.play(s.Jump);
					p.setVelY(-6);
				}
			} else if (key == KeyEvent.VK_ENTER && !is_shooting && poop > 0) {
				s.play(s.Poop);
				poop = poop - 1;
				is_shooting = true;
				if (PlayerDir == 0)
					c.addEntity(new Poop(p.getX(), p.getY() + 50, tex));
				if (PlayerDir == 1)
					c.addEntity(new Poop(p.getX() + 50, p.getY() + 50, tex));
			} else if (!(poop > 0))
				s.play(s.noPoop);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			// Sound.stop();
			p.setVelX(0);
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			// Sound.stop();
			p.setVelX(0);
		} else if (key == KeyEvent.VK_DOWN) {
			p.setVelY(0);
		} else if (key == KeyEvent.VK_UP) {
			p.setVelY(0);
		} else if (key == KeyEvent.VK_ENTER) {
			is_shooting = false;
		}
	}

	public void mousePressed(MouseEvent e) {
		if (State == STATE.GAME && p.State == pSTATE.ALIVE) {
			if (!is_shooting && poop > 0) {
				s.play(s.Poop);
				poop = poop - 1;
				is_shooting = true;
				if (PlayerDir == 0)
					c.addEntity(new Poop(p.getX(), p.getY() + 50, tex));
				if (PlayerDir == 1)
					c.addEntity(new Poop(p.getX() + 50, p.getY() + 50, tex));
			} else if (!(poop > 0))
				s.play(s.noPoop);
		}
	}

	public void mouseReleased(MouseEvent e) {
		is_shooting = false;
	}

	public static void main(String args[]) {
		Game game = new Game();

		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame f = new JFrame(game.TITLE);
		f.add(game);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		game.start();
	}

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getDir() {
		return dir;
	}

	public void setTotal_enemy_count(int total_enemy_count) {
		this.total_enemy_count = total_enemy_count;
	}

	public int getTotal_enemy_count() {
		return total_enemy_count;
	}

	public void setPoop(int poop) {
		this.poop = poop;
	}

	public int getPoop() {
		return poop;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

}

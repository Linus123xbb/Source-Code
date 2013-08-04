package poop.linus.poopgame.entities;

import java.awt.Graphics;
import java.util.LinkedList;

import poop.linus.poopgame.IO.Read.ReadTextFile;
import poop.linus.poopgame.audio.Sound;
import poop.linus.poopgame.entities.classes.EntityA;
import poop.linus.poopgame.entities.classes.EntityB;
import poop.linus.poopgame.entities.classes.EntityC;
import poop.linus.poopgame.entities.classes.EntityD;
import poop.linus.poopgame.entities.classes.EntityE;
import poop.linus.poopgame.entities.enemies.Zombie;
import poop.linus.poopgame.entities.food.Hamburger;
import poop.linus.poopgame.entities.food.Hotdog;
import poop.linus.poopgame.entities.food.Pizza;
import poop.linus.poopgame.main.Game;
import poop.linus.poopgame.main.gfx.Textures;
import poop.linus.poopgame.main.level.bigNight;
import poop.linus.poopgame.main.level.smallDay;
import poop.linus.poopgame.main.level.smallNight;
import poop.linus.poopgame.main.level.street_bigNight;

public class Controller {

  private LinkedList<EntityA> ea = new LinkedList<EntityA>();
	private LinkedList<EntityB> eb = new LinkedList<EntityB>();
	private LinkedList<EntityC> ec = new LinkedList<EntityC>();
	private LinkedList<EntityD> ed = new LinkedList<EntityD>();
	private LinkedList<EntityE> ee = new LinkedList<EntityE>();

	private EntityA enta;
	private EntityB entb;
	private EntityC entc;
	private EntityD entd;
	private EntityE ente;
	Textures tex;
	Sound s;
	ReadTextFile rtf;
	Game game;

	public Controller(Textures tex, Game game, Sound s, ReadTextFile rtf) {
		this.tex = tex;
		this.game = game;
		this.s = s;
		this.rtf = rtf;

		int lvl = Integer.parseInt(rtf.File("/Files/Settings.txt", 0));
		System.out.println(lvl);
		if(lvl == 0)
			addEntity(new smallDay(0, 412 + 65));
		if(lvl == 1)
			addEntity(new smallNight(0, 412 + 65));
		if(lvl == 2)
			addEntity(new bigNight(0, 412 + 65));
		if(lvl == 3)
			addEntity(new street_bigNight(0, 412 + 65));
	}

	public void createEnemy(int enemy_count, int x, int y, int Dir) {
		for (int i = 0; i < enemy_count; i++) {
			game.setTotal_enemy_count(game.getTotal_enemy_count() + 1);
			addEntity(new Zombie(x, y, tex, game, s, this, Dir));
		}
	}

	public void createHotdog(int x, int y) {
		for (int i = 0; i < 1; i++) {
			addEntity(new Hotdog(x, y, tex));
		}
	}

	public void createHamburger(int x, int y) {
		for (int i = 0; i < 1; i++) {
			addEntity(new Hamburger(x, y, tex));
		}
	}

	public void createPizza(int x, int y) {
		for (int i = 0; i < 1; i++) {
			addEntity(new Pizza(x, y, tex));
		}
	}

	public void tick() {
		// E CLASS
		for (int i = 0; i < ee.size(); i++) {
			ente = ee.get(i);

			ente.tick();
		}
		// A CLASS
		for (int i = 0; i < ea.size(); i++) {
			enta = ea.get(i);

			enta.tick();

			if (enta.getX() >= 620 || enta.getX() <= -3 || enta.getY() >= 460) {
				removeEntity(enta);
			}
		}

		// B CLASS
		for (int i = 0; i < eb.size(); i++) {
			setEntb(eb.get(i));

			getEntb().tick();
		}

		// C CLASS
		for (int i = 0; i < ec.size(); i++) {
			setEntc(ec.get(i));

			getEntc().tick();
		}
		// D CLASS
		for (int i = 0; i < ed.size(); i++) {
			entd = ed.get(i);

			entd.tick();
		}
	}

	public void render(Graphics g) {
		// E CLASS
		for (int i = 0; i < ee.size(); i++) {
			ente = ee.get(i);

			ente.render(g);
		}
		// A CLASS
		for (int i = 0; i < ea.size(); i++) {
			enta = ea.get(i);

			enta.render(g);
		}
		// B CLASS
		for (int i = 0; i < eb.size(); i++) {
			setEntb(eb.get(i));

			getEntb().render(g);
		}
		// C CLASS
		for (int i = 0; i < ec.size(); i++) {
			setEntc(ec.get(i));

			getEntc().render(g);
		}
		// D CLASS
		for (int i = 0; i < ed.size(); i++) {
			entd = ed.get(i);

			entd.render(g);
		}
	}

	public void addEntity(EntityA block) {
		ea.add(block);
	}

	public void removeEntity(EntityA block) {
		ea.remove(block);
	}

	public void addEntity(EntityB block) {
		eb.add(block);
	}

	public void removeEntity(EntityB block) {
		eb.remove(block);
	}

	public void addEntity(EntityC block) {
		ec.add(block);
	}

	public void removeEntity(EntityC block) {
		ec.remove(block);
	}

	public void addEntity(EntityD block) {
		ed.add(block);
	}

	public void removeEntity(EntityD block) {
		ed.remove(block);
	}

	public void addEntity(EntityE block) {
		ee.add(block);
	}

	public void removeEntity(EntityE block) {
		ee.remove(block);
	}

	public LinkedList<EntityA> getEntityA() {
		return ea;
	}

	public LinkedList<EntityB> getEntityB() {
		return eb;
	}

	public LinkedList<EntityC> getEntityC() {
		return ec;
	}

	public LinkedList<EntityD> getEntityD() {
		return ed;
	}

	public LinkedList<EntityE> getEntityE() {
		return ee;
	}

	public void setEntc(EntityC entc) {
		this.entc = entc;
	}

	public EntityC getEntc() {
		return entc;
	}

	public void setEntb(EntityB entb) {
		this.entb = entb;
	}

	public EntityB getEntb() {
		return entb;
	}

	public EntityE getEnte() {
		return ente;
	}
}

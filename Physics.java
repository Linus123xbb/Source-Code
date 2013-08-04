package poop.linus.poopgame.entities;

import java.util.LinkedList;

import poop.linus.poopgame.entities.classes.EntityA;
import poop.linus.poopgame.entities.classes.EntityB;
import poop.linus.poopgame.entities.classes.EntityC;
import poop.linus.poopgame.entities.classes.EntityD;
import poop.linus.poopgame.entities.classes.EntityE;

public class Physics {
  public static boolean Collision(EntityA enta, LinkedList<EntityC> entc) {

		for (int i = 0; i < entc.size(); i++) {

			if (enta.getBounds().intersects(entc.get(i).getBounds())) {
				return true;
			}
		}

		return false;
	}

	public static boolean playerCollision(EntityA enta, LinkedList<EntityB> entb) {

		for (int i = 0; i < entb.size(); i++) {

			if (enta.getBounds().intersects(entb.get(i).getBounds())) {
				return true;
			}
		}

		return false;
	}

	public static boolean Collision(EntityB entb, LinkedList<EntityD> entd) {

		for (int i = 0; i < entd.size(); i++) {

			if (entb.getBounds().intersects(entd.get(i).getBounds())) {
				return true;
			}
		}

		return false;
	}

	public static boolean Collision(EntityC entc, LinkedList<EntityA> enta) {

		for (int i = 0; i < enta.size(); i++) {

			if (entc.getBounds().intersects(enta.get(i).getBounds())) {
				return true;
			}
		}

		return false;
	}

	public static boolean Collision(EntityD entd, LinkedList<EntityB> entb) {

		for (int i = 0; i < entb.size(); i++) {

			if (entd.getBounds().intersects(entb.get(i).getBounds())) {
				return true;
			}
		}

		return false;
	}

	public static boolean Gravity(EntityA enta, LinkedList<EntityE> ente) {

		for (int i = 0; i < ente.size(); i++) {

			if (enta.getBounds().intersects(ente.get(i).getBounds())) {
				return true;
			}
		}
		return false;
	}

	public static boolean Gravity(EntityB entb, LinkedList<EntityE> ente) {

		for (int i = 0; i < ente.size(); i++) {

			if (entb.getBounds().intersects(ente.get(i).getBounds())) {
				return true;
			}
		}
		return false;
	}
}

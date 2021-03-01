package priv.sandbox.game.entities;

import priv.dremma.game.entities.Entity;

public class Weapon extends Entity {
	public int attackHarm; // 攻击造成的伤害
	public float attackDistance; // 攻击动作插值距离
	
	public Weapon(String name, int attackHarm, float attackDistance) {
		this.name = name;
		this.attackHarm = attackHarm;
		this.attackDistance = attackDistance;
	}
}

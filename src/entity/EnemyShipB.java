package entity;

import engine.Cooldown;
import engine.DrawManager;
import engine.GameState;

import java.util.Set;

public class EnemyShipB extends EnemyShip {
    /** HP's magnification */
    private final double HPPOWER = .4;
    /** the velocity magnification of a bullet */
    private final double BULLETSPEEDPOWER = 1;
    /** Shooting cool down magnification */
    private final double BULLETCOOLDOWN = 0.2;
    /** Scores that go up when you remove them */
    private final int POINT = 10;
    public EnemyShipB(final int positionX, final int positionY,
                      final DrawManager.SpriteType spriteType, final GameState gameState) {
        super(positionX, positionY, spriteType, gameState);
        super.HP = (int)(super.HP * HPPOWER);
        super.pointValue = POINT;
    }

    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();
            if (spriteType == DrawManager.SpriteType.EnemyShipB1)
                spriteType = DrawManager.SpriteType.EnemyShipB2;
            else
                spriteType = DrawManager.SpriteType.EnemyShipB1;
        }
    }
    public final void shoot(final Set<Bullet> bullets, Cooldown shootingCooldown) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, (int)(super.BULLET_SPEED * BULLETSPEEDPOWER),0 , DrawManager.SpriteType.EnemyBullet));
        shootingCooldown.timedown(BULLETCOOLDOWN);
    }

}

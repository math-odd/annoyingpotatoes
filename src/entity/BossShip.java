package entity;

import engine.DrawManager;
import engine.GameState;
import engine.SoundManager;

import java.util.List;
import java.util.Set;


public class BossShip extends EnemyShip {
    private final int SpeedY = 2;
    private final double PI = Math.acos(-1);
    /** Width of current screen. */
    private static final int WIDTH = 448;
    /** Height of current screen. */
    private static final int HEIGHT = 520;
    /** Width of boss ship. */
    private static final int BOSS_WIDTH = 30;
    /** Height of boss ship. */
    private static final int BOSS_HEIGHT = 20;
    private int splitLevel;
    private int TARX = this.positionX;
    private int TARY = this.positionY;
    private int Rotate, MoveType,Radius;
    private int summonTime;
    private int sp_flag;
    public BossShip (final int positionX, final int positionY,
                     final DrawManager.SpriteType spriteType, final GameState gameState, int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        this.width = BOSS_WIDTH * 2;
        this.height = BOSS_HEIGHT * 2;
        super.HP = splitLevel;//따로 수정;
        super.pointValue = 100*splitLevel; //따로수정
        this.splitLevel = splitLevel;
        this.spriteType = spriteType.BossShip;
        MoveType = -1;Rotate=0;Radius=0;summonTime=0;
        this.sp_flag = 0;
    }
    private void summon(List<EnemyShip> enemyShipList){//enemyships.get(1) is Boss stage's small enemy
        if (summonTime>=1) {
            summonTime=0;
            int rand = (int)(Math.random()*3);
            EnemyShip enemyShip;
            switch (rand){
                case 0:
                    enemyShip = new EnemyShipA(0,0, DrawManager.SpriteType.EnemyShipA1,gameState); break;
                case 1:
                    enemyShip = new EnemyShipB(0,0, DrawManager.SpriteType.EnemyShipB1,gameState); break;
                default:
                    enemyShip = new EnemyShipC(0,0, DrawManager.SpriteType.EnemyShipC1,gameState); break;
            }
            scatter(enemyShip);
            enemyShipList.add(enemyShip);
            return;
        }
        summonTime++;
    }
    /**
     * when slime Boss dead this function
     */
    public void split(List<EnemyShip> enemyShipList) {
        int currentX = this.positionX, currentY = this.positionY;
        if (splitLevel <= 0) return;

        // Adjust the starting position based on BossShip width
        if (sp_flag == 0)
            currentX += BOSS_WIDTH * 2;
        else
            currentX -= BOSS_WIDTH * 2;
        if (currentX < BOSS_WIDTH) {
            currentX += BOSS_WIDTH; sp_flag = 0;}
        if (WIDTH - BOSS_WIDTH < currentX) {
            currentX -= BOSS_WIDTH; sp_flag = 1;}
        // Calculate the positions for the split BossShips
        int firstX = currentX - BOSS_WIDTH, secondX = currentX + BOSS_WIDTH;

        // Ensure the BossShips do not overlap
        if (firstX < 0) {
            firstX = 0;
            secondX = BOSS_WIDTH * 2;
        } else if (WIDTH - BOSS_WIDTH < secondX) {
            secondX = WIDTH - BOSS_WIDTH;
            firstX = secondX - BOSS_WIDTH * 2;
        }

        // Create the split BossShips
        BossShip first = new BossShip(firstX, currentY, DrawManager.SpriteType.BossShip, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(secondX, currentY, DrawManager.SpriteType.BossShip, this.gameState, this.splitLevel - 1);

        // Add the BossShips to the list
        enemyShipList.add(first);
        enemyShipList.add(second);
    }

    /**
     * when Boss uses beam pattern
     */
    public void beam(final Set<LaserBeam> laserBeams) {
        int randomX = (int)(Math.random() * 448);
        laserBeams.add(new LaserBeam(randomX, 44));
    }

    /**
     *  when Boss shoot big bullet
     */
    public void shootBigBullet(final Set<Bullet> bullets) {
        bullets.add(BulletPool.getBullet(positionX
                + width / 2, positionY, BULLET_SPEED, 0, DrawManager.SpriteType.BiggerEnemyBullet));
        SoundManager.playSound("SFX/S_Enemy_Shoot", "EnemyShoot", false, false);
    }

    /**
     * when Boss Die this function execute
     */
    public void Death(List<EnemyShip> enemyShipList){
        split(enemyShipList);
    }

    /**
     * when Boss attack this function execute
     * There is only one attack pattern yet
     */
    public void Attack(final Set<LaserBeam> laserBeams,
                       List<EnemyShip> enemyShipList,
                       final Set<Bullet> bullets) {
        int patternNum = (int)(Math.random() * 3);
        switch (patternNum) {
            case 0:
                for (int i = 0; i < gameState.getLevel(); i++)
                    beam(laserBeams);
                break;
            case 1:
                summon(enemyShipList);
                break;
            case 2:
                shootBigBullet(bullets);
            }
        }

    public void Move(){
        if (this.HP >= 0) {
            if (MoveType==-1){
                this.Radius = moveTrackSize(TARX, TARY);
                moveTeleport();
                MoveType = (int)(Math.random()*3);
                if (MoveType==3)MoveType=2;
                Rotate=0;
            }
            switch (MoveType) {
                case 0:
                    moveCircle(); break;
                case 1:
                    moveDiamond();break;
                case 2:
                    moveCross();break;
            }
        }
    }

    /**
     * tell the direction of boss ship in perspective of gamer
     * @return true if turn right
     */
    public boolean isRight(){
        double dValue = Math.random();
        int lr = (int)(dValue * 2);
        if (lr != 0){return true;} // right
        else {return false;} // left
    }
    private void scatter(EnemyShip target){
        target.setPositionX((int)((WIDTH-BOSS_WIDTH)*Math.random()));
        target.setPositionY((int)(HEIGHT*0.6*Math.random())+52);
    }
    public int moveTrackSize(int nowShipX, int nowShipY){
        double dValue = Math.random();
        int minimX = Math.min((WIDTH - nowShipX - BOSS_WIDTH*2), nowShipX);
        return (int)(dValue * Math.min(minimX, (HEIGHT - nowShipY - BOSS_HEIGHT*2)));
    }
    /**
     * move along the circle track
     */
    public void moveCircle() {
        if (Rotate>=36){
            Rotate=0;
            MoveType=-1;
            return;
        }

        this.setPositionX((int)(TARX + Radius * Math.sin(Rotate*10/180.0*PI)));
        this.setPositionY((int)(TARY + Radius * Math.cos(Rotate*10/180.0*PI)));
        Rotate++;
    }

    /**
     * move along the cross track
     */
    public void moveCross() {
        int r = moveTrackSize(positionX, positionY);
        if (r <= 0){moveTeleport();}
        else {
            int forward;
            if(isRight()){forward = 1;}
            else {forward = -1;}
            int i;
            for (i = 1; i < r/10; i++){this.setPositionX(positionX + 10);}
            for (i = 1; i < r/10; i++){this.setPositionX(positionX - 10);}
            for (i = 1; i < r/10; i++){this.setPositionY(positionY + forward*10);}
            for (i = 1; i < r/5 - 1; i++){this.setPositionY(positionY - forward*10);}
            for (i = 1; i < r/10; i++){this.setPositionY(positionY + forward*10);}
        }
    }

    /**
     * move along the diamond track
     */
    public void moveDiamond() {
        if (Rotate>=36){
            Rotate=0;
            MoveType=-1;
            return;
        }
        int Radius = this.Radius/9;
        if (Rotate<9){
            this.setPositionX(TARX+(Rotate-9)*Radius);
            this.setPositionY(TARY+Rotate*Radius);
        }
        else if (Rotate<18){
            int Rotate=this.Rotate%9;
            this.setPositionX(TARX+Rotate*Radius);
            this.setPositionY(TARY+(9-Rotate)*Radius);
        }
        else if (Rotate<27){
            int Rotate=this.Rotate%9;
            this.setPositionX(TARX+(9-Rotate)*Radius);
            this.setPositionY(TARY-Rotate*Radius);
        }
        else{
            int Rotate=this.Rotate%9;
            this.setPositionX(TARX-Rotate*Radius);
            this.setPositionY(TARY+(Rotate-9)*Radius);
        }
        Rotate++;
    }

    /**
     * Teleport randomly
     */
    public void moveTeleport() {
        double randomX = Math.random();
        double randomY = Math.random();
        positionX = (int) (randomX * (WIDTH - BOSS_WIDTH));
        positionY = (int) (randomY * (HEIGHT*0.6 - BOSS_HEIGHT)+300);
    }
    public int getSplitLevel(){return this.splitLevel;}

    public void destroy() {
        this.HP--;
        if (this.HP <= 0) {
            SoundManager.playSound("SFX/S_Enemy_Destroy_a", "Enemy_destroyed", false, false);
            this.isDestroyed = true;
            this.spriteType = DrawManager.SpriteType.BossShipDestroyed; // 스프라이트를 BossShipDestroy로 변경
        }
    }
}
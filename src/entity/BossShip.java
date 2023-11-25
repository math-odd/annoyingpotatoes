package entity;

import engine.DrawManager;
import engine.GameState;

import java.util.List;


public class BossShip extends EnemyShip {
    private final int SpeedY = -2;
    /** Width of current screen. */
    private static final int WIDTH = 448;
    /** Height of current screen. */
    private static final int HEIGHT = 520;
    /** Width of boss ship. */
    private static final int BOSS_WIDTH = 50;
    /** Height of boss ship. */
    private static final int BOSS_HEIGHT = 30;
    private int splitLevel;

    public BossShip (final int positionX, final int positionY,
                     final DrawManager.SpriteType spriteType, final GameState gameState, int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        super.HP = splitLevel;//따로 수정;
        super.pointValue = 100*splitLevel; //따로수정
        this.splitLevel = splitLevel;
    }

    /**
     * when slime Boss dead this function
     */
    public void split(List<EnemyShip> enemyShipList) {
        int currentX = this.positionX, currentY = this.positionY;
        if (splitLevel <= 0) return;
        currentX += 10;
        if (currentX < 20) currentX += 50;
        if (WIDTH - 20 < currentX) currentX -= 50;
        int firstX = currentX - 20, secondX = currentX + 20;
        BossShip first = new BossShip(firstX, currentY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(secondX, currentY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        enemyShipList.add(first);
        enemyShipList.add(second);
    }
    /**
     * when Boss Die this function execute
     */
    public void Death(List<EnemyShip> enemyShipList){
        split(enemyShipList);
    }

    /**
     * when Boss attack this function execute
     */
    public void Attack() {

    }

    /**
     * when Boss attack this function execute
     */
    public void Move(){

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

    public int moveTrack(int nowShipX, int nowShipY){
        double dValue = Math.random();
        return (int)(dValue * Math.min((WIDTH - nowShipX), (HEIGHT - nowShipY)));
    }

    /**
     * move along the circle track
     */
    public void moveCircle() {
        if(isRight()){}
        else {}
    }

    /**
     * move along the cross track
     */
    public void moveCross() {
        if(isRight()){}
        else {}
    }

    /**
     * move along the diamond track
     */
    public void moveDiamond() {
        if(isRight()){}
        else {}
    }

    /**
     * Teleport randomly
     */
    public void moveTeleport() {
    }
}

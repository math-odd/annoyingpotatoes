package entity;

import engine.DrawManager;
import engine.GameState;

import java.util.ArrayList;
import java.util.List;

public class BossShip extends EnemyShip {
    private List<BossShip> SplitList;
    private final int SpeedX = -2;
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
                     final DrawManager.SpriteType spriteType, final GameState gameState, final int splitLevel){
        super(positionX, positionY, spriteType, gameState);
        super.HP = 0;//따로 수정;
        super.pointValue = 0; //따로수정
        this.splitLevel = splitLevel;
        SplitList = new ArrayList<>();
        SplitList.add(this);
    }

    /**
     * when slime Boss dead this function
     */
    public void split() {
        int currentX = this.positionX, currentY = this.positionY;
        if (this.splitLevel <= 0) return;
        currentX += 10;
        if (currentY < 20) currentY += 50;
        if (WIDTH - 20 < currentY) currentY -= 50;
        int firstY = currentY - 20, secondY = currentY + 20;
        BossShip first = new BossShip(currentX, firstY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        BossShip second = new BossShip(currentX, secondY, DrawManager.SpriteType.EnemyShipA1, this.gameState, this.splitLevel - 1);
        SplitList.add(first);
        SplitList.add(second);
    }
    /**
     * when Boss Die this function execute
     */
    public void Death(){

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

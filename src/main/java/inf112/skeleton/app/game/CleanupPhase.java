package inf112.skeleton.app.game;

import inf112.skeleton.app.objects.Actors.Robot;
import inf112.skeleton.app.objects.TileObjects.RepairSite;
import inf112.skeleton.app.screens.cardsUI.CardUI;

import static inf112.skeleton.app.game.MainGame.gameBoard;
import static inf112.skeleton.app.game.MainGame.robots;

public class CleanupPhase {


    public void run(MainGame mainGame, CardUI cardUI) {
        repairs();
        mainGame.startGameRound(cardUI);
    }

    private void repairs() {
        for (Robot robot : robots) {
            if (gameBoard.isPosARepaiSite(robot.getPosition())) {
                RepairSite repair = (RepairSite) gameBoard.getNonWallTileOnPos(robot.getPosition());

                robot.getProgramSheet().setArchiveMarker(repair.getPosition());
                robot.getProgramSheet().addDamage(-1); //Repair 1 damageToken

                //If robot at double repairSite. Give 1 option card. Imlpement under. This is not MVP.
                //if (repair.getStrength() == 2) robot.getProgramSheet().give1optionCard
            }
        }
    }


    /*
    Clean up plan:
        - Repairs & Upgrades
            - Robots at single-wrench space, discard 1 damage token(heal 1). Robots at two-wrench space, discard 1 damage token token and draw 1 Options card.
     */

}

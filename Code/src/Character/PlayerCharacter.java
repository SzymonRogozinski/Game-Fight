package Character;

import Fight.ActionItem;

import java.util.ArrayList;

public class PlayerCharacter extends GameCharacter{

    private static final int MAX_ACTION_ITEM=3;
    private final ArrayList<ActionItem> actionItems;

    public PlayerCharacter(int startStrength, int startEndurance, int startIntelligence, int startCharisma, int startCunning, int startLuck,ArrayList<ActionItem> actionItems) {
        super(startStrength, startEndurance, startIntelligence, startCharisma, startCunning, startLuck);
        this.actionItems = actionItems;
    }

    public int getDiceNumber(int attribute){
        return attribute/6+1;
    }

    public int getCharacterRerolls(){
        return getLuck()/10;
    }

    public ArrayList<ActionItem> getActionItems() {
        return actionItems;
    }

}
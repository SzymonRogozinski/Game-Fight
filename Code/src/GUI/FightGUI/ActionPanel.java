package GUI.FightGUI;

import GUI.GUISettings;
import Fight.FightModule;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class ActionPanel extends JPanel {

    private FightModule fight;
    private CardLayout layout;
    private ActionListPanel actions;
    private DicePanel dice;
    private JPanel pauseScreen,enemyScreen;

    public ActionPanel(Border border){
        //Set display
        this.setSize(GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
        this.layout=new CardLayout();
        this.setLayout(layout);
        this.setBackground(Color.BLACK);

        //Card components
        actions=new ActionListPanel(border);
        dice =new DicePanel(border);
        //Pause Screen
        pauseScreen=new StopPanel(e->roll(),"roll");
        enemyScreen=new StopPanel(e->enemy(),"Enemy attack");

        this.add("Actions",actions);
        this.add("Pause",pauseScreen);
        this.add("Enemy",enemyScreen);
        this.add("Dice", dice);
    }

    public void setFight(FightModule fight){
        this.fight=fight;
        dice.setFight(fight);
        actions.setFight(fight);
    }

    public DicePanel getDicePanel(){return dice;}

    public void changePage(String pageName){layout.show(this,pageName);}

    public ActionListPanel getActions() {
        return actions;
    }

    private void roll(){
        if(fight.isNoRoll()){
            fight.endAction();
            return;
        }
        changePage("Dice");
        fight.rollDices();
    }

    private void enemy(){
        fight.endAction();
    }

    private class StopPanel extends JPanel{

        StopPanel(ActionListener l,String buttonName){
            this.setSize(GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
            this.setLayout(new GridLayout(3,1));
            this.setBackground(Color.BLACK);
            JButton pauseButton = new JButton(buttonName);
            pauseButton.addActionListener(l);
            this.add(new JLabel());
            this.add(pauseButton);
        }
    }
}

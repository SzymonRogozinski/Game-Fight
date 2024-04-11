package GUI;

import main.DiceMaster;
import main.FightModule;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MainPanel extends JPanel {

    private DicePanel dicePanel;
    private RollPanel rollPanel;
    private ActionPanel actionPanel;

    public MainPanel( ){
        //Setting panel
        this.setLayout(null);
        this.setPreferredSize(new Dimension(GUISettings.heightAndWidth,GUISettings.heightAndWidth));
        this.setBackground(Color.BLACK);

        //Panel components
        //Big four
        JPanel bigPanel=new JPanel();
        JPanel smallPanel = new JPanel();
        JPanel leftInfoPanel = new JPanel();
        JPanel downInfoPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
        //Real components
        dicePanel=new DicePanel(border);
        rollPanel =new RollPanel(border);
        actionPanel = new ActionPanel(border);

        //Settings components
        bigPanel.setBounds(0,0, GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE);
        bigPanel.setLayout(null);
        bigPanel.setBackground(Color.BLACK);
        bigPanel.setBorder(border);

        smallPanel.setBounds(GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
        smallPanel.setLayout(null);
        smallPanel.setBackground(Color.BLACK);
        smallPanel.setBorder(border);
        smallPanel.add(rollPanel);

        leftInfoPanel.setBounds(GUISettings.PANEL_SIZE,0,GUISettings.SMALL_PANEL_SIZE,GUISettings.PANEL_SIZE);
        leftInfoPanel.setLayout(null);
        leftInfoPanel.setBackground(Color.BLACK);
        leftInfoPanel.setBorder(border);
        leftInfoPanel.add(actionPanel);

        downInfoPanel.setBounds(0,GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
        downInfoPanel.setLayout(null);
        downInfoPanel.setBackground(Color.BLACK);
        downInfoPanel.setBorder(border);
        downInfoPanel.add(dicePanel);

        //Adding component to panel
        this.add(bigPanel);
        this.add(smallPanel);
        this.add(leftInfoPanel);
        this.add(downInfoPanel);

        //Refresh
        this.revalidate();
        this.repaint();
    }

    public void setDiceMaster(DiceMaster master){
        dicePanel.setMaster(master);
        rollPanel.setMaster(master);
    }

    public void setFightModule(FightModule fightModule){
        actionPanel.setFight(fightModule);
    }

    public DicePanel getDicePanel() {
        return dicePanel;
    }

    public RollPanel getRollPanel() {
        return rollPanel;
    }
}
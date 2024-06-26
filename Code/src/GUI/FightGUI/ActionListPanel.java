package GUI.FightGUI;

import Fight.GameActions.ActionItem;
import Fight.GameActions.SpellAction;
import Fight.GameActions.UsableItemAction;
import GUI.GUISettings;
import Fight.FightModule;
import Character.PlayerCharacter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class ActionListPanel extends JPanel {

    //Sizes and placement of button
    private final static int buttonWidth=GUISettings.SMALL_PANEL_SIZE*2/3;
    private final static int buttonHeight=GUISettings.SMALL_PANEL_SIZE/3;
    private final static int buttonHGap =GUISettings.PANEL_SIZE/5-buttonWidth*2/3;
    private final static int buttonVGap =(GUISettings.SMALL_PANEL_SIZE-buttonHeight)/2;
    private FightModule fight;
    private CardLayout layout;
    private CardPanel startPanel, fightPanel,magicPanel,itemPanel;

    public ActionListPanel(Border border){
        //Set display
        this.setSize(GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
        this.layout=new CardLayout();
        this.setLayout(layout);
        this.setBorder(border);
        this.setBackground(Color.BLACK);

        ArrayList<JButton> actionButtons1=new ArrayList<>();
        //Setting buttons
        String[] names={"Attack","Items","Spells"};
        for(int i=0;i<3;i++){
            JButton action=new JButton(names[i]);
            action.setSize(buttonWidth,buttonHeight);
            actionButtons1.add(action);
        }

        actionButtons1.get(0).addActionListener(e->changePage("Fight"));
        actionButtons1.get(1).addActionListener(e->changePage("Items"));
        actionButtons1.get(2).addActionListener(e->changePage("Magic"));

        startPanel=new CardPanel(border,actionButtons1);
        fightPanel=new CardPanel(border,new ArrayList<>(),"Start");
        magicPanel=new CardPanel(border,new ArrayList<>(),"Start");
        itemPanel=new CardPanel(border,new ArrayList<>(),"Start");

        this.add("Start",startPanel);
        this.add("Items",itemPanel);
        this.add("Fight",fightPanel);
        this.add("Magic",magicPanel);
    }

    public void setFight(FightModule fight){
        this.fight=fight;
    }

    public void loadAction(){
        if(!(fight.getCharacter() instanceof PlayerCharacter))
            throw new RuntimeException("Illegal state, enemy and player character were mixed!");
        PlayerCharacter character=(PlayerCharacter) fight.getCharacter();
        //items
        ArrayList<ActionItem> items = character.getActionItems();
        ArrayList<JButton> buttons=new ArrayList<>();
        for(ActionItem item:items){
            JButton button=new JButton(item.getName());
            button.setSize(buttonWidth,buttonHeight);
            button.addActionListener(e-> {
                fight.choosedAction(item);
                changePage("Start");
            });
            buttons.add(button);
        }
        fightPanel.loadNewAction(buttons);

        //usable items
        ArrayList<UsableItemAction> usableItems = character.getUsableItems();
        buttons=new ArrayList<>();

        for(UsableItemAction item:usableItems){
            JButton button=new JButton(item.getName());
            button.setSize(buttonWidth,buttonHeight);
            button.addActionListener(e-> {
                fight.choosedAction(item);
                changePage("Start");
            });
            buttons.add(button);
        }
        itemPanel.loadNewAction(buttons);

        //spells
        ArrayList<SpellAction> spellActions = character.getSpells();
        buttons=new ArrayList<>();
        //Mana
        FlowLayout flowLayout=new FlowLayout();
        flowLayout.setHgap(0);
        flowLayout.setVgap(0);
        for(SpellAction spell:spellActions){
            //TextAreas
            JLabel t = new JLabel(spell.getName());
            t.setFont(GUISettings.BUTTON_FONT);
            t.setBackground(new Color(0,0,0,0));

            JLabel tt = new JLabel(" "+spell.getManaCost());
            tt.setFont(GUISettings.BUTTON_FONT);
            tt.setBackground(new Color(0,0,0,0));
            tt.setForeground(Color.BLUE);

            //Button setup
            JButton button=new JButton();
            button.setLayout(flowLayout);
            button.add(t);
            button.add(tt);

            button.setSize(buttonWidth,buttonHeight);
            button.addActionListener(e-> {
                if(fight.getParty().getCurrentMana()<spell.getManaCost()){
                    System.out.println("You don't have enough mana!");
                }
                else {
                    fight.getParty().spendMana(spell.getManaCost());
                    fight.choosedAction(spell);
                    changePage("Start");
                }
            });
            buttons.add(button);
        }
        magicPanel.loadNewAction(buttons);
    }

    private void changePage(String pageName){
        layout.show(this,pageName);
    }

    private class CardPanel extends JPanel{
        final ArrayList<JButton> buttons;
        private JButton goBackButton;
        CardPanel(Border border, ArrayList<JButton> buttons){
            this.buttons=buttons;
            //Set display
            this.setSize(GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
            FlowLayout layout=new FlowLayout();
            layout.setHgap(buttonHGap);
            layout.setVgap(buttonVGap);
            this.setLayout(layout);
            this.setBorder(border);
            this.setBackground(Color.BLACK);

            for(JButton button:buttons){
                this.add(button);
            }
        }

        CardPanel(Border border, ArrayList<JButton> buttons,String goBackName){
            this.buttons=buttons;
            //Set display
            this.setSize(GUISettings.PANEL_SIZE,GUISettings.SMALL_PANEL_SIZE);
            FlowLayout layout=new FlowLayout();
            layout.setHgap(buttonHGap);
            layout.setVgap(buttonVGap);
            this.setLayout(layout);
            this.setBorder(border);
            this.setBackground(Color.BLACK);

            for(JButton button:buttons){
                this.add(button);
            }

            JButton goBackButton=new JButton("Go back");
            goBackButton.setSize(buttonWidth,buttonHeight);
            goBackButton.addActionListener(e->changePage(goBackName));
            this.goBackButton = goBackButton;
            this.add(goBackButton);
        }

        void loadNewAction(ArrayList<JButton> buttons){
            this.removeAll();

            for(JButton button:buttons){
                this.add(button);
            }
            this.add(goBackButton);
        }
    }

}

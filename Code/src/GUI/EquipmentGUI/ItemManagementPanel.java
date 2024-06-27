package GUI.EquipmentGUI;

import Equipment.CharacterEquipment;
import Equipment.EquipmentModule;
import Equipment.Items.Item;
import GUI.GUISettings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManagementPanel extends JPanel {

    private static ImageIcon HELM_SLOT_ICON =new ImageIcon("ItemsIcons/slot-helm.png");
    private static ImageIcon CHEST_SLOT_ICON =new ImageIcon("ItemsIcons/slot-chest.png");
    private static ImageIcon GAUNTLET_SLOT_ICON =new ImageIcon("ItemsIcons/slot-gauntlet.png");
    private static ImageIcon LEG_SLOT_ICON =new ImageIcon("ItemsIcons/slot-leg.png");
    private static ImageIcon SCROLL_SLOT_ICON =new ImageIcon("ItemsIcons/slot-scroll.png");
    private static ImageIcon DICE_SLOT_ICON =new ImageIcon("ItemsIcons/slot-dice.png");
    private static ImageIcon BAG_SLOT_ICON =new ImageIcon("ItemsIcons/slot-bag.png");

    private BackpackPanel backpackPanel;
    private EquipmentPanel equipmentPanel;
    private CardLayout layout;
    private EquipmentModule equipment;

    public ItemManagementPanel(Border border) {
        this.setSize(GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE);
        layout=new CardLayout();
        this.setLayout(layout);
        this.setBorder(border);

        backpackPanel=new BackpackPanel();
        equipmentPanel=new EquipmentPanel();

        this.add(backpackPanel,"Backpack");
        this.add(equipmentPanel,"Equipment");

        layout.show(this,"Equipment");
    }

    public void changeCard(String name){
        layout.show(this,name);
    }

    public void setEquipment(EquipmentModule equipment) {
        this.equipment = equipment;
    }

    public void refresh(){
        if(equipment==null)
            return;
        backpackPanel.refresh();
        equipmentPanel.refresh();
    }

    private class EquipmentPanel extends JPanel{

        private JLabel title;
        private ItemSlotRow armor,items,spells;
        private SmallBackpackItemsPanel smallBackpackItemsPanel;

        public EquipmentPanel() {
            this.setSize(GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE);
            FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
            layout.setVgap(5);
            this.setLayout(layout);
            this.setBackground(Color.BLACK);

            title = new JLabel("Equipment", SwingConstants.CENTER);
            title.setPreferredSize(new Dimension((int)(GUISettings.PANEL_SIZE*0.8),(int)(GUISettings.PANEL_SIZE/7.5)));
            title.setFont(GUISettings.BIG_FONT);
            title.setForeground(Color.WHITE);

            armor=new ItemSlotRow("Armor",4,new ImageIcon[]{HELM_SLOT_ICON,GAUNTLET_SLOT_ICON,CHEST_SLOT_ICON,LEG_SLOT_ICON},CharacterEquipment.ARMOR_SLOT);
            items=new ItemSlotRow("Items",3,new ImageIcon[]{DICE_SLOT_ICON,DICE_SLOT_ICON,DICE_SLOT_ICON},CharacterEquipment.ACTION_SLOT);
            spells=new ItemSlotRow("spells",3,new ImageIcon[]{SCROLL_SLOT_ICON,SCROLL_SLOT_ICON,SCROLL_SLOT_ICON},CharacterEquipment.SPELL_SLOT);

            smallBackpackItemsPanel=new SmallBackpackItemsPanel();

            this.add(title);
            this.add(armor);
            this.add(items);
            this.add(spells);
            this.add(smallBackpackItemsPanel);
        }

        void refresh(){
            armor.refresh();
            items.refresh();
            spells.refresh();
        }

    }

    private class ItemSlotRow extends JPanel{

        private ItemSlot[] itemSlots;
        private final int slotType;

        public ItemSlotRow(String name, int slotNumber,ImageIcon[] emptySlotIcons,int slotType) {
            this.setPreferredSize(new Dimension((int)(GUISettings.PANEL_SIZE*0.8),(int)(GUISettings.PANEL_SIZE/6)));
            FlowLayout layout=new FlowLayout(FlowLayout.LEFT);
            layout.setHgap(10);
            layout.setVgap(0);
            this.setLayout(layout);
            this.setBackground(Color.BLACK);

            JLabel title =new JLabel(name, SwingConstants.CENTER);
            title.setForeground(Color.WHITE);
            this.add(title);

            itemSlots=new ItemSlot[slotNumber];
            this.slotType=slotType;

            for(int i=0;i<slotNumber;i++){
                itemSlots[i]=new ItemSlot(null,emptySlotIcons[i]);
                this.add(itemSlots[i]);
            }
        }

        void refresh(){
            ArrayList<Item> items;

            if(slotType == CharacterEquipment.ACTION_SLOT){
                items=castArray(equipment.getCurrentCharacter().getEquipment().getActionItems());
            }else if(slotType == CharacterEquipment.SPELL_SLOT){
                items=castArray(equipment.getCurrentCharacter().getEquipment().getSpellItems());
            }else{
                items=castArray(equipment.getCurrentCharacter().getEquipment().getArmorItems());
            }
            for(int i=0;i<itemSlots.length;i++){
                itemSlots[i].setItem(items.get(i));
            }
        }

        private static <T> ArrayList<Item> castArray(T[] array) {
            ArrayList<Item> target=new ArrayList<>();
            for (T T : array) {
                target.add((Item) T);
            }
            return target;
        }
    }


    private class BackpackPanel extends JPanel{

        private JLabel title;
        private BackpackItemsPanel backpackItemsPanel;

        public BackpackPanel() {
            this.setSize(GUISettings.PANEL_SIZE,GUISettings.PANEL_SIZE);
            FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
            layout.setVgap(25);
            this.setLayout(layout);
            this.setBackground(Color.BLACK);

            title = new JLabel("Backpack", SwingConstants.CENTER);
            title.setFont(GUISettings.BIG_FONT);
            title.setForeground(Color.WHITE);

            backpackItemsPanel=new BackpackItemsPanel();

            this.add(title);
            this.add(backpackItemsPanel);
        }

        void refresh(){
            backpackItemsPanel.refresh();
        }
    }

    private class BackpackItemsPanel extends JPanel{

        private ItemSlot[] itemSlots;

        public BackpackItemsPanel() {
            this.setPreferredSize(new Dimension(GUISettings.ITEM_ICON_SIZE*7,GUISettings.ITEM_ICON_SIZE*6));
            FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
            layout.setVgap(0);
            layout.setHgap(0);
            this.setLayout(layout);
            this.setBackground(Color.BLACK);

            itemSlots=new ItemSlot[42];

            for(int i=0;i<42;i++){
                itemSlots[i]=new ItemSlot(null, BAG_SLOT_ICON);
                this.add(itemSlots[i]);
            }
        }

        public void refresh(){
            int i=0;
            var items=equipment.getParty().getBackpack().getItems();
            for(;i<items.size() && i<42;i++){
                itemSlots[i].setItem(items.get(i));
            }
            for(;i<42;i++){
                itemSlots[i].setItem(null);
            }
        }
    }

    private class SmallBackpackItemsPanel extends JPanel{

        public SmallBackpackItemsPanel() {
            this.setPreferredSize(new Dimension(GUISettings.ITEM_ICON_SIZE*7,GUISettings.ITEM_ICON_SIZE*2));
            FlowLayout layout=new FlowLayout(FlowLayout.CENTER);
            layout.setVgap(0);
            layout.setHgap(0);
            this.setLayout(layout);
            this.setBackground(Color.BLACK);

            for(int i=0;i<14;i++){
                if(i%7!=6)
                    this.add(new ItemSlot(null, BAG_SLOT_ICON));
                else{
                    JButton button = new JButton(i<7?"Next":"Prev");
                    button.setForeground(Color.WHITE);
                    button.setPreferredSize(new Dimension(GUISettings.ITEM_ICON_SIZE,GUISettings.ITEM_ICON_SIZE));
                    button.setBackground(Color.BLACK);
                    button.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
                    this.add(button);
                }
            }
        }
    }
}

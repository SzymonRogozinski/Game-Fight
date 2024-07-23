package Generators;

import Equipment.Items.ArmorItem;
import Equipment.Items.ItemQuality;
import Game.GameCollection;
import Game.Tags;

import javax.swing.*;

public class ArmorGenerator {

    private static final Tags[] classTags = new Tags[]{Tags.WARRIOR, Tags.THIEF, Tags.MAGE};
    private static final int STATS_COUNT=6;
    private static final int ARMOR_PARTS_COUNT=4;
    private static final double MAX_STAT_VALUE_PROP=0.4;

    public static ArmorItem generateArmor(ItemQuality quality){
        if (quality==null)
            throw new IllegalArgumentException("Quality of item cannot be null!");
        int points, maxStatValue,armor_part;
        Tags tag=null;
        String name;

        //Roll for random armor part
        armor_part = GameCollection.random.nextInt(ARMOR_PARTS_COUNT);
        name = ArmorDictionary.getArmorPartName(armor_part);

        int[] stats=new int[STATS_COUNT];
        switch (quality){
            case COMMON -> points = getPoints(GeneratorConst.MEDIUM_POINTS*GeneratorConst.COMMON_MOD);
            case RARE -> {
                points = getPoints(GeneratorConst.MEDIUM_POINTS*GeneratorConst.RARE_MOD);
                //Add tag
                if (GameCollection.random.nextBoolean()) {
                    tag = getRandomTag();
                    points += GeneratorConst.TAG_BONUS*GeneratorConst.RARE_MOD;
                    name = ArmorDictionary.getNameFromTag(tag)+" "+name;
                }
            }
            case LEGENDARY -> {
                points = getPoints(GeneratorConst.MEDIUM_POINTS*GeneratorConst.LEGENDARY_MOD)+GeneratorConst.TAG_BONUS*GeneratorConst.LEGENDARY_MOD;
                tag = getRandomTag();
                name = ArmorDictionary.getNameFromTag(tag)+" "+name;
            }
            default -> {
                throw new RuntimeException("Quality not implemented");
            }
        }
        maxStatValue = (int) (points*MAX_STAT_VALUE_PROP);
        int[] highStatIndex = new int[]{-1,-1};
        int highStatCount=0;
        while(points>0){
            int statIndex = GameCollection.random.nextInt(STATS_COUNT);
            int value = GameCollection.random.nextInt(1,Math.min(maxStatValue,points)+1);
            if(stats[statIndex] == maxStatValue){
                value=0;
            } else if(stats[statIndex]+value >= maxStatValue){
                //Save stat index
                highStatIndex[highStatCount++]=statIndex;
                value = maxStatValue - stats[statIndex];
            }
            stats[statIndex]+=value;
            points-=value;
        }
        name = getArmorAdjective(name,highStatIndex,highStatCount);

        ImageIcon icon = null; //TODO
        Tags [] tags = tag==null ? new Tags[]{}:new Tags[]{tag};
        return new ArmorItem(stats[0],stats[1],stats[2],stats[3],stats[4],stats[5],armor_part,tags,icon,name,quality);
    }


    private static Tags getRandomTag(){
        return classTags[GameCollection.random.nextInt(classTags.length)];
    }

    private static int getPoints(int medium){
        int variance = (int) (medium*GeneratorConst.VARIANCE);
        return GameCollection.random.nextInt(medium-variance,medium+variance+1);

    }

    private static String getArmorAdjective(String name, int[] stats, int len){
        if(len==0)
            return name;
        else if(len==1){
            return name +" of "+ ArmorDictionary.getAdjectiveName(stats[0]);
        } else if (len == 2) {
            return name +" of "+ ArmorDictionary.getAdjectiveName(stats[0])+" and "+ArmorDictionary.getAdjectiveName(stats[1]);
        }else{
            throw new RuntimeException("To many high stats");
        }
    }
}
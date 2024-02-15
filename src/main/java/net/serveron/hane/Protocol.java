package net.serveron.hane;

import java.util.HashMap;
import java.util.Map;

public class Protocol {
    private final Map<String,String> idMap = new HashMap<>();
    public Protocol(){
        idMap.put("0x02","Login Success");
        idMap.put("0x28","Login (play)");
        idMap.put("0x6B","Feature Flags");
        idMap.put("0x17","Plugin Message");
        idMap.put("0x0C","Change Difficulty");
        idMap.put("0x34","Player Abilities(Flying Speed.. )");
        idMap.put("0x4D","Set Held Item (Slot)");
        idMap.put("0x6D","Update Recipes");
        idMap.put("0x6E","Update Tags");
        idMap.put("0x1C","Entity Event");
        idMap.put("0x10","Commands");
        idMap.put("0x3D","Update Recipe Book");
        idMap.put("0x58","Update Objectives");
        idMap.put("0x51","Display Objective");
        idMap.put("0x5B","Update Score");
        idMap.put("0x24","\u001B[31mChunk Data and Update Light\u001B[0m");
        idMap.put("0x03","Set Compression");
        idMap.put("0x52","\u001B[33mSet Entity Metadata\u001B[0m");
        idMap.put("0x04","\u001B[33mSet Entity Velocity\u001B[0m");
        idMap.put("0x2B","\u001B[33mUpdate Entity Position\u001B[0m");
        idMap.put("0x65","Set Tab List Header And Footer");
        idMap.put("0x42","Set Head Rotation");
        idMap.put("0x3C","Synchronize Player Position");
        idMap.put("0x68","Teleport Entity");
        idMap.put("0x2C","Update Entity Position and Rotation");
        idMap.put("0x2D","Update Entity Rotation");
        idMap.put("0x6A","Update Attributes(Property)");
        idMap.put("0x69","Update Advancements");
        idMap.put("0x54","Set Entity Velocity");
        idMap.put("0x55","Set Equipment");
        idMap.put("0x56","Set Experience");
        idMap.put("0x57","Set Health");
        idMap.put("0x5E","Update Time");
        idMap.put("0x62","Sound Effect");
    }

    public String getProtocolName(int id){
        return idMap.get(String.format("0x%02X", id));
    }
}

package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;

public class Packet300Custom extends Packet{
    private int id;
    private String[] data;
    private boolean toClient;
    private String modName;
    private String modVersion;
    private String mcVersion;

    public Packet300Custom(Mod mod, boolean toClient, int id, String[] data){
        this.toClient = toClient;
        this.id = id;
        this.data = data;
        modName = mod.getModName();
        modVersion = mod.getModVersion();
        mcVersion = mod.getMcVersion();
    }

    public Packet300Custom(){}

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException{
        modName = readString(par1DataInputStream, 999);
        modVersion = readString(par1DataInputStream, 999);
        mcVersion = readString(par1DataInputStream, 999);
        id = par1DataInputStream.readInt();
        int l = par1DataInputStream.readInt();
        data = new String[l];
        for (int i = 0; i < l; i++){
            data[i] = readString(par1DataInputStream, 999);
        }
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException{
        writeString(modName, par1DataOutputStream);
        writeString(modVersion, par1DataOutputStream);
        writeString(mcVersion, par1DataOutputStream);
        par1DataOutputStream.writeInt(id);
        par1DataOutputStream.writeInt(data.length);
        for (String s : data){
            writeString(s, par1DataOutputStream);
        }
    }

    public void processPacket(NetHandler par1NetHandler){
        if (par1NetHandler.getClass() == net.minecraft.src.NetHandler.class){
            par1NetHandler.registerPacket(this);
        }
        toClient = !par1NetHandler.isServerHandler();
        Minecraft mc = Minecraft.getMinecraft();
        for (int i = 0; i < mc.mods.size(); i++){
            Mod m = ((Mod)mc.mods.get(i));
            if (!m.getModName().equals(modName)){
                continue;
            }
            if (!m.getModVersion().equals(modVersion)){
                String message = "Wrong version of "+modName+"! You need "+modVersion+", not "+m.getModVersion()+"!";
                par1NetHandler.handleKickDisconnect(new Packet255KickDisconnect(message));
                System.out.println(message);
                return;
            }
            if (!m.getMcVersion().equals(mcVersion)){
                String message = "Wrong Minecraft version";
                par1NetHandler.handleKickDisconnect(new Packet255KickDisconnect(message));
                System.out.println(message);
                return;
            }
            if (toClient){
                m.handlePacketFromServer(this);
            }else{
                m.handlePacketFromClient(this);
            }
            return;
        }
        String message = "You need "+modName+" "+modVersion+"!";
        par1NetHandler.handleKickDisconnect(new Packet255KickDisconnect(message));
        System.out.println(message);
    }

    public int getPacketSize(){
        int i = modName.length() + modVersion.length() + mcVersion.length() + 8;
        for (int j = 0; j < data.length; j++){
            i += data[j].length();
        }
        return i;
    }

    public String[] getData(){
        return data;
    }

    public int getId(){
        return id;
    }
}
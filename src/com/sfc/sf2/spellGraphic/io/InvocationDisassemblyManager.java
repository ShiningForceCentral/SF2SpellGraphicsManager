/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TiMMy
 */
public class InvocationDisassemblyManager {
        
    public static InvocationGraphic importDisassembly(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.importDisassembly() - Importing disassembly file ...");
        
        InvocationGraphic invocationGraphic = new InvocationGraphic();
        try {
            Path path = Paths.get(filepath);
            if(path.toFile().exists()) {
                byte[] data = Files.readAllBytes(path);
                if(data.length > 42) {
                    //First 6 bytes = ??? (or 3 words)
                    //4th word = paletteOffset
                    //Next bytes until paletteOffset = offsets for each frame (each frame 128 x 64)(makes half of full sprite)
                    short unknown1 = getNextWord(data, 0);
                    short unknown2 = getNextWord(data, 2);
                    short unknown3 = getNextWord(data, 4);
                    invocationGraphic.setUnknown1(unknown1);
                    invocationGraphic.setUnknown2(unknown2);
                    invocationGraphic.setUnknown3(unknown3);
                    
                    int palettesOffset = getNextWord(data, 6) + 6;
                    byte[] paletteData = new byte[32];
                    System.arraycopy(data, palettesOffset, paletteData, 0, paletteData.length);
                    Color[] palette = PaletteDecoder.parsePalette(paletteData);
                    invocationGraphic.setPalette(palette);
                    
                    int[] frameOffsets = new int[(palettesOffset-8) / 2];
                    for (int i = 0; i < frameOffsets.length; i++) {
                        frameOffsets[i] = getNextWord(data, 8 + i*2) + (8 + i*2);
                    }
                    Tile[][] frameList = new Tile[frameOffsets.length][128];
                    for (int i = 0; i < frameOffsets.length; i++) {
                        int frameOffset = getNextWord(data, 8 + i*2) + (8 + i*2);
                        int dataLength = (i == frameOffsets.length-1) ? data.length-frameOffsets[i] : frameOffsets[i+1] - frameOffsets[i];
                        byte[] tileData = new byte[dataLength];
                        System.arraycopy(data, frameOffset, tileData, 0, dataLength);
                        frameList[i] = new StackGraphicsDecoder().decodeStackGraphics(tileData, palette);
                        System.out.println("Frame "+i+" length="+dataLength+", offset="+frameOffset+", tiles="+frameList[i].length);
                    }
                    invocationGraphic.setFrames(frameList);
                }else{
                    System.out.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.importDisassembly() - File ignored because of too small length (must be a dummy file) " + data.length + " : " + filepath);
                }
            }            
        }catch (Exception e) {
             System.err.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.importDisassembly() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.importDisassembly() - Disassembly imported.");
        return invocationGraphic;
    }
    
    public static void exportDisassembly(InvocationGraphic invocationGraphic, String filepath){
        System.out.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            //TODO
        } catch (Exception ex) {
            Logger.getLogger(SpellDisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }  
        System.out.println("com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
    
    private static short getNextWord(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }
    
    private static byte getNextByte(byte[] data, int cursor){
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor]);
        byte b = bb.get(0);
        return b;
    }
    
    private static void setWord(byte[] data, int cursor, short value){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(value);
        data[cursor+1] = bb.get(0);
        data[cursor] = bb.get(1);
    }
}

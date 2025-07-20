/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.graphics.io.DisassemblyManager;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
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
                        Tile[] frame = new StackGraphicsDecoder().decodeStackGraphics(tileData, palette);
                        frameList[i] = DisassemblyManager.reorderTilesSequentially(frame, 4, 2, 4);
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
                short unknown1 = invocationGraphic.getUnknown1();
                short unknown2 = invocationGraphic.getUnknown2();
                short unknown3 = invocationGraphic.getUnknown3();
                
                Color[] palette = invocationGraphic.getPalette();
                byte[] paletteBytes;
                PaletteEncoder.producePalette(palette);
                paletteBytes = PaletteEncoder.getNewPaletteFileBytes();
                short paletteOffset = (short)(invocationGraphic.getFrames().length*2 + 2);
                
                Tile[][] frames = invocationGraphic.getFrames();
                byte[][] frameBytes = new byte[frames.length][];
                short[] frameOffsets = new short[frames.length];
                int totalFramesSize = 0;
                for (int i = 0; i < frames.length; i++) {
                    Tile[] tiles = DisassemblyManager.reorderTilesForDisasssembly(frames[i], 4, 2, 4);
                    StackGraphicsEncoder.produceGraphics(tiles);
                    frameBytes[i] = StackGraphicsEncoder.getNewGraphicsFileBytes();
                    if (i == 0) {
                        frameOffsets[i] = (short)(frames.length*2 + 32);
                    } else {
                        int target = frameOffsets[i-1] + 6 + (i-1)*2 + frameBytes[i-1].length;
                        int offsetLocation = 6 + i*2;
                        frameOffsets[i] = (short)((target - offsetLocation)&0xFFFF);
                    }
                    System.out.println("Frame "+i+" length="+frameBytes[i].length+", offset="+frameOffsets[i]);
                    totalFramesSize += frameBytes[i].length;
                }
                
                int totalSize = 8 + frames.length * 2 + palette.length * 32 + totalFramesSize;
                byte[] newBattleSpriteFileBytes = new byte[totalSize];
                setWord(newBattleSpriteFileBytes, 0, unknown1);
                setWord(newBattleSpriteFileBytes, 2, unknown2);
                setWord(newBattleSpriteFileBytes, 4, unknown3);
                setWord(newBattleSpriteFileBytes, 6, paletteOffset);
                for(int i=0;i<frameOffsets.length;i++){
                    setWord(newBattleSpriteFileBytes, 8 + i*2, frameOffsets[i]);
                }
                System.arraycopy(paletteBytes, 0, newBattleSpriteFileBytes, 6 + paletteOffset, 32);
                for (int i = 0; i < frameBytes.length; i++) {
                    System.out.println("Writing frame "+i+" with length="+frameBytes[i].length+" at offset="+(int)(frameOffsets[i]+8 + i*2));
                    System.arraycopy(frameBytes[i], 0, newBattleSpriteFileBytes, frameOffsets[i]+8 + i*2, frameBytes[i].length);
                }
                Path graphicsFilePath = Paths.get(filepath);
                Files.write(graphicsFilePath,newBattleSpriteFileBytes);
                System.out.println(newBattleSpriteFileBytes.length + " bytes into " + graphicsFilePath);
            
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.spellGraphic.SpellGraphic;
import com.sfc.sf2.graphics.compressed.StackGraphicsDecoder;
import com.sfc.sf2.graphics.compressed.StackGraphicsEncoder;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
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
public class SpellDisassemblyManager {
    
    public static SpellGraphic importDisassembly(String filepath, Color[] defaultPalette){
        System.out.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.importDisassembly() - Importing disassembly file ...");
        
        SpellGraphic spellGraphic = new SpellGraphic();
        try {
            Path path = Paths.get(filepath);
            if(path.toFile().exists()){
                byte[] data = Files.readAllBytes(path);
                if(data.length>42){
                    byte[] colorData = new byte[6];
                    System.arraycopy(data, 2, colorData, 0, 6);
                    Color[] swapColors = PaletteDecoder.parsePalette(colorData);
                    Color[] palette = new Color[defaultPalette.length];
                    System.arraycopy(defaultPalette, 0, palette, 0, palette.length);
                    palette[9] = swapColors[0];
                    palette[13] = swapColors[1];
                    palette[14] = swapColors[2];
                    byte[] tileData = new byte[data.length - 8];
                    System.arraycopy(data, 8, tileData, 0, tileData.length);
                    Tile[] tiles = new StackGraphicsDecoder().decodeStackGraphics(tileData, palette);
                    spellGraphic.setPalette(palette);
                    spellGraphic.setTiles(tiles);
                }else{
                    System.out.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.parseGraphics() - File ignored because of too small length (must be a dummy file) " + data.length + " : " + filepath);
                }
            }            
        }catch(Exception e){
             System.err.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.parseGraphics() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }    
        System.out.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.importDisassembly() - Disassembly imported.");
        return spellGraphic;
    }
    
    public static void exportDisassembly(SpellGraphic spellGraphic, String filepath){
        System.out.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.exportDisassembly() - Exporting disassembly ...");
        try {
            Color[] palette = spellGraphic.getPalette();
            Color[] swapColors = new Color[3];
            swapColors[0] = palette[9];
            swapColors[1] = palette[13];
            swapColors[2] = palette[14];
            PaletteEncoder.producePalette(swapColors);
            byte[] colorSwapBytes = PaletteEncoder.getNewPaletteFileBytes();
            StackGraphicsEncoder.produceGraphics(spellGraphic.getTiles());
            byte[] tilesBytes = StackGraphicsEncoder.getNewGraphicsFileBytes();

            byte[] newSpellGraphicFileBytes = new byte[tilesBytes.length + 8];
            setWord(newSpellGraphicFileBytes,0,(short)(StackGraphicsEncoder.getNewGraphicsFileUncompressedBytes()));
            System.arraycopy(colorSwapBytes, 0, newSpellGraphicFileBytes, 2, colorSwapBytes.length);
            System.arraycopy(tilesBytes, 0, newSpellGraphicFileBytes, 8, tilesBytes.length);

            Path graphicsFilePath = Paths.get(filepath);
            Files.write(graphicsFilePath,newSpellGraphicFileBytes);
            System.out.println(newSpellGraphicFileBytes.length + " bytes into " + graphicsFilePath);
        } catch (Exception ex) {
            Logger.getLogger(SpellDisassemblyManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println(ex);
        }  
        System.out.println("com.sfc.sf2.spellGraphic.io.spellDisassemblyManager.exportDisassembly() - Disassembly exported.");        
    }
    
    private static void setWord(byte[] data, int cursor, short value){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(value);
        data[cursor+1] = bb.get(0);
        data[cursor] = bb.get(1);
    }
}

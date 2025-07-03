/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.spellGraphic.layout.SpellGraphicLayout;
import com.sfc.sf2.palette.graphics.PaletteDecoder;
import com.sfc.sf2.palette.graphics.PaletteEncoder;
import com.sfc.sf2.spellGraphic.SpellGraphic;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author TiMMy
 */
public class GifManager {

    public static SpellGraphic importGif(String filepath, Color[] defaultPalette) {
        SpellGraphic spellGraphic = null;
        System.out.println("com.sfc.sf2.spellGraphic.io.GifManager.importGif() - Importing GIF files ...");
        try {
            spellGraphic = loadGifFile(filepath, defaultPalette);
        } catch (Exception e) {
             System.err.println("com.sfc.sf2.spellGraphic.io.GifManager.importGif() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }
        System.out.println("com.sfc.sf2.spellGraphic.io.GifManager.importGif() - GIF files imported.");
        return spellGraphic;
    }
    
    public static SpellGraphic loadGifFile(String filepath, Color[] defaultPalette) throws IOException {
        SpellGraphic spellGraphic = null;
        boolean cFound = false;
        try {
            Path path = Paths.get(filepath);
            if (!path.toFile().exists()) {
                throw new IOException("FILE NOT FOUND: " + path.toString());
            }
            BufferedImage img = ImageIO.read(path.toFile());
            ColorModel cm = img.getColorModel();
            if (!(cm instanceof IndexColorModel)){
                throw new IOException("GIF FORMAT ERROR : COLORS ARE NOT INDEXED AS EXPECTED.");
            }
            IndexColorModel icm = (IndexColorModel)cm;
            if (icm.getMapSize() > defaultPalette.length) {
                System.err.println("GIF Palette is greater than expected (" + 16 + "). Image colors may not import properly.");
            }
                        
            Color[] spellPalette = new Color[defaultPalette.length];
            int[] filePalette = new int[defaultPalette.length];
            icm.getRGBs(filePalette);
            for (int i = 0; i < defaultPalette.length; i++) {
                spellPalette[i] = defaultPalette[i];
                if (!PngManager.areRgbEqual(defaultPalette[i].getRGB(), filePalette[i])) {
                    if (i == 9 || i == 13 || i == 14) {
                        //Indexes 9, 13, & 14 are meant to be replaced by the spell
                        spellPalette[i] = new Color(filePalette[i]);
                    } else {
                        System.err.println("GIF Palette overrides default palette at index " + i + ". Image colors may not import properly.");
                    }
                }
            }
            
            
            int imageWidth = img.getWidth();
            int imageHeight = img.getHeight();
            if (imageWidth%8!=0 || imageHeight%8!=0) {
                throw new IOException("GIF FORMAT WARNING : DIMENSIONS ARE NOT MULTIPLES OF 8. (8 pixels per tile)");
            }

            int tileWidth = imageWidth / 8;
            int tileHeight = imageHeight / 8;
            Tile[] tiles = new Tile[tileWidth * tileHeight];
            int tileId = 0;
            for (int j = 0; j < tileHeight; j++) {
                for (int i = 0; i < tileWidth; i++) {
                    int x = i*8;
                    int y = j*8;
                    //System.out.println("Building tile from coordinates "+x+":"+y);
                    Tile tile = new Tile();
                    tile.setId(tileId);
                    tile.setPalette(spellPalette);
                    for (int tY=0;tY<8;tY++) {
                        for (int tX=0;tX<8;tX++) {
                            Color color = new Color(img.getRGB(x+tX,y+tY));
                            int a = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getAlpha()]&0xE);
                            int b = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getBlue()]&0xE);
                            int g = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getGreen()]&0xE);
                            int r = PaletteDecoder.VALUE_MAP.get(PaletteEncoder.VALUE_ARRAY[color.getRed()]&0xE);
                            Color standardizedColor = new Color(r,g,b);
                            cFound = false;
                            for (int c=0;c<16;c++) {
                                if(PngManager.areRgbEqual(standardizedColor, spellPalette[c])) {
                                    tile.setPixel(tX, tY, c);
                                    cFound = true;
                                    break;
                                }
                            }
                            if (!cFound) {
                                //TODO find nearest color with lowest r*g*b diff
                                int diff = Integer.MAX_VALUE;
                                int index = 0;
                                if(a>=128){
                                    index=0;
                                } else {
                                    for (int c=0;c<16;c++) {
                                        int bDiff = Math.abs(spellPalette[c].getBlue()-color.getBlue())+1;
                                        int gDiff = Math.abs(spellPalette[c].getGreen()-color.getGreen())+1;
                                        int rDiff = Math.abs(spellPalette[c].getRed()-color.getRed())+1;
                                        int candidateDiff = bDiff * gDiff * rDiff;
                                        if(candidateDiff<=diff){
                                            diff = candidateDiff;
                                            index = c;
                                        }
                                    }
                                }
                                tile.setPixel(tX, tY, index);
                            }
                        }
                    }
                    tiles[tileId] = tile;
                    tileId++;
                }
            }
            spellGraphic = new SpellGraphic();
            spellGraphic.setTiles(tiles);
            spellGraphic.setPalette(spellPalette);
        } catch (Exception e) {
             System.err.println("com.sfc.sf2.spellGraphic.io.GifManager.importGif() - Error while parsing GIF data : "+e);
             throw e;
        }
        return spellGraphic;
    }
    
    public static void exportGif(SpellGraphic spellGraphic, String filepath) {
        try {
            System.out.println("com.sfc.sf2.spellGraphic.io.GifManager.exportGif() - Exporting GIF file ...");
            BufferedImage image = SpellGraphicLayout.buildImage(spellGraphic, true);
            File outputfile = new File(filepath);
            System.out.println("File path : "+outputfile.getAbsolutePath());
            ImageIO.write(image, "gif", outputfile);
            System.out.println("GIF file exported : " + outputfile.getAbsolutePath());
            System.out.println("com.sfc.sf2.spellGraphic.io.GifManager.exportGif() - GIF file exported.");
        } catch (Exception ex) {
            Logger.getLogger(GifManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

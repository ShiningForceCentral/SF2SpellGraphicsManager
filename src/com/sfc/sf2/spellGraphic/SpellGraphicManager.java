/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.spellGraphic.io.SpellDisassemblyManager;
import com.sfc.sf2.palette.PaletteManager;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class SpellGraphicManager {
       
    private final PaletteManager paletteManager = new PaletteManager();
    private final GraphicsManager graphicsManager = new GraphicsManager();
    private Palette defaultPalette;
    private SpellGraphic spellGraphic;

    public void importDisassembly(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Importing disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        spellGraphic = SpellDisassemblyManager.importDisassembly(filepath, defaultPalette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Exporting disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        SpellDisassemblyManager.exportDisassembly(spellGraphic, filepath);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly exported.");        
    }   
        
    public void importPng(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - Importing PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        graphicsManager.importPng(filepath);
        spellGraphic = new SpellGraphic();
        spellGraphic.setTiles(graphicsManager.getTiles());
        Palette palette = spellGraphic.getPalette();
        adjustImportedPalette(defaultPalette, palette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath, int tilesPerRow) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - Exporting PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        graphicsManager.setTiles(spellGraphic.getTiles());
        graphicsManager.exportPng(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - PNG exported.");       
    }
        
    public void importGif(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importGif() - Importing GIF ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        graphicsManager.importGif(filepath);
        spellGraphic = new SpellGraphic();
        spellGraphic.setTiles(graphicsManager.getTiles());
        Palette palette = spellGraphic.getPalette();
        adjustImportedPalette(defaultPalette, palette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath, int tilesPerRow) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportGif() - Exporting GIF ...");
        graphicsManager.setTiles(spellGraphic.getTiles());
        graphicsManager.exportGif(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportGif() - GIF exported.");       
    }
    
    private void importDefaultPalette(String palettePath) {        
        if (defaultPalette == null) {
            paletteManager.importDisassembly(palettePath);
            defaultPalette = paletteManager.getPalette();
        }
    }
    
    private static void adjustImportedPalette(Palette defaultPalette, Palette importedPalette) {
        for (int i = 0; i < defaultPalette.getColors().length; i++) {
            if (i != 9 && i != 13 && i != 14)
                importedPalette.getColors()[i] = defaultPalette.getColors()[i];
        }
    }
    
    private String getAbsoluteFilepath(String filepath) {
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        Path filePath = Paths.get(filepath);
        if (!filePath.isAbsolute())
            filePath = toolPath.resolve(filePath);
        
        return filePath.toString();
    }
    
    public void clearData() {
        defaultPalette = null;
        spellGraphic = null;
    }

    public SpellGraphic getSpellGraphic() {
        return spellGraphic;
    }

    public void setSpellGraphic(SpellGraphic spellGraphic) {
        this.spellGraphic = spellGraphic;
    }

    public Palette getDefaultPalette() {
        return defaultPalette;
    }
}

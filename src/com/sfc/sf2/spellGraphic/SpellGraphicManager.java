/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.spellGraphic.io.DisassemblyManager;
import com.sfc.sf2.spellGraphic.io.PngManager;
import com.sfc.sf2.spellGraphic.io.GifManager;
import com.sfc.sf2.palette.PaletteManager;
import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class SpellGraphicManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private Color[] defaultPalette;
    private SpellGraphic spellGraphic;

    public void importDisassembly(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Importing disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        spellGraphic = DisassemblyManager.importDisassembly(filepath, defaultPalette);
        if (spellGraphic.getTiles() != null && spellGraphic.getTiles().length > 0) {
            graphicsManager.setTiles(spellGraphic.getTiles());
        }
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Exporting disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        DisassemblyManager.exportDisassembly(spellGraphic, filepath);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importDisassembly() - Disassembly exported.");        
    }   
        
    public void importPng(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - Importing PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        spellGraphic = PngManager.importPng(filepath, defaultPalette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - Exporting PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        PngManager.exportPng(spellGraphic, filepath);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportPng() - PNG exported.");       
    }
        
    public void importGif(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importGif() - Importing GIF ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        spellGraphic = GifManager.importGif(filepath, defaultPalette);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath) {
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportGif() - Exporting GIF ...");
        filepath = getAbsoluteFilepath(filepath);
        GifManager.exportGif(spellGraphic, filepath);
        System.out.println("com.sfc.sf2.spellGraphic.SpellGraphicManager.exportGif() - GIF exported.");       
    }
    
    private void importDefaultPalette(String palettePath) {        
        paletteManager.importDisassembly(palettePath);
        defaultPalette = paletteManager.getPalette();
    }
    
    private String getAbsoluteFilepath(String filepath) {
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        Path filePath = Paths.get(filepath);
        if (!filePath.isAbsolute())
            filePath = toolPath.resolve(filePath);
        
        return filePath.toString();
    }

    public SpellGraphic getSpellGraphic() {
        return spellGraphic;
    }

    public void setSpellGraphic(SpellGraphic spellGraphic) {
        this.spellGraphic = spellGraphic;
    }

    public Color[] getDefaultPalette() {
        return defaultPalette;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.invocationGraphic;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.palette.PaletteManager;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager;
import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphicManager {
       
    private PaletteManager paletteManager = new PaletteManager();
    private GraphicsManager graphicsManager = new GraphicsManager();
    private Color[] defaultPalette;
    private InvocationGraphic invocationGraphic;

    public void importDisassembly(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importDisassembly() - Importing disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        invocationGraphic = InvocationDisassemblyManager.importDisassembly(filepath);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importDisassembly() - Exporting disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
        InvocationDisassemblyManager.exportDisassembly(invocationGraphic, filepath);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importDisassembly() - Disassembly exported.");        
    }   
        
    public void importPng(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importPng() - Importing PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        graphicsManager.importPng(filepath);
        invocationGraphic = new InvocationGraphic();
        //invocationGraphic.setTiles(graphicsManager.getTiles());
        Color[] palette = invocationGraphic.getFrames()[0][0].getPalette();
        adjustImportedPalette(defaultPalette, palette);
        invocationGraphic.setPalette(palette);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportPng() - Exporting PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        //graphicsManager.setTiles(invocationGraphic.getTiles());
        //graphicsManager.exportPng(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportPng() - PNG exported.");       
    }
        
    public void importGif(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importGif() - Importing GIF ...");
        filepath = getAbsoluteFilepath(filepath);
        importDefaultPalette(defaultPalettePath);
        graphicsManager.importGif(filepath);
        invocationGraphic = new InvocationGraphic();
        //invocationGraphic.setTiles(graphicsManager.getTiles());
        Color[] palette = invocationGraphic.getFrames()[0][0].getPalette();
        adjustImportedPalette(defaultPalette, palette);
        invocationGraphic.setPalette(palette);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportGif() - Exporting GIF ...");
        //graphicsManager.setTiles(invocationGraphic.getTiles());
        //graphicsManager.exportGif(filepath, tilesPerRow);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportGif() - GIF exported.");       
    }
    
    private void importDefaultPalette(String palettePath) {        
        paletteManager.importDisassembly(palettePath);
        defaultPalette = paletteManager.getPalette();
    }
    
    private static void adjustImportedPalette(Color[] defaultPalette, Color[] importedPalette) {
        for (int i = 0; i < defaultPalette.length; i++) {
            if (i != 9 && i != 13 && i != 14)
                importedPalette[i] = defaultPalette[i];
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
        invocationGraphic = null;
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }

    public Color[] getDefaultPalette() {
        return defaultPalette;
    }
}

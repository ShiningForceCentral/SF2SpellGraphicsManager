/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.invocationGraphic;

import com.sfc.sf2.graphics.GraphicsManager;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import com.sfc.sf2.spellGraphic.io.InvocationDisassemblyManager;
import com.sfc.sf2.spellGraphic.io.RawImageManager;
import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphicManager {
       
    private InvocationGraphic invocationGraphic;

    public void importDisassembly(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importDisassembly() - Importing disassembly ...");
        filepath = getAbsoluteFilepath(filepath);
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
        invocationGraphic = RawImageManager.importImage(filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportPng() - Exporting PNG ...");
        filepath = getAbsoluteFilepath(filepath);
        RawImageManager.exportImage(invocationGraphic, filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportPng() - PNG exported.");       
    }
        
    public void importGif(String filepath, String defaultPalettePath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importGif() - Importing GIF ...");
        filepath = getAbsoluteFilepath(filepath);
        invocationGraphic = RawImageManager.importImage(filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath) {
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportGif() - Exporting GIF ...");
        RawImageManager.exportImage(invocationGraphic, filepath, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.invocationGraphic.InvocationGraphicManager.exportGif() - GIF exported.");       
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
        invocationGraphic = null;
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }
}

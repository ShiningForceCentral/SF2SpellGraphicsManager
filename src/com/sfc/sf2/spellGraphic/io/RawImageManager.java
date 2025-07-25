/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.io;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wiz
 */
public class RawImageManager {
    
    public static InvocationGraphic importImage(String filepath, int fileFormat) {
        System.out.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.importImage() - Importing image files ...");
        InvocationGraphic invocationGraphic = null;
        try {
            List<Tile[]> frames = new ArrayList<Tile[]>();
            Palette palette = null;
            String dir = filepath.substring(0, filepath.lastIndexOf(System.getProperty("file.separator")));
            String pattern = filepath.substring(filepath.lastIndexOf(System.getProperty("file.separator"))+1);
            File directory = new File(dir);
            File[] files = directory.listFiles();
            for (File f : files) {
                if (f.getName().startsWith(pattern + "-frame") && f.getName().endsWith(com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat))) {
                    Tile[] frame = com.sfc.sf2.graphics.io.RawImageManager.importImage(f.getAbsolutePath());
                    palette = frame[0].getPalette();
                    frames.add(frame);
                }
            }
            if (frames.isEmpty()) {
                System.err.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.importImage() - ERROR : no frame imported. Image files missing for this pattern ?");
            } else {
                System.out.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.importImage() - " + frames.size() + " : " + frames);
                invocationGraphic = new InvocationGraphic();
                invocationGraphic.setFrames(frames.toArray(new Tile[frames.size()][]));
                invocationGraphic.setPalette(palette);
                
                String metaPath = filepath + ".meta";
                File inputFile = new File(metaPath);
                if (inputFile.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    String data = reader.readLine();
                    data = data.substring(data.indexOf(":")+1).trim();
                    invocationGraphic.setUnknown1(Short.parseShort(data));
                    data = reader.readLine();
                    data = data.substring(data.indexOf(":")+1).trim();
                    invocationGraphic.setUnknown2(Short.parseShort(data));
                    data = reader.readLine();
                    data = data.substring(data.indexOf(":")+1).trim();
                    invocationGraphic.setUnknown3(Short.parseShort(data));
                    reader.close();
                }
            }
        } catch(Exception e) {
             System.err.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.importImage() - Error while parsing graphics data : "+e);
             e.printStackTrace();
        }        
        System.out.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.importImage() - image files imported.");
        return invocationGraphic;                
    }
    
    public static void exportImage(InvocationGraphic invocationGraphic, String filepath, int fileFormat) {
        try {
            System.out.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.exportImage() - Exporting image files and palettes ...");
            Tile[][] frames = invocationGraphic.getFrames();
            for(int i=0;i<frames.length;i++){
                String framePath = filepath + "-frame-" + String.valueOf(i) + "." + com.sfc.sf2.graphics.io.RawImageManager.GetFileExtensionString(fileFormat);
                com.sfc.sf2.graphics.io.RawImageManager.exportImage(frames[i], framePath, 16, fileFormat);
            }
            String metaPath = filepath + ".meta";
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown 1: " + invocationGraphic.getUnknown1() + "\n");
            sb.append("Unknown 2: " + invocationGraphic.getUnknown2() + "\n");
            sb.append("Unknown 3: " + invocationGraphic.getUnknown3() + "\n");
            File outputfile = new File(metaPath);
            FileWriter writer = new FileWriter(outputfile, false);
            writer.write(sb.toString());
            writer.close();
            System.out.println("com.sfc.sf2.invocationGraphic.io.RawImageManager.exportImage() - image files and palettes exported.");
        } catch (Exception ex) {
            Logger.getLogger(RawImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

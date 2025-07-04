/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.spellGraphic.SpellGraphic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class SpellGraphicLayout extends JPanel {
    private static int tilesPerRow = 8;
    private int displaySize;
    private boolean showGrid = true;
    
    private SpellGraphic spellGraphic;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        BufferedImage image = buildImage(spellGraphic, false);
        image = resize(image);
        setSize(image.getWidth(), image.getHeight());
        if (showGrid) { drawGrid(image); }
        return image;
    }
    
    public static BufferedImage buildImage(SpellGraphic spellGraphic, boolean pngExport) {
        Tile[] tiles = spellGraphic.getTiles();
        int imageWidth = tilesPerRow;
        int imageHeight = (tiles.length / tilesPerRow);
        BufferedImage image;
        image = new BufferedImage(imageWidth*8, imageHeight*8, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                graphics.drawImage(tiles[x+y*tilesPerRow].getIndexedColorImage(), x*8, y*8, null);
            }
        }
        graphics.dispose();
        return image;
    }
    
    private void drawGrid(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        while (x < image.getWidth()) {
            graphics.drawLine(x, 0, x, image.getHeight());
            x += 8*displaySize;
        }
        graphics.drawLine(x-1, 0, x-1, image.getHeight());
        while (y < image.getHeight()) {
            graphics.drawLine(0, y, image.getWidth(), y);
            y += 8*displaySize;
        }
        graphics.drawLine(0, y-1, image.getWidth(), y-1);
        graphics.dispose();
    }
    
    private BufferedImage resize(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth()*displaySize, image.getHeight()*displaySize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displaySize, image.getHeight()*displaySize, null);
        g.dispose();
        return newImage;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public SpellGraphic getSpellGraphic() {
        return spellGraphic;
    }

    public void setSpellGraphic(SpellGraphic spellGraphic) {
        this.spellGraphic = spellGraphic;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        this.revalidate();
    } 

    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        SpellGraphicLayout.tilesPerRow = tilesPerRow;
        this.revalidate();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.revalidate();
    } 
}

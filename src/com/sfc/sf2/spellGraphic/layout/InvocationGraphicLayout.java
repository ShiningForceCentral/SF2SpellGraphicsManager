/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic.layout;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.spellGraphic.InvocationGraphic;
import com.sfc.sf2.spellGraphic.SpellGraphic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphicLayout extends JPanel {
    private int displaySize;
    
    private InvocationGraphic invocationGraphic;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buildImage(), 0, 0, this);       
    }
    
    public BufferedImage buildImage() {
        BufferedImage image = buildImage(invocationGraphic, false);
        image = resize(image);
        setSize(image.getWidth(), image.getHeight());
        return image;
    }
    
    public static BufferedImage buildImage(InvocationGraphic invocationGraphic, boolean pngExport) {
        return null;
        /*int blockColumnsNumber = (battlespriteType == BattleSprite.TYPE_ALLY) ? 3 : 4;
        int blockRowNumber = (battlespriteType == BattleSprite.TYPE_INVOCATION) ? 2 : 3;
        int imageHeight = blockRowNumber*4*8;
        BufferedImage image;
        IndexColorModel icm = buildIndexColorModel(tiles[0].getPalette());
        image = new BufferedImage(tilesPerRow*8, imageHeight , BufferedImage.TYPE_BYTE_BINARY, icm);
        Graphics graphics = image.getGraphics();
        for(int frame=0;frame<(tiles.length/(blockColumnsNumber*4*blockRowNumber*4));frame++){*/
            /*
                1  5  9 13 49 53                  
                2  6 10 14 50  .                  
                3  7 11 15 51  .                  
                4  8 12 16 52  .                  
               17 21 25 29  
               18 22 26 30
               19 23 27 31
               20 24 28 32
               33 37 41 45                  . 141
               34 38 42 46                  . 142
               35 39 43 47                  . 143
               36 40 44 48                140 144
            */
            /*int index = 0;
            for(int blockColumn=0;blockColumn<blockColumnsNumber;blockColumn++){
                for(int blockRow=0;blockRow<blockRowNumber;blockRow++){
                    for(int tileColumn=0;tileColumn<4;tileColumn++){
                        for(int tileRow=0;tileRow<4;tileRow++){
                            int x = (blockColumn*4+tileColumn)*8;
                            int y = (frame*blockRowNumber*4+blockRow*4+tileRow)*8;
                            graphics.drawImage(tiles[index].getImage(), x, y, null);
                            index++;
                        }
                    }
                }
            }
        }
        graphics.dispose();
        return image;*/
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

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
        this.revalidate();
    }
}

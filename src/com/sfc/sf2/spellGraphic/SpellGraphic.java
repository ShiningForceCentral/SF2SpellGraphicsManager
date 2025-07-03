/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.graphics.Tile;
import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public class SpellGraphic {
        
    private Tile[] tiles;
    
    Color[] palette;

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public Color[] getPalette() {
        return palette;
    }

    public void setPalette(Color[] palette) {
        this.palette = palette;
    }
}

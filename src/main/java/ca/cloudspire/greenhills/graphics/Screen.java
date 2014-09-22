package ca.cloudspire.greenhills.graphics;

/**
 * Copyright (c) 2014.
 * Created by Ryan Saunderson on 2014-09-22.
 */
public class Screen
{
    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH-1;

    public int[] tiles = new int[MAP_WIDTH*MAP_WIDTH];
    public int[] colours = new int[MAP_WIDTH*MAP_WIDTH*4];

    public int xOffset = 0;
    public int yOffset = 0;

    public int width;
    public int height;

    public SpriteSheet sheet;

    public Screen(int width, int height, SpriteSheet sheet)
    {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        for (int i=0;i<MAP_WIDTH*MAP_WIDTH;i++)
        {
            colours[i*4+0] = 0xff5555;
            colours[i*4+1] = 0xff7788;
            colours[i*4+2] = 0xff9922;
            colours[i*4+3] = 0x778899;
        }
    }

    public void render(int[] pixels, int offset, int row)
    {
        for (int yTile = yOffset>>3;yTile<=(yOffset+height)>>3;yTile++)
        {
            int yMin = yTile*16 - yOffset;
            int yMax = yMin + 16;
            if (yMin<0)yMin = 0;
            if (yMax>height)yMax = height;

            for (int xTile = xOffset>>3;xTile<=(xOffset+width)>>3;xTile++)
            {
                int xMin = xTile*16 - xOffset;
                int xMax = xMin + 16;
                if (xMin<0)xMin = 0;
                if (xMax>width)xMax = width;

                int tileIndex = (xTile & (MAP_WIDTH_MASK))+(yTile & (MAP_WIDTH_MASK))*MAP_WIDTH;

                for (int y = yMin;y<yMax;y++)
                {
                    int sheetPixel = ((y+yOffset) & 15)*sheet.width+((xMin+xOffset) & 15);
                    int tilePixel = offset+xMin+y*row;

                    for (int x =xMin;x<xMax;x++)
                    {
                        int colour = tileIndex*4+sheet.pixels[sheetPixel++];
                        pixels[tilePixel++] = colours[colour];
                    }
                }
            }
        }
    }
}

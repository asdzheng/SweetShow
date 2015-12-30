package com.asdzheng.sweetshow.utils.recyclerview;

/**
 * Created by asdzheng on 2015/12/26.
 */
public class Size
{
    private int height;
    private int width;

    public Size(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public String toString() {
        return "withd = " + width + " | height = " + height;
    }
}
package com.deadlist.core.entity;

public class Texture {

    private final int id;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    private int numberOfRows = 1;

    public Texture(int id){
        this.id = id;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getId() {
        return id;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting(){
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting){
        this.useFakeLighting = useFakeLighting;
    }
}


package com.example.jeuvaisseaux;

import android.widget.ImageView;

//La librairie contient les définitions du joystick et du vaisseau.

final class Accelerometer{
    private float xActuel;
    private float yActuel;
    private float decalageX;
    private float decalageY;
    private boolean initMode;

    public Accelerometer() {
        this.xActuel = 0;
        this.yActuel = 0;
        this.decalageX = 0;
        this.decalageY = 0;
        this.initMode = true;
    }

    public boolean isInitMode() {
        return initMode;
    }

    public void setInitMode(boolean initMode) {
        this.initMode = initMode;
    }

    public float getDecalageX() {
        return decalageX;
    }

    public void setDecalageX(float decalageX) {
        this.decalageX = decalageX;
    }

    public float getDecalageY() {
        return decalageY;
    }

    public void setDecalageY(float decalageY) {
        this.decalageY = decalageY;
    }


    public float getxActuel() {
        return xActuel;
    }

    public void setxActuel(float xActuel) {
        this.xActuel = xActuel;
    }

    public float getyActuel() {
        return yActuel;
    }

    public void setyActuel(float yActuel) {
        this.yActuel = yActuel;
    }
}


final class Joystick{
    private ImageView joystickImg;
    private boolean isPressed;
    private final float distanceMax;

    Joystick(){
        this.isPressed = false;
        this.distanceMax = 250;
    }

    public float getDistanceMax() {
        return distanceMax;
    }

    public void setPositionX(float positionX) {
        joystickImg.setX(positionX);
    }

    public void setPositionY(float positionY) {
        joystickImg.setY(positionY);
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean pressed) {
        isPressed = pressed;
    }

    public void setJoystickImg(ImageView joystickImg) {
        this.joystickImg = joystickImg;
    }

}


final class Tie{

    private ImageView tieImage;
    private float positionX;
    private float positionY;
    private float xmin;  //sera utilisé par la suite
    private float xmax;
    private float ymin;
    private float ymax;



    Tie(){
        this.positionX = 0;
        this.positionY = 0;
        this.xmin = 0;
        this.xmax = 790;
        this.ymin = 0;
        this.ymax = 1280;
    }

    public float getXmin() {
        return xmin;
    }

    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    public float getXmax() {
        return xmax;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    public float getYmin() {
        return ymin;
    }

    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

    public float getYmax() {
        return ymax;
    }

    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        tieImage.setX(positionX);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        tieImage.setY(positionY);
    }

    public void setTieImage(ImageView tieImage) {
        this.tieImage = tieImage;
    }

    public ImageView getTieImage() {
        return tieImage;
    }

}
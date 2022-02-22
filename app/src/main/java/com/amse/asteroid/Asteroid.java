package com.amse.asteroid;

public class Asteroid {
    private double x;
    private double y;
    private double dx;
    private double dy;
    Asteroid(double init_x, double init_y){
        this.x = init_x;
        this.y = init_y;
    }
    public void update(){
        this.x += this.dx;
        this.y += this.dy;
    }
    public double getPosX(){
        return this.x;
    }
    public double getPosY(){
        return this.y;
    }
}

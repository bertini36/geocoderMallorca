package geocoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Point {
    
    private double x;
    private double y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public double getX() {
        return x;
    }
   
    public double getY() {
        return y;
    }
     
    public boolean equals (Point point) {
        return this.getX() == point.getX() && this.getY() == point.getY();
    }
       
}

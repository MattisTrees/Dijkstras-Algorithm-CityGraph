/* CityMap.Java  -- class file
 *  
 * This software package creates a graph of cities in the Unitied States with
 * links between the cities. Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph.   The data for the cities and
 * links are read into arrays from data files, which should be in the project folder.
 * The files are CSV files, which can be read and edited in Excel.
 *
 * The main class for the project is the CityProject class.   Other class include:
 * 
 *   Vertex - clas for each Vertex in a graph.
 *   City extends Vertex - Each City is a Vertex with added properties.  Each City
 *      has a unique name, and X and Y cooordinates for location on a 1500 by 900 Canvas.
 *   Edge - an edge in the graph, with a source, destination, and length.
 *   AjacencyNode - a node for a linked list of cities directly connected to each City.
 *      Each City has a linked list of adjacnt cities, created from the info in the 
 *      data files, with destination City and distance data in the node, and a 
 *      link to the next node. 
 *   CityMap - extends Canvas, a map of the graph on a 1500 by 900 GUI Canvas.
 *      A CityMap object in instantiated in the drawMap method in the CityProject class.
 * 
 * The main method in the CityProject class calls methods to reads City and Edge 
 * data from data files into arrays, set up the adjacency list in each instance 
 * of City, print a list of Vertex cities and their Edges, then draw a map of the graph.
 *
 * created for use by students in CSCI 211 at Community Colle of Philadelphia
 * copyright 2014 by C. herbert.  last edited Nov. 23, 2014 by C. Herbert
 */
package cityproject;

import java.awt.*;
import javax.swing.*;

public class CityMap extends Canvas {

    City[] cities = new City[200];  //array of cities (Vertices) max = 200
    int cityCount;                  // actual number of cities
    Edge[] links = new Edge[2000];  // array of links  (Edges)  max = 2000
    int linkCount;                  // avtual number of links
    CityStack cityStack;

    CityMap() {

    } // end CityCanvas(...)
    
    CityMap(int cCount, City[] c, int lCount, Edge[] l, CityStack cityStack) {

        this.cities = c;
        this.cityCount = cCount;
        this.links = l;
        this.linkCount = lCount;
        this.cityStack = cityStack;
    } // end CityCanvas(...) 
    
    public void setRoadMap(CityStack stack){
        this.cityStack = stack;
    } // end setRoadMap

    @Override
    public void paint(Graphics graphics) {

        // fill background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 1500, 900);

        // place us image under map
        Image us = new ImageIcon("us.png").getImage();
        graphics.drawImage(us, 125, 0, null);

        // draw links
        graphics.setColor(new Color(200, 200, 200));
        // iterate link array
        for (int i = 0; i < linkCount; i++) {

            // get soucrce city  and destination city coordinates
            int xS = links[i].getSource().getX();       // x coordinate of source city
            int yS = links[i].getSource().getY();       // y coordinate of source city
            int xD = links[i].getDestination().getX();	// x coordinate of destination city
            int yD = links[i].getDestination().getY();  // y coordinate of destination city

            graphics.drawLine(xS, yS, xD, yD);
        } // end for

        // draw cities
        graphics.setColor(Color.red);
        for (int i = 0; i < cityCount; i++) {
            //draw a dot for each city, 4 x 4 pixels
            graphics.fillOval(cities[i].getX() - 3, cities[i].getY() - 3, 6, 6);

        } // end for

        // * * * * * * * * * * * * * * * * * * * * * * * * *  NEW PART TO METHOD
        
        // draw the display on the left
        graphics.setFont(new Font("Lucida Console", Font.BOLD, 22));
        graphics.setColor(Color.black);
        graphics.drawString("Route", 50, 50);
        graphics.drawString("- - - -", 45, 65);
        
        if(cityStack != null){
            
            // offset value for the y position of the drawString method
            int yOff = 45;
            // variable to help determing the y position for the drawSting method
            int yPos = 16;
            // integer for left margin of display text
            int xPos = 40;
            
            graphics.setFont(new Font("Lucida Console", Font.BOLD, 12));
            
            City current = cityStack.pop();
            City next = cityStack.pop();
            City placeholder = current;
            
            int i = 3;
            int xS, yS, xD, yD;
            
            graphics.drawString((i-2) + " - " + current.getCityName(), xPos, (yPos*2)+yOff);
            while(next != null){
                
                graphics.drawString((i-1) + " - " + next.getCityName(), xPos, (i*yPos)+yOff);
                
                xS = current.getX();
                yS = current.getY();
                
                xD = next.getX();
                yD = next.getY();
                
                graphics.setColor(Color.red);
                graphics.drawLine(xS, yS, xD, yD);
                graphics.drawLine(xS, yS+1, xD, yD+1);
                graphics.drawLine(xS+1, yS, xD+1, yD);
                graphics.setColor(Color.black);
                
                current = next;
                next = cityStack.pop();
                i++;
                
                // if finished draw origin and destination city dots larger
                if(next == null){
                    graphics.setColor(Color.yellow);
                    graphics.fillOval(current.getX() - 8, current.getY() - 8, 16, 16);
                    graphics.setColor(Color.blue);
                    graphics.fillOval(placeholder.getX() - 8, placeholder.getY() - 8, 16, 16);
                    graphics.setColor(Color.black);
                    graphics.drawString("Distance: " + current.getBestDistance(), xPos, i*yPos+yOff);
                } // end if branch
                
            } // end while loop
            
        } // end painting of the path
        
        // * * * * * * * * * * * * * * * * * * * * * * * * * * END OF NEW STUFF
        
        
        // draw labels
        graphics.setColor(Color.black);
        graphics.setFont(new Font("Lucida Console", Font.BOLD, 10));
        
        for (int i = 0; i < cityCount; i++) {
            // draw a label for each city, offest by 6 hor. and 9 ver. pixels
            graphics.drawString(cities[i].getName(), cities[i].getX() + 6, cities[i].getY() + 9);
        } // end for

        // add note to the canvas
        Image logo = new ImageIcon("note-smaller.png").getImage();
        graphics.drawImage(logo, 350, 600, null);
        

    }  // end paint()

} // end class CityMap
//**********************************************************************************************************************************

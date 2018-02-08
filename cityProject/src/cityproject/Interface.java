/*
 * This class represents the user interface and all of the methods that go
 * along with it. When an instance of this class is instantiated, a window
 * with two drop down menus appear. These drop down menus are populated with
 * the array of cities. There is also a button to call Djikstra's algorithm
 * and lets the user know what the series of Cities make the shortest path
 * from the Starting city to the Ending City.
 */
package cityproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author matthewjordan
 */
public class Interface extends JFrame implements ActionListener {
    
    private final JComboBox startBox;
    private final JComboBox endBox;
    private final JButton calcBtn;
    
    private final int cityCount;
    private final City[] cityList;
    private final int edgeCount;
    private final Edge[]edges;
    
    private City startCity;
    private City endCity;
    public CityStack cityStack;
    
    private final JFrame mapWindow;
    
    public Interface(int cityCount, City[] cities, int edgeCount, Edge[] edges, JFrame mapWindow) {
        
        this.cityCount = cityCount;
        this.cityList = cities;
        this.edgeCount = edgeCount;
        this.edges = edges;
        this.mapWindow = mapWindow;
        
        // give the intial interface properties
        setLayout(new BorderLayout());
        setTitle("Find Shortest Path");
        setSize(300, 150);
        setLocation(25, 610);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        
        GridLayout grid = new GridLayout(2,2);
        JPanel inputArea = new JPanel();
        inputArea.setLayout(grid);
        
        
        // instantiate drop down lists that contain all of the city vertexes
        // what displays in the dropdown is the return of the toString method
        startBox = new JComboBox(cities);
        startBox.addActionListener(this);
        startBox.setSelectedIndex(0);
        endBox = new JComboBox(cities);
        endBox.addActionListener(this);
        endBox.setSelectedIndex(0);
        // instantiate the calculate button
        calcBtn = new JButton("Calculate");
        calcBtn.addActionListener(this);
        
        inputArea.add(new JLabel("Starting City: "));
        inputArea.add(startBox);
        inputArea.add(new JLabel("Ending City: "));
        inputArea.add(endBox);
        
        this.add(inputArea, BorderLayout.NORTH);
        this.add(calcBtn, BorderLayout.SOUTH);
        
    }  // end Interface constructor

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startBox){
            
            JComboBox tempStart = (JComboBox)e.getSource();
            startCity = (City)tempStart.getSelectedItem();
            
        } else if(e.getSource() == endBox){
            
            JComboBox tempEnd = (JComboBox)e.getSource();
            endCity = (City)tempEnd.getSelectedItem();
            
        } else if(e.getSource() == calcBtn){
            
            for(City city : cityList) // set each city's relevant data back  
                city.clearCity();     // to its original state
            
            cityStack = null;         // get rid of the previous Stack
            
            printNewLines();          // this redraws the canvas with the new data
            
            cityStack = DijkstrasAlg(cityList);
            printNewLines();
            
        } else {
            System.out.println("You've preformed some action that should be impossible");
        }// end if/else block
        
    } // end actionPerformed method override
    
    private void printNewLines(){
        
        CityMap canvas2 = new CityMap(cityCount, cityList, edgeCount, edges, cityStack);
        mapWindow.getContentPane().remove(0);
        mapWindow.getContentPane().add(canvas2);
        mapWindow.setVisible(true);
        
    } // end printNewLines method
    
    // ***************************************************************************
    // *** DIJKSTRAS ALGORITHM *** DIJKSTRAS ALGORITHM *** DIJKSTRAS ALGORITHM ***
    // ***************************************************************************
    private CityStack DijkstrasAlg(City[] cityList){
        
        //     - - - - - STEP 1 - - - - - STEP 1 - - - - -
        
        City currentCity = startCity;
        currentCity.setBestDistance(0);
        
        
        // iterate a loop until all objects in cityList have a false value for
        // their 'visited' variable
        while(isUListEmpty(cityList) == false){
            
            // instantiate AdjacencyNode variable 'adjacentCity' to check for 
            // closest adjacent city to currentCity
            AdjacencyNode adjacentCity = currentCity.getAdjacencyListHead();
            
            /*      - - - - - STEP 2a - - - - - STEP 2a - - - - -
             * 
             * This while loop iterates through the Adjacency list of currentCity
             * and calculates the distance from the startCity to each element in
             * the linked list. If the bestDistance at each element in the list
             * is more than the calculated sum 'newDist', then newDist is set as
             * the value for the best distance to that City. 
             */
            while(adjacentCity != null){
                // calculate distance from startCity to item in adjacency list
                int newDist = currentCity.getBestDistance() + adjacentCity.getDistance();
                
                // if calculated sum of the total distance from 'startCity' to 
                // 'adjacentCity' which is the 'newDist' variable, set that value
                // to the 'bestDistance' value of the "adjacentCity' city
                if(newDist < adjacentCity.getCity().getBestDistance()){
                    adjacentCity.getCity().setBestDistance(newDist);
                    adjacentCity.getCity().setImmediatePredecessor(currentCity);
                } // end if statement
                
                // Iterate through the linked-list
                adjacentCity = adjacentCity.getNext();
                
            } // end while(adjacentCity != null)
            
            /*     - - - - - END STEP 2a - - - - - END STEP 2a - - - - -
            
            
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
             * At this point in time we have iterated through all of the adjacency *
             * nodes of currentCity and determined if any of them have a shorter   *
             * path than any previously calculated paths. Next we set currentCity  *
             * as a city that has been visited already and find the next city      *
             * with the shortest distance.                                         *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            
            
            //     - - - - - STEP 2b - - - - - - - - STEP 2b - - - - - - - 
            // set the currentCity visited value to true
            currentCity.setVisited(true);
            
            //     - - - - - STEP 2c - - - - - - - - STEP 2c - - - - - - - 
            // change currentCity to be an UN-visited city with the shortest distance
            currentCity = closestUnvisited(cityList);
            
        } // end while(isUListEmpty(cityList) == false)
        
        
        //     - - - - - STEP 3 - - - - - - - - - STEP 3 - - - - - - -
        // make a stack using the generated data
        CityStack cityStack = new CityStack(endCity);
        
        return cityStack;
    } // end DijkstrasAlg
    
    
    
    private City closestUnvisited(City[] cityList){
        
        // City variable to hold which city is closest to startCity
        City closest = null;
        // variable to hold the shortest distance from startCity found
        int shortestDist = Integer.MAX_VALUE;
        
        for(City city : cityList){
            // if the current city in the list being looked at hasn't been visited
            // AND it's bestDistance is less than or equal to the value of 
            // shortestDist then the closest city to startCity is set to cityList[i]
            if(city.getVisited() == false && city.getBestDistance() <= shortestDist){
                closest = city;
                shortestDist = city.getBestDistance();
            } // end if block
        } // end for loop
        
        // If the value of closest is null then that means every city has been
        // visited and the while loop will stop. This will not halt the algorithm.
        return closest;
        
    } // end closestUnvisited method
    
    
    /*
     * This method checks the cityList array for any element that has the property
     * of being unvisited. If it finds an unvisited city, then the while loop in the 
     * DijkstrasAlg() method iterates again.
    */
    private boolean isUListEmpty(City[] cityList){
        boolean empty = true;
        
        // for loop to check cityList array for Unvisited Cities.
        // Exit loop (break) if an unvisited city is found
        for (City cityList1 : cityList)
            if (cityList1.getVisited() == false) {
                empty = false;
                break;
            } // end if statement
        
        return empty;
    } // end isUListEmpty
    
} // end Interface class

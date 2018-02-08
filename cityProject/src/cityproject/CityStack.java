/*
 * This is the class to hold the final stack for the list of city from endCity
 * to startCity. The top of this stack is a pointer to startCity and each 
 * subsequent  
 */
package cityproject;

/**
 * @author matthewjordan
 */
public class CityStack {
    
    private CityStackNode top; // pointer to the top of the Stack
    
    public CityStack(City onlyCity, int i){
        top = new CityStackNode();
        push(onlyCity);
    } // end null constructor
    
    public CityStack(City endCity){
        
        top = new CityStackNode();
        City current = endCity;
        
        // while the current city exist
        while(current != null){
            
            // push the current city into the stack
            push(current);
            // itterate to the current City's Immediate Predecessor
            current = current.getImmediatePredecessor();
            
        } // end while loop
        
    } // constructor end method
    
    public void push(City newCity){
        
        if(top.city == null)
            top.city = newCity;
        else {
            CityStackNode newNode = new CityStackNode(newCity, top);
            top = newNode;
        } // end if/else block
        
    } // push end method
    
    public City pop(){
        City temp;
        if(top == null){
            temp = null;
        } else {
            temp = top.city;
            top = top.next;
        } // end if/else branching
        
        return temp;
    } // pop end method
    
    // this method returns a String showing the name of each city in the Stack, in order
    public String stackToString(){
        CityStackNode current = top;
        String message = "";
        
        while(current!=null){
            
            message += current.city.getCityName() + ", " + current.city.getStateName() + " > ";
            // itterate to the next item in the stack
            current = current.next;
            
        } // end while loop
        
        return message;
    } // end stackToString
    
    // this method prints out the name of each city to the console
    public void printStack(){
        
        CityStackNode current = top;
        
        System.out.println("Your route is:\n");
        // while the current city being look at in this method has City data
        // we print out the full city name
        while(current != null){
            System.out.print(current.city.getCityName() 
                    + ", "  + current.city.getStateName());
            if(current.next == null){
                System.out.println();
            } else {
                System.out.print(" >> ");
            } // end branching
            
            // itterate to the next node in the Stack
            current = current.next; 
        } // end while loop
        
    } // end printStack
    
     //This class holds the data for every node in the CityStack class
    class CityStackNode {

        private City city; 
        CityStackNode next;

        public CityStackNode(){
            this.city = null; 
            this.next = null;
        } // CityStackNode null constructor end method
        
        public CityStackNode(City newCity, CityStackNode top){
            this.city = newCity;
            this.next = top;
        } // CityStackNode constructor end method

    } // CityStackNode end class
}// CityStack end class

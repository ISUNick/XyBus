package hw1;

/**
 * XyBus simulates a bus system with multiple stops. It tracks passengers, stranded passengers,
 * and violations (when more people try to get off than are on the bus). Supports looping and backing routes.
 * 
 * @author Nick Bafia
 */
public class XyBus 
{
    /** Current number of passengers on the bus */
    private int numPassengers;

    /** Total number of passengers who have boarded the bus */
    private int totalNumPassengers;

    /** Number of passengers stranded at the current stop */
    private int numStrandedPassengers;

    /** Total number of passengers stranded across all stops */
    private int totalNumStrandedPassengers;

    /** Number of seats on the bus */
    private int numSeats;

    /** Number of available seats at the current stop */
    private int numAvailableSeats;

    /** Total number of stops on the bus route */
    private int numStopsOnRoute;

    /** Total number of violations (people trying to get off who aren't on the bus) */
    private int totalViolations;

    /** Total number of stops the bus has made */
    private int totalStopsMade;

    /** String containing the names of all stops (each name 8 characters) */
    private String nameOfStops;

    /** 1 = service on, 0 = service off */
    private int serviceOn = 1;  

    /**
     * Constructor to initialize a bus with seats, stops, and stop names.
     * 
     * @param initNumSeats Number of seats on the bus
     * @param initNumStopsOnRoute Number of stops on the route
     * @param initNameOfStops String containing names of all stops
     */
    public XyBus(int initNumSeats, int initNumStopsOnRoute, String initNameOfStops)
    {           
        numSeats = initNumSeats;
        numStopsOnRoute = initNumStopsOnRoute;
        nameOfStops = initNameOfStops;
    }

    /**
     * Processes a normal stop, handling passengers getting on/off. 
     * Counts violations and stranded passengers when service is off.
     * 
     * @param wantOff Number of passengers wanting to get off
     * @param wantOn Number of passengers wanting to get on
     */
    public void makeStop(int wantOff, int wantOn)
    {
        int actualOff = Math.min(numPassengers, wantOff * serviceOn);
        numPassengers -= actualOff;

        totalViolations += (wantOff * (1 - serviceOn)) + (wantOff * serviceOn - actualOff);

        numAvailableSeats = numSeats - numPassengers;

        int boarded = Math.min(numAvailableSeats, wantOn * serviceOn);
        totalNumPassengers += boarded;
        numPassengers += boarded;

        numStrandedPassengers = (wantOn * (1 - serviceOn)) + (wantOn * serviceOn - boarded);
        totalNumStrandedPassengers += numStrandedPassengers;

        totalStopsMade++;
    }

    /**
     * Processes a stop and then turns the bus service off. 
     * Remaining passengers and waiting passengers are stranded.
     * 
     * @param wantOff Number of passengers wanting to get off
     * @param wantOn Number of passengers wanting to get on
     */
    public void makeStopAndTurnServiceOff(int wantOff, int wantOn)
    {
        int actualOff = Math.min(numPassengers, wantOff);
        numPassengers -= actualOff;
        totalViolations += wantOff - actualOff;

        numStrandedPassengers = numPassengers + wantOn;
        totalNumStrandedPassengers += numStrandedPassengers;

        numPassengers = 0;
        serviceOn = 0;

        totalStopsMade++;
    }

    /**
     * Processes a stop and then turns the bus service on. 
     * Any attempted offboarding while service was off counts as violations.
     * 
     * @param wantOff Number of passengers wanting to get off
     * @param wantOn Number of passengers wanting to get on
     */
    public void makeStopAndTurnServiceOn(int wantOff, int wantOn)
    {
        totalViolations += wantOff;

        serviceOn = 1;
        numStrandedPassengers = 0;

        numAvailableSeats = numSeats - numPassengers;

        int boarded = Math.min(numAvailableSeats, wantOn);
        totalNumPassengers += boarded;
        numPassengers += boarded;

        numStrandedPassengers = wantOn - boarded;
        totalNumStrandedPassengers += numStrandedPassengers;

        totalStopsMade++;
    }

    /**
     * Returns the current number of passengers on the bus.
     * 
     * @return Number of passengers currently on the bus
     */
    public int getNumPassengers()
    {
        return numPassengers;
    }

    /**
     * Returns the total number of passengers who have boarded the bus.
     * 
     * @return Total passengers who have boarded
     */
    public int getNumPassengersTotal()
    {
        return totalNumPassengers;
    }

    /**
     * Returns the total number of stops made so far.
     * 
     * @return Total stops made
     */
    public int getNumStopsMade()
    {
        return totalStopsMade;       
    }

    /**
     * Returns the total number of stranded passengers so far.
     * 
     * @return Total stranded passengers
     */
    public int getNumStranded()
    {
        return totalNumStrandedPassengers;    
    }

    /**
     * Returns the total number of violations so far.
     * 
     * @return Total violations
     */
    public int getNumViolations()
    {
        return totalViolations;
    }

    /**
     * Returns the name of the stop corresponding to the stop number, without padding characters.
     * 
     * @param stopNumber The index of the stop
     * @return Name of the stop without trailing '@' characters
     */
    public String stopNumberToName(int stopNumber)
    {
        String stopName = nameOfStops.substring(stopNumber * 8, stopNumber * 8 + 8);
        return stopName.replaceAll("@", ""); // removes trailing @ symbols
    }


    /**
     * Returns the current stop number for a looping route.
     * 
     * @return Stop number on a looping route
     */
    public int getLoopingStopNumber()
    {
        return totalStopsMade % numStopsOnRoute;
    }

    /**
     * Returns the current stop name for a looping route.
     * 
     * @return Stop name on a looping route
     */
    public String getLoopingStopName()
    {
        return stopNumberToName(getLoopingStopNumber());
    }

    /**
     * Returns the current stop number for a backing route.
     * 
     * @return Stop number on a backing route
     */
    public int getBackingStopNumber()
    {
        int cycle = 2 * (numStopsOnRoute - 1);          
        int posCycle = totalStopsMade % cycle;       

        return numStopsOnRoute - 1 - Math.abs((numStopsOnRoute - 1) - posCycle);
    }

    /**
     * Returns the current stop name for a backing route.
     * 
     * @return Stop name on a backing route
     */
    public String getBackingStopName()
    {
        return stopNumberToName(getBackingStopNumber());   
    }
}

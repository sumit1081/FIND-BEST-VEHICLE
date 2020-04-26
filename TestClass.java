import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
/*
Algorithm Steps:
Step 1:    Sort Cars by capacity.
Step 2:    Sort pumps by distance.  
Step 3:    Apply binary search between 0 and maxCapacity to find minRequiredCapacity.
        3a:  Compute mid.  Test if a capacity of mid allows journey to be completed. 
        3b:  If yes, then it is a possible answer, test for better solution.
        3c:  If no, then increase low and check at higher capacity.
Step 4:   Perform a linear search for capacity of vehicle more than minRequiredCapacity 
          and minimum cost.  

*/

class TestClass {
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] line = br.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int k = Integer.parseInt(line[1]);
        int s = Integer.parseInt(line[2]);
        int t = Integer.parseInt(line[3]);
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            line = br.readLine().split(" ");
            cars[i] = new Car(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
        }

        int[] pumps = new int[k + 2];
        line = br.readLine().split(" ");

        // Add a pump at 0 and another one at s.
        pumps[0] = 0;
        for (int i = 0; i < k; i++) {
            pumps[i + 1] = Integer.parseInt(line[i]);
        }
        pumps[k + 1] = s;

        // Sort both cars and pumps
        Arrays.sort(cars);
        Arrays.sort(pumps);

        int low = 0;
        int high = cars[cars.length - 1].capacity;

        // Handle the case when it is not possible to handle journey even with max
        // capacity.
        if (!isPossibleToCompleteJourney(high, pumps, t)) {
            System.out.println(-1);
            return;
        }
        int minRequiredCapacity = high;

        // Apply binary search from 0 to high. Find min Capacity required.
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (isPossibleToCompleteJourney(mid, pumps, t)) {
                minRequiredCapacity = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        // Find minimum cost with vehicles having minRequiredCapacity or more.
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].capacity >= minRequiredCapacity) {
                minCost = Math.min(minCost, cars[i].cost);
            }
        }
        System.out.println(minCost);
    }

    // This method computes if it is possible to reach the destination with a
    // capacity of fuel.
    public static boolean isPossibleToCompleteJourney(int fuel, int[] pumps, int t) {
        int minimumTime = 0;
        for (int i = 1; i < pumps.length; i++) {
            int distance = pumps[i] - pumps[i - 1];
            if (distance > fuel) // Not possible to complete journey
                return false;
            if (fuel >= 2 * distance) // Go in fast mode.
                minimumTime += distance;
            else {
                minimumTime += (3 * distance - fuel); // Go in slow and fast mode mixed.
            }
        }
        return minimumTime <= t;
    }
}

// Class to keep the cost and capacity in one unit. Also, have added a
// comparable for sorting using capacity.
class Car implements Comparable<Car> {
    int cost, capacity;

    Car(int c, int cp) {
        cost = c;
        capacity = cp;
    }

    public int compareTo(Car c) {
        return (this.capacity - c.capacity);
    }
}

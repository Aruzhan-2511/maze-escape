/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.ArrayList;
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int n = w.cells();
        int wallCount = w.count();
        DisjointSet dsu = new DisjointSet(n);
        int[] broken = new int[n-1];
        int index = 0;
        for (int k = 0; k < wallCount; k++){
            int a = w.a()[k];
            int b = w.b()[k];
            if (dsu.union(a,b)){
                broken[index] = k;
                index++;
            }
        }
        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.cells();
        int wallCount = w.count();
        DisjointSet dsu = new DisjointSet(n);
        Integer[] order = new Integer[wallCount];
        for (int i = 0; i < wallCount; i++){
            order[i] = i;
        }
        Arrays.sort(order,(a,b)->Integer.compare(w.cost()[a], w.cost()[b]));
        int[] broken = new int[n-1];
        int index = 0;
        for (int k : order){
            int a = w.a()[k];
            int b = w.b()[k];
            if(dsu.union(a,b)){
                broken[index] = k;
                index++;
                if (index == n-1){
                    break;
                }
            }
        }
        return broken;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        ArrayList<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int k = 0; k < w.count(); k++) {
            int a = w.a()[k];
            int b = w.b()[k];
            int cost = w.cost()[k];
            graph[a].add(new int[]{b, cost});
            graph[b].add(new int[]{a, cost});
        }
        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{0, 0});
        long totalCost = 0;
        int visitedCount = 0;
        while (!pq.isEmpty() && visitedCount < n) {
            int[] current = pq.poll();
            int vertex = current[0];
            int cost = current[1];
            if (visited[vertex]) {
                continue;
            }
            visited[vertex] = true;
            visitedCount++;
            totalCost += cost;
            for (int[] edge : graph[vertex]) {
                int next = edge[0];
                int weight = edge[1];
                if (!visited[next]) {
                    pq.add(new int[]{next, weight});
                }
            }
        }
        return totalCost;
    }
}

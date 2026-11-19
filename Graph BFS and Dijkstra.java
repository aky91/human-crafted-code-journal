/*


GRAPH ALGORITHMS REFERENCE

Vertices: 0 to n-1

BFS:
Shortest distance in an unweighted graph.

Dijkstra:
Shortest distance in a weighted graph (non-negative weights only).

------------------------------------------------------------
GRAPH REPRESENTATION VARIATIONS
------------------------------------------------------------

1. Unweighted graph, integer vertices:
List<List<Integer>> graph
graph.get(0) -> nbrs List<Integer>

2. Unweighted graph, string vertices eg: A, DEL, etc:
Map<String, List<String>> graph
graph.get("DEL") -> nbrs List<String>

3. Weighted graph, integer vertices:
List<List<Edge>> graph
record Edge(to, weight)
graph.get(0) -> nbrs List<Edge>

Alternative:
List<List<int[]>> graph
int[]{neighbor, weight}

------------------------------------------------------------
UNDIRECTED vs DIRECTED GRAPH
------------------------------------------------------------

Undirected:
Add both from -> to and to -> from.

Directed:
Add only from -> to.


*/
public class Main {
    private static final int INF = Integer.MAX_VALUE/2;
  
    record Edge(int to, int weight) {} //Weighted edge
    record State(int node, int distance) {} // Used by Dijkstra's priority queue
    
    public static void main(String[] args) {
        int n = 6;
        // Unweighted edges: {from, to}
        int[][] edges = {
            {0, 1},
            {1, 3},
            {3, 5},
            {4, 5},
            {0, 2},
            {0, 4},
            {2, 4}
        };

        // Weighted edges: {from, to, weight}
        int[][] weightedEdges = {
            {0, 1, 3},
            {1, 3, 2},
            {3, 5, 6},
            {4, 5, 1},
            {0, 2, 1},
            {0, 4, 4},
            {2, 4, 1}
        };
        
        List<List<Integer>> graph1 = buildGraph(n, edges);
        //bfs(n, graph, 0);
        
        List<List<Edge>> graph2 = buildWeightedGraph(n, weightedEdges);
        for(int i = 0; i < n; i++) {
            print(i + " ");
            println(graph2.get(i)) ;
        }
        dijkstra(n, graph2, 0);
    }

    // =========================================================
    // BFS - SHORTEST DISTANCE IN UNWEIGHTED GRAPH
    // =========================================================
    private static void bfs(int n, List<List<Integer>> graph, int source){
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        
        Set<Integer> visited = new HashSet<>();
        
        
        // VARIATION 1 - vanilla BFS
        
        //APPROACH 1
        // visited.add(0);
        // while(!queue.isEmpty()){
        //     int node = queue.poll();
        //     print(node);
        //     for(int nbr : graph.get(node)){
        //         if(!visited.contains(nbr)){
        //             queue.add(nbr);
        //             visited.add(nbr);
        //         }
        //     }
        // }
        
        //APPROACH 2 - correct but not space efficient, duplicates in queue
        // while(!queue.isEmpty()){
        //     int node = queue.poll();
        //     if(visited.contains(node)) continue;
        //     visited.add(node);
        //     print(node);
        //     for(int nbr : graph.get(node)){
        //         if(!visited.contains(nbr)){
        //             queue.add(nbr);
        //         }
        //     }
        // }
        
        // VARIATION 2 - Level order style
        visited.add(0);
        
        int burnDistance = 0;
        while(!queue.isEmpty()){
            int size = queue.size();
            print("burn : " + burnDistance);
            while(size-- > 0){
                int node = queue.poll();
                print(node);
                for(int nbr : graph.get(node)){
                    if(!visited.contains(nbr)){
                        queue.add(nbr);
                        visited.add(nbr);
                    }
                }
            }
            burnDistance++;
        }
        
        return;
    }
    
    private static void dijkstra(int n, List<List<Edge>> graph, int source){
        PriorityQueue<State> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.distance(), b.distance())); // IMPORTANT
        pq.offer(new State(source, 0));

        int[] distance = new int[n];
        Arrays.fill(distance, INF); // IMPORTANT
        distance[0] = 0;

        while(!pq.isEmpty()){
            State cur = pq.poll();
            int node = cur.node(), dist = cur.distance();
            if(dist > distance[node]) continue; // IMPORTANT skip long paths

            for(Edge edge : graph.get(node)){
                int nbr = edge.to(), w = edge.weight();
                int netDistance = dist + w; // IMPORTANT
                if(netDistance < distance[nbr]){ // IMPORTANT
                    distance[nbr] = netDistance;
                    pq.offer(new State(nbr, netDistance));
                }
            }
        }

        System.out.println(Arrays.toString(distance));
    }

    // =========================================================
    // GRAPH BUILDERS
    // =========================================================

    // Undirected, unweighted graph
    private static List<List<Integer>> buildGraph(int n, int[][] edges){
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < n; i++){
            graph.add(new ArrayList<>());
        }
        
        for(int[] edge : edges){
            int from = edge[0];
            int to = edge[1];
            graph.get(from).add(to);
            graph.get(to).add(from);
        }
        
        return graph;
    }
    
    // Undirected, weighted graph
    private static List<List<Edge>> buildWeightedGraph(int n, int[][] edges) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            graph.get(from).add(new Edge(to, weight));
            graph.get(to).add(new Edge(from, weight));
        }

        return graph;
    }
    
    private static void print(Object o){
        System.out.print(o);
    }
    private static void println(Object o){
        System.out.println(o);
    }
}

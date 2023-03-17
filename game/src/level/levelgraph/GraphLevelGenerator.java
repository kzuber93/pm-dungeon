package level.levelgraph;

import dslToGame.QuestConfig;
import dslToGame.graph.Graph;
import dslToGame.graph.Node;
import interpreter.DSLInterpreter;
import java.util.HashMap;
import level.elements.ILevel;
import level.generator.IGenerator;
import level.tools.DesignLabel;
import level.tools.LevelElement;
import level.tools.LevelSize;

/**
 * Generates a space-based level whose structure is defined by the given graph
 *
 * @quthor Andre Matutat
 */
public class GraphLevelGenerator implements IGenerator {

    private LevelNode root;
    private Graph<String> graph;
    public static HashMap<Node<String>, LevelNode> nodeToLevelNode;
    public static HashMap<LevelNode, Node<String>> levelNodeToNode;

    public GraphLevelGenerator(Graph<String> graph) {
        setGraph(graph);
    }

    /** Generate the GraphLevel from a random graph */
    public GraphLevelGenerator() {
        this(generateRandomDotGraph());
    }

    /**
     * The Root-Node defines the graph
     *
     * @param graph
     */
    public void setGraph(Graph<String> graph) {
        ConvertedGraph cg = DotToLevelGraph.convert(graph);
        root = cg.root();
        this.graph = cg.graph();
        nodeToLevelNode = cg.nodeToLevelNode();
        levelNodeToNode = cg.levelNodeToNode();
    }

    @Override
    public ILevel getLevel(DesignLabel designLabel, LevelSize size) {
        if (root == null)
            throw new NullPointerException("Root is null. Please add a graph to this generator");
        else return (ILevel) new GraphLevel(root, size, designLabel).getRootRoom();
    }

    @Override
    public LevelElement[][] getLayout(LevelSize size) {
        throw new UnsupportedOperationException("This Method is not supported for GraphLevel");
    }

    public Graph<String> getGraph() {
        return graph;
    }

    public HashMap<Node<String>, LevelNode> getNodeToLevelNode() {
        return nodeToLevelNode;
    }

    public HashMap<LevelNode, Node<String>> getLevelNodeToNode() {
        return levelNodeToNode;
    }

    /**
     * Generates a graph that can be used as a level, in the DOT-like representation of the DSL.
     *
     * @return the generated graph
     */
    public static Graph<String> generateRandomDotGraph() {
        String generatedDSLInput =
                "graph g {\n"
                        + "                        G -- T -- Q -- D\n"
                        + "                        Q -- F\n"
                        + "                        T -- X -- S\n"
                        + "                        G -- W -- E\n"
                        + "                        W -- C -- U\n"
                        + "                        C -- N\n"
                        + "                  }\n"
                        + "            quest_config c {\n"
                        + "                level_graph: g,\n"
                        + "                quest_points: 10,\n"
                        + "                quest_desc: \"Das Passwort findest du in der Breitensuche\",\n"
                        + "                password: \"GTWQXECDFSUN\"\n"
                        + "            }";
        DSLInterpreter interpreter = new DSLInterpreter();
        return ((QuestConfig) interpreter.getQuestConfig(generatedDSLInput)).levelGraph();
    }
}
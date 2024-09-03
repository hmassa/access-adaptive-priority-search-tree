import java.io.IOException;
import java.util.ArrayList;

public abstract class Test {
    protected SplayTree splayTree;
    protected RestructuringAAPST rest;
    protected BalancedBST bst;

    protected ArrayList<Comparable> queries;
    protected int numKeys;
    protected long numQueries;

    abstract void run();
    abstract void generateQueries();
    abstract void generateTrees();
    abstract void searchAndWrite() throws IOException;
}

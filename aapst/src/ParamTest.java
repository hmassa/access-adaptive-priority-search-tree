import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class ParamTest extends Test{
    private ArrayList<Integer> keys;
    private int[] queryKeys;
    private float p;
    private FileWriter fileFw;

    public ParamTest(float p) {
        this.p = p;
    }

    @Override
    void generateTrees() {
        keys = new ArrayList<>();
        for (int i = 1; i <= numKeys; i++) {
            keys.add(i);
        }

        bst = new BalancedBST(keys);
        rest = new RestructuringAAPST(keys);
        splayTree = new SplayTree();

        Collections.shuffle(keys);

        for (int i = 0; i < numKeys; i++){
            int queryVal = keys.get(i);
            splayTree.insert(queryVal);
        }
    }

    @Override
    void generateQueries() {
        Collections.shuffle(keys);

        queryKeys = new int[64];
        for (int i = 0; i < 64; i++){
            queryKeys[i] = keys.get(i);
        }

        numQueries = 1000000000L;
    }

    private int tossCoin() {
        int count = 0;
        long randint = lrand();

        while (randint%2 == 1) {
            count++;
            randint >>= 1;
        }

        return count;
    }

    private long lrand() {
        long r = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
        return r;
    }

    @Override
    public void searchAndWrite() throws IOException {
        int splayHold;
        int restHold;
        int bstHold;

        long splayTotal = 0;
        long restTotal = 0;
        long bstTotal = 0;

        int query;
        int random;

        for (int i = 0; i < numQueries; i++) {
            random = ThreadLocalRandom.current().nextInt(0, 100);
            if (random > p*100) {
                query = ThreadLocalRandom.current().nextInt(1, numKeys+1);
            } else {
                query = queryKeys[tossCoin()];
            }

            restHold = rest.find(query);
            bstHold = bst.find(query);
            splayHold = splayTree.find(query);

            if (restHold*bstHold*splayHold > 0) {
                restTotal += restHold;
                splayTotal += splayHold;
                bstTotal += bstHold;
            } else {
                System.out.println("error: " + query + " not in tree");
                return;
            }
        }

        float restAvg = (float)restTotal/(numQueries);
        float bstAvg = (float)bstTotal/(numQueries);
        float splayAvg = (float)splayTotal/(numQueries);

        String line = String.format("%d,%.2f,%.2f,%.2f\n", numKeys, restAvg, splayAvg, bstAvg);
        fileFw.write(line);
    }

    private void runForKeyset(int keySize) throws IOException{
        numKeys = keySize;

        generateTrees();
        generateQueries();
        searchAndWrite();
    }

    @Override
    void run() {
        try {
            File file = new File("../results/avg-" + (int) (p * 100) + ".txt");
            file.createNewFile();
            fileFw = new FileWriter(file);
            fileFw.write("DB Size,AAPST,Splay,BST\n");

            for (int ks = 1000; ks <= 1000000; ks *= 10) {
                runForKeyset(ks);
            }

            fileFw.flush();
            fileFw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

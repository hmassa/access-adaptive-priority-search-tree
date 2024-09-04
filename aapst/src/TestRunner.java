public class TestRunner {
    public static void main(String[] args) {
        Test splayTest = new SplayWorstCaseTest();
        splayTest.run();

        Test paramTest;
        for (float p = 0; p <= 1; p += 0.25) {
            paramTest = new ParamTest(p);
            paramTest.run();
        }
    }
}

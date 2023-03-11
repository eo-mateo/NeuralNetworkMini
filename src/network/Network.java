package network;

public class Network {
    //ctrl + alt + l = format kodu
    //sout + enter = System.out.print()
    //shift shift = otwiera wszystko co wpiszesz
    //ctrl + shift + f = szukanie w całym kodzie
    //w debugowaniu: F8 - idzie linijke dalej, F9 - idzie do nast. breakpointa
    //1. commit; 2. push
    //test commit
    //test commit 2
    public static int LAYERS_NUMBER = 3;
    public static int NEURONS_IN_LAYER = 4; //moze byc final
    public static int INPUTS_NUMBER = 4;
    public static int REPEAT_NUMBER = 10000;

//    int[] input;

    Neuron[][] neuron;

    public static void main(String[] args) {
        Network network = new Network();
        network.neuron = new Neuron[LAYERS_NUMBER][];

        // TWORZENIE WARSTW SIECI

        for (int j = 0; j < LAYERS_NUMBER; j++) {
            network.neuron[j] = new Neuron[NEURONS_IN_LAYER];
            for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                if (j == 0) {// FIRST LAYER SETUP
                    network.neuron[j][i] = new Neuron("firstLayer", INPUTS_NUMBER);
                    network.neuron[j][i].nexts = new Neuron[NEURONS_IN_LAYER];
                } else if (j == LAYERS_NUMBER - 1) { // LAST LAYER SETUP
                    network.neuron[j][i] = new Neuron("lastLayer", NEURONS_IN_LAYER);
                    network.neuron[j][i].inputs = new Neuron[NEURONS_IN_LAYER];
                } else { // MIDDLE LAYERS SETUP
                    network.neuron[j][i] = new Neuron("middleLayer", NEURONS_IN_LAYER);
                    network.neuron[j][i].inputs = new Neuron[NEURONS_IN_LAYER];
                    network.neuron[j][i].nexts = new Neuron[NEURONS_IN_LAYER];
                }
                network.neuron[j][i].row = i;
            }
        }
        // USTAWIAMY INPUTY I NEXTY
        for (int j = 0; j < LAYERS_NUMBER; j++) {
            for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                if (j == 0) {// FIRST LAYER SETUP
                    for (int p = 0; p < NEURONS_IN_LAYER; p++)  // USTAWIAMY NASTĘPNIKI
                        network.neuron[j][i].nexts[p] = network.neuron[j + 1][p];
                } else if (j == LAYERS_NUMBER - 1) { // LAST LAYER SETUP
                    for (int p = 0; p < NEURONS_IN_LAYER; p++)  // USTAWIAMY POPRZEDNIKI
                        network.neuron[j][i].inputs[p] = network.neuron[j - 1][p];
                } else { // MIDDLE LAYERS SETUP
                    for (int p = 0; p < NEURONS_IN_LAYER; p++) {// USTAWIAMY POPRZEDNIKI I NASTĘPNIKI
                        network.neuron[j][i].inputs[p] = network.neuron[j - 1][p];
                        network.neuron[j][i].nexts[p] = network.neuron[j + 1][p];
                    }
                }
            }
        }

        // MAPUJEMY OCZEKIWANE OUTPUTY
        network.neuron[LAYERS_NUMBER - 1][0].expected = 'l';
        network.neuron[LAYERS_NUMBER - 1][1].expected = 'j';
        network.neuron[LAYERS_NUMBER - 1][2].expected = '^';
        network.neuron[LAYERS_NUMBER - 1][3].expected = '_';

        // UCZYMY SIĘ!
        Data[] data = network.loadData();
        for (int repeat = 0; repeat < REPEAT_NUMBER; repeat++) {
            for (int j = 0; j < data.length; j++) {// Badamy wszystkie sety do uczenia wgrane w data
                // NA POCZĄTKU LICZYMY OUTPUTY DLA DANEGO ZBIORU INPUTÓW
                network.countOutput(network.neuron, data[j].input);

                for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                    if (network.neuron[2][i].expected == data[j].expected)
                        network.neuron[2][i].learn(data[j].input, 1);
                    else
                        network.neuron[2][i].learn(data[j].input, 0);
                }

                for (int i = 0; i < network.neuron[1].length; i++) {
                    //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[1][i].learn(data[j].input, -1);
                }
                for (int i = 0; i < network.neuron[0].length; i++) {
                    //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[0][i].learn(data[j].input, -1);
                }

                // AKTUALIZUJEMY WAGI
                for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                    for (int z = 0; z < network.neuron[2][i].weight.length; z++)
                        network.neuron[2][i].weight[z] += network.neuron[2][i].weightDelta[z];
                    network.neuron[2][i].bias += network.neuron[2][i].biasDelta;
                }
                for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                    for (int z = 0; z < network.neuron[1][i].weight.length; z++)
                        network.neuron[1][i].weight[z] += network.neuron[1][i].weightDelta[z];
                    network.neuron[1][i].bias += network.neuron[1][i].biasDelta;
                }
                for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                    for (int z = 0; z < network.neuron[0][i].weight.length; z++)
                        network.neuron[0][i].weight[z] += network.neuron[0][i].weightDelta[z];
                    network.neuron[0][i].bias += network.neuron[0][i].biasDelta;
                }
            }
        }

        double[] entrance = new double[]{   0.8, 0.07,
                                            0.7, 0.05};

        network.countOutput(network.neuron, entrance);

        System.out.println("PO UCZENIU, OUTPUTY W OSTATNIEJ WARSTWIE:");
        System.out.println("[2][0] " + network.neuron[2][0].output);
        System.out.println("[2][1] " + network.neuron[2][1].output);
        System.out.println("[2][2] " + network.neuron[2][2].output);
        System.out.println("[2][3] " + network.neuron[2][3].output);
    }

    public void countOutput(Neuron[][] neuron, double[] input) {

        // WARSTWA 0 - ustawiamy wejścia na warstwę 0
        for (int i = 0; i < neuron[0].length; i++) {
            for (int j = 0; j < neuron[0][i].input.length; j++)
                neuron[0][i].input[j] = input[j];
        }

        // WARSTWA 0
        for (int i = 0; i < neuron[0].length; i++) {
            neuron[0][i].getOutput(input);
        }

        // WARSTWA 1
        for (int i = 0; i < neuron[1].length; i++) {
            neuron[1][i].getOutput(neuron[1][i].inputs);
        }

        // WARSTWA 2
        for (int i = 0; i < NEURONS_IN_LAYER; i++) {
            neuron[2][i].getOutput(neuron[2][i].inputs);
        }
    }

    public Data[] loadData() {
        Data[] data = new Data[4];

        for (int i = 0; i < data.length; i++) {
            data[i] = new Data();
        }

        data[0].input = new double[]{   0, 1,
                                        0, 1};
        data[0].expected = 'l';

        data[1].input = new double[]{   1, 0,
                                        1, 0};
        data[1].expected = 'j';

        data[2].input = new double[]{   1, 1,
                                        0, 0};
        data[2].expected = '^';

        data[3].input = new double[]{   0, 0,
                                        1, 1};
        data[3].expected = '_';

        return data;
    }

    public void dataShuffle(Data[] data) {

   /*     List<Data> dataList = Arrays.asList(data);
        Collections.shuffle(dataList);
        dataList.toArray(data);

        System.out.println("INPUT 0 PO SHUFFLE:");
        for(int i=0;i<36;i++)
        {
            System.out.println(data[1].input[0][i]);
        }
        System.out.println("EXP:"+data[1].expected.toString());*/
    }
}

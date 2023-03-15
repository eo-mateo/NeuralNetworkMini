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
    public static int LAYERS_NUMBER = 4;
    public static int NEURONS_IN_LAYER = 6; //moze byc final
    public static int INPUTS_NUMBER = 30;
    public static int REPEAT_NUMBER = 50000;

//    int[] input;

    Neuron[][] neuron;

    public static void main(String[] args) {
        Network network = new Network();
        network.neuron = new Neuron[LAYERS_NUMBER][];

        // TWORZENIE WARSTW SIECI
        createNetworkLayer(network);

        // USTAWIAMY INPUTY I NEXTY
        setInputsAndNexts(network);

        // MAPUJEMY OCZEKIWANE OUTPUTY
        network.neuron[LAYERS_NUMBER - 1][0].expected = '1';
        network.neuron[LAYERS_NUMBER - 1][1].expected = '0';
        network.neuron[LAYERS_NUMBER - 1][2].expected = 'x';
        network.neuron[LAYERS_NUMBER - 1][3].expected = 'd';

        // UCZYMY SIĘ!
        Data[] data = network.loadData();
        startLearning(network, data);

        double[] entrance = new double[]{   0.08, 0.87, 0.04, 0.00, 0.00,
                                            0.70, 0.80, 0.00, 0.07, 0.00,
                                            0.07, 0.70, 0.09, 0.00, 0.30,
                                            0.00, 0.80, 0.00, 0.30, 0.40,
                                            0.70, 0.90, 0.70, 0.00, 0.40,
                                            0.00, 0.20, 0.20, 0.20, 0.00   };
        network.countOutput(network.neuron, entrance);

        printNetworkOutputs(network);
    }

    private static void printNetworkOutputs(Network network) {
        System.out.println("PO UCZENIU, OUTPUTY W OSTATNIEJ WARSTWIE:");
        System.out.println("[2][0] " + network.neuron[LAYERS_NUMBER - 1][0].output);
        System.out.println("[2][1] " + network.neuron[LAYERS_NUMBER - 1][1].output);
        System.out.println("[2][2] " + network.neuron[LAYERS_NUMBER - 1][2].output);
        System.out.println("[2][3] " + network.neuron[LAYERS_NUMBER - 1][3].output);
    }

    private static void startLearning(Network network, Data[] data) {
        System.out.println("Rozpoczęto naukę...");
        for (int repeat = 0; repeat < REPEAT_NUMBER; repeat++) {
            for (int j = 0; j < data.length; j++) {// Badamy wszystkie sety do uczenia wgrane w data
                // NA POCZĄTKU LICZYMY OUTPUTY DLA DANEGO ZBIORU INPUTÓW
                network.countOutput(network.neuron, data[j].input);

                for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                    if (network.neuron[LAYERS_NUMBER - 1][i].expected == data[j].expected)
                        network.neuron[LAYERS_NUMBER - 1][i].learn(data[j].input, 1);
                    else
                        network.neuron[LAYERS_NUMBER - 1][i].learn(data[j].input, 0);
                }

                for (int z = LAYERS_NUMBER-2; z >= 0 ; z--){
                    for (int i = 0; i < network.neuron[z].length; i++) {
                        //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                        network.neuron[z][i].learn(data[j].input, -1);
                    }
                }


                // AKTUALIZUJEMY WAGI

                for (int r = LAYERS_NUMBER-1; r>=0 ; r-- ) {
                    for (int i = 0; i < NEURONS_IN_LAYER; i++) {
                        for (int z = 0; z < network.neuron[2][i].weight.length; z++)
                            network.neuron[r][i].weight[z] += network.neuron[r][i].weightDelta[z];
                        network.neuron[r][i].bias += network.neuron[r][i].biasDelta;
                    }
                }
            }

            // PROGRESS BAR
            float temp =  (float)repeat / (float)REPEAT_NUMBER * 100;
            if (temp % 5 == 0)
                  System.out.print("*");
        }
        System.out.println();
    }

    private static void setInputsAndNexts(Network network) {
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
    }

    private static void createNetworkLayer(Network network) {
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
    }

    public void countOutput(Neuron[][] neuron, double[] input) {

        // FIRST LAYER - SET INPUTS
        for (int i = 0; i < neuron[0].length; i++) {
            for (int j = 0; j < neuron[0][i].input.length; j++)
                neuron[0][i].input[j] = input[j];
        }

        // FIRST LAYER
        for (int i = 0; i < neuron[0].length; i++) {
            neuron[0][i].getOutput(input);
        }

        // MIDDLE LAYERS
        for (int z = 1; z < LAYERS_NUMBER-1; z++) {
            for (int i = 0; i < neuron[z].length; i++) {
                neuron[z][i].getOutput(neuron[z][i].inputs);
            }
        }

        // LAST LAYER
        for (int i = 0; i < NEURONS_IN_LAYER; i++) {
            neuron[LAYERS_NUMBER-1][i].getOutput(neuron[LAYERS_NUMBER-1][i].inputs);
        }
    }

    public Data[] loadData() {
        Data[] data = new Data[7];

        for (int i = 0; i < data.length; i++) {
            data[i] = new Data();
        }

        data[0].input = new double[]{   0, 0, 0, 1, 0,
                                        0, 0, 1, 1, 0,
                                        0, 1, 1, 1, 0,
                                        0, 0, 1, 1, 0,
                                        0, 0, 1, 1, 0,
                                        0, 1, 1, 1, 1  };
        data[0].expected = '1';

        data[1].input = new double[]{   0, 0, 1, 1, 0,
                                        0, 1, 1, 1, 0,
                                        1, 0, 1, 1, 0,
                                        0, 0, 1, 1, 0,
                                        0, 0, 1, 1, 0,
                                        0, 1, 1, 1, 1  };
        data[1].expected = '1';

        data[2].input = new double[]{   0, 0, 1, 0, 0,
                                        0, 1, 1, 0, 0,
                                        1, 0, 1, 0, 0,
                                        0, 0, 1, 0, 0,
                                        0, 0, 1, 0, 0,
                                        0, 1, 1, 1, 0  };
        data[2].expected = '1';

        data[3].input = new double[]{   0, 0, 1, 1, 0,
                                        0, 1, 0, 0, 1,
                                        0, 1, 0, 0, 1,
                                        0, 1, 0, 0, 1,
                                        0, 1, 0, 0, 1,
                                        0, 0, 1, 1, 0  };
        data[3].expected = '0';

        data[4].input = new double[]{   0, 1, 1, 0, 0,
                                        1, 0, 0, 1, 0,
                                        1, 0, 0, 1, 0,
                                        1, 0, 0, 1, 0,
                                        1, 0, 0, 1, 0,
                                        0, 1, 1, 0, 0};
        data[4].expected = '0';

        data[5].input = new double[]{   0, 1, 1, 1, 0,
                                        0, 1, 0, 1, 0,
                                        0, 1, 0, 1, 0,
                                        0, 1, 0, 1, 0,
                                        0, 1, 0, 1, 0,
                                        0, 1, 1, 1, 0};
        data[5].expected = '0';

        data[6].input = new double[]{   0, 1, 1, 1, 0,
                                        1, 1, 0, 1, 1,
                                        1, 1, 0, 1, 1,
                                        1, 1, 0, 1, 1,
                                        1, 1, 0, 1, 1,
                                        0, 1, 1, 1, 0};
        data[6].expected = '0';

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

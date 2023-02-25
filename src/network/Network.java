package network;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Network {
    //ctrl + alt + l = format kodu
    //sout + enter = System.out.print()
    //shift shift = otwiera wszystko co wpiszesz
    //ctrl + shift + f = szukanie w całym kodzie
    //w debugowaniu: F8 - idzie linijke dalej, F9 - idzie do nast. breakpointa
    //1. commit; 2. push
    //test commit
    //test commit 2
    public static int perceptronsNumber = 4; //moze byc final
    int[] input;

    Perceptron[][] neuron;

    public static void main(String[] args) {

        Network network = new Network();
        //   System.out.println("START");

        // TWORZENIE WARSTW SIECI
        network.neuron = new Perceptron[3][];
        network.neuron[0] = new Perceptron[perceptronsNumber];
        network.neuron[1] = new Perceptron[perceptronsNumber];
        network.neuron[2] = new Perceptron[perceptronsNumber];


        // WARSTWA 1
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[0][i] = new Perceptron("firstLayer",16);
            network.neuron[0][i].row = i;
            network.neuron[0][i].run();
            network.neuron[0][i].inputs = new Perceptron[perceptronsNumber];
        }

        // WARSTWA 2
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[1][i] = new Perceptron("middleLayer",4);
            network.neuron[1][i].row = i;
            network.neuron[1][i].run();
            network.neuron[1][i].inputs = new Perceptron[perceptronsNumber];

            // USTAWIAMY INPUTY Z POPRZEDNIKÓW
            for (int j = 0; j < 4; j++) {
                network.neuron[1][i].inputs[j] = network.neuron[0][j];
            }
        }

        // WARSTWA 3
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[2][i] = new Perceptron("lastLayer",4);
            network.neuron[2][i].row = i;
            network.neuron[2][i].run();
            network.neuron[2][i].inputs = new Perceptron[perceptronsNumber];

            // USTAWIAMY INPUTY Z POPRZEDNIKÓW
            for (int j = 0; j < 4; j++) {
                network.neuron[2][i].inputs[j] = network.neuron[1][j];
            }
        }


        // WARSTWA 1: USTAWIAMY NEXTY
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[0][i].nexts = new Perceptron[perceptronsNumber];
            // USTAWIAMY NEXTY
            for (int j = 0; j < 4; j++) {
                network.neuron[0][i].nexts[j] = network.neuron[1][j];
                network.neuron[0][i].run();
            }
        }

        // WARSTWA 2: USTAWIAMY NEXTY
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[1][i].nexts = new Perceptron[perceptronsNumber];
            // USTAWIAMY NEXTY
            for (int j = 0; j < 4; j++) {
                network.neuron[1][i].nexts[j] = network.neuron[2][j];
                network.neuron[1][i].run();
            }
        }


        System.out.println("PRZED UCZENIEM, OUTPUT W OSTATNIEJ WARSTWIE:");
        for (int i=0;i<network.neuron[2].length;i++)
        {
            System.out.println(network.neuron[2][i].output);
        }



        // UCZYMY SIĘ!

        Data[] data= network.loadData();
        System.out.println("PIERWSZY OUTPUT\n");
        network.countOutput(network.neuron);

     //   for(int repeat=0;repeat<10;repeat++) {
            for (int j = 0; j < 4; j++) // Badamy wszystkie sety do uczenia wgrane w data
            {
                for (int i = 0; i < perceptronsNumber; i++) {
                   // network.startLearn(network.neuron[2][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[2][i].learn(data[j].input[i], data[j].expected[i]);
                }

                for (int i = 0; i < perceptronsNumber; i++) {
                  //  network.startLearn(network.neuron[1][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[1][i].learn(data[j].input[i], data[j].expected[i]);

                }

                for (int i = 0; i < perceptronsNumber; i++) {
                  //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[0][i].learn(data[j].input[i], data[j].expected[i]);

                }
            }
       // }

     //   network.dataShuffle(data);


   /*     System.out.println("neuron B, d:"+network.neuron[1][1].d);
        System.out.println("PO UCZENIU, WAGI W OSTATNIEJ WARSTWIE:");
        for (int i=0;i<network.neuron[1][1].weight.length;i++)
        {
            System.out.println(network.neuron[1][1].weight[i]);
        } */


        network.countOutput(network.neuron);



    }

    public void startLearn(Perceptron perceptron, double[] input, int expected) {

        // NAUKA Z INPUTÓW

        perceptron.learn(input,expected);

    }

    public void countOutput(Perceptron[][] neuron) {

        input = new int[]  {0, 0, 1, 0,
                            0, 0, 1, 0,
                            1, 1, 1, 1,
                            0, 0, 1, 0};

        // WARSTWA 0 - ustawiamy wejścia na warstwę 0
        for (int i = 0; i < 4; i++) {
            for(int j=0;j<16;j++)
                neuron[0][i].input[j]=input[i];
        }

        // WARSTWA 0
        for (int i = 0; i < 4; i++) {
            neuron[0][i].getOutput(input);
        }

        // WARSTWA 1
        for (int i = 0; i < perceptronsNumber; i++) {
            double[] tradere = new double[neuron[1][i].inputs.length];
            for (int j = 0; j < neuron[1][i].inputs.length; j++) {
                tradere[i] = neuron[1][i].inputs[j].output;
            }
            neuron[1][i].getOutput(tradere);
        }

        // WARSTWA 2
        for (int i = 0; i < perceptronsNumber; i++) {
            double[] tradere = new double[neuron[2][i].inputs.length];
            for (int j = 0; j < neuron[2][i].inputs.length; j++) {
                tradere[i] = neuron[2][i].inputs[j].output;
            }
            neuron[2][i].getOutput(tradere);
        }


        System.out.println("\n WYNIK ROW 0:");
        for(int i=0;i<4;i++) System.out.println(i+". "+neuron[0][i].output);
        System.out.println("\n WYNIK ROW 1:");
        for(int i=0;i<4;i++) System.out.println(i+". "+neuron[1][i].output);
        System.out.println("\n WYNIK ROW 2:");
        for(int i=0;i<4;i++) System.out.println(i+". "+neuron[2][i].output);

    }

    public Data[] loadData() {
        Data data[] = new Data[8];

        for(int z=0;z<4;z++){
            data[z] = new Data();
            data[z].input = new double[perceptronsNumber][];

            for (int j=0;j<perceptronsNumber;j++){
                data[z].input[j] = new double[9];
            }
            data[z].expected = new int[perceptronsNumber];
        }


        for(int i=0;i<perceptronsNumber;i++) {
            data[0].input[i] = new double[]{0, 0, 1, 0,
                                            0, 0, 1, 0,
                                            1, 1, 1, 1,
                                            0, 0, 1, 0};
            if(i==0)
                data[0].expected[i]=1;
            else
                data[0].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[1].input[i] = new double[]{0, 0, 0, 0,
                                            1, 1, 1, 1,
                                            0, 0, 0, 0,
                                            0, 0, 0, 0};
            if(i==1)
                data[1].expected[i]=1;
            else
                data[1].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[2].input[i] = new double[]{1, 0, 0, 0,
                                            0, 1, 0, 0,
                                            0, 0, 1, 0,
                                            0, 0, 0, 1};
            if(i==2)
                data[2].expected[i]=1;
            else
                data[2].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[3].input[i] = new double[]{0, 0, 0, 1,
                                            0, 0, 1, 0,
                                            0, 1, 0, 0,
                                            1, 0, 0, 0};
            if(i==3)
                data[3].expected[i]=1;
            else
                data[3].expected[i]=0;
        }
/*
        for(int i=0;i<perceptronsNumber;i++) {
            data[4].input[i] = new double[]{1, 1, 1,
                                            0, 0, 0,
                                            1, 1, 1};
            data[4].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[5].input[i] = new double[]{1, 1, 1,
                                            0, 1, 0,
                                            1, 1, 1};
            data[5].expected[i]=0;
        }
        for(int i=0;i<perceptronsNumber;i++) {
            data[6].input[i] = new double[]{0, 0, 0,
                                            0, 1, 0,
                                            0, 0, 0};
            data[6].expected[i]=0;
        }

 */

       return data;
    }

    public void dataShuffle(Data[] data) {

        List<Data> dataList = Arrays.asList(data);
        Collections.shuffle(dataList);
        dataList.toArray(data);

        System.out.println("INPUT 0 PO SHUFFLE:");
        for(int i=0;i<36;i++)
        {
            System.out.println(data[1].input[0][i]);
        }
        System.out.println("EXP:"+data[1].expected.toString());
    }
}

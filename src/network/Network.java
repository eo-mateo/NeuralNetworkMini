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
            network.neuron[0][i] = new Perceptron("firstLayer", 4);
            network.neuron[0][i].row = i;
            network.neuron[0][i].inputs = new Perceptron[perceptronsNumber];
        }

        // WARSTWA 2
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[1][i] = new Perceptron("middleLayer",4);
            network.neuron[1][i].row = i;
            network.neuron[1][i].inputs = new Perceptron[perceptronsNumber];

            // USTAWIAMY INPUTY Z POPRZEDNIKÓW
            for (int j = 0; j < 4; j++) {
                network.neuron[1][i].inputs[j] = network.neuron[0][j];
            }
        }

        // WARSTWA 3
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[2][i] = new Perceptron("lastLayer", 4); // było neuron[2][i]
            network.neuron[2][i].row = i;
            network.neuron[2][i].inputs = new Perceptron[perceptronsNumber];

            // USTAWIAMY INPUTY Z POPRZEDNIKÓW
            for (int j = 0; j < network.neuron[1][i].inputs.length; j++) {
                network.neuron[2][i].inputs[j] = network.neuron[1][j];
            }
        }
        network.neuron[2][0].expected = 'l';
        network.neuron[2][1].expected = 'j';
        network.neuron[2][2].expected = '^';
        network.neuron[2][3].expected = '_';
        //    network.neuron[1][2].expected = 'X';
        //    network.neuron[1][3].expected = '/';


        // WARSTWA 1: USTAWIAMY NEXTY
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[0][i].nexts = new Perceptron[perceptronsNumber];
            // USTAWIAMY NEXTY
            for (int j = 0; j < network.neuron[0][i].nexts.length; j++) {
                network.neuron[0][i].nexts[j] = network.neuron[1][j];
            }
        }

        // USTAWIAMY NA SZTYWNO WAGI - TEST

    /*    network.neuron[0][0].weight[0] = 0.15f;
        network.neuron[0][0].weight[1] = 0.20f;
        network.neuron[0][0].bias = 0.35f;

        network.neuron[0][1].weight[0] = 0.25f;
        network.neuron[0][1].weight[1] = 0.30f;
        network.neuron[0][1].bias = 0.35f;

        network.neuron[1][0].weight[0] = 0.40f;
        network.neuron[1][0].weight[1] = 0.45f;
        network.neuron[1][0].bias = 0.60f;

        network.neuron[1][1].weight[0] = 0.50f;
        network.neuron[1][1].weight[1] = 0.55f;
        network.neuron[1][1].bias = 0.60f; */


       // WARSTWA 2: USTAWIAMY NEXTY
        for (int i = 0; i < perceptronsNumber; i++) {
            network.neuron[1][i].nexts = new Perceptron[perceptronsNumber];
            // USTAWIAMY NEXTY
            for (int j = 0; j < 4; j++) {
                network.neuron[1][i].nexts[j] = network.neuron[2][j];
            }
        }


        System.out.println("PRZED UCZENIEM, OUTPUT W OSTATNIEJ WARSTWIE:");

        double[] input = new double[]{0.8, 0.1,
                                      0.7,0.05};
        network.countOutput(network.neuron, input);

        for (int i = 0; i < network.neuron[2].length; i++) {
            System.out.println(network.neuron[2][i].output);
        }
// DO TEJ PORY DZIAŁA - FORWARD JEST OK


        // UCZYMY SIĘ!

        Data[] data = network.loadData();
        //     System.out.println("PIERWSZY OUTPUT\n");


        for (int repeat = 0; repeat < 10000; repeat++) {
            for (int j = 0; j < data.length; j++) // Badamy wszystkie sety do uczenia wgrane w data
            {
                // NA POCZĄTKU LICZYMY OUTPUTY DLA DANEGO ZBIORU INPUTÓW

                network.countOutput(network.neuron, data[j].input);



                for (int i = 0; i < perceptronsNumber; i++) {
                // network.startLearn(network.neuron[2][i], data[j].input[i], data[j].expected[i]);
                      if(network.neuron[2][i].expected==data[j].expected)
                            network.neuron[2][i].learn(data[j].input, 1); // było: (..., 1);
                     else
                            network.neuron[2][i].learn(data[j].input, 0); // SPRAWDZIĆ, dlaczego po pierwszej iteracji, która poprawnie zmienia wagi, mamy próbę odwrotnych wag
                }

               /* for (int i = 0; i < perceptronsNumber; i++) {
                  //  network.startLearn(network.neuron[1][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[1][i].learn(data[j].input, -1);

                } */

                for (int i = 0; i < network.neuron[1].length; i++) {
                    //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[1][i].learn(data[j].input, -1);
                }
                for (int i = 0; i < network.neuron[0].length; i++) {
                    //  network.startLearn(network.neuron[0][i], data[j].input[i], data[j].expected[i]);
                    network.neuron[0][i].learn(data[j].input, -1);
                }

                // AKTUALIZUJEMY WAGI


                for (int i = 0; i < perceptronsNumber; i++) {
                    for (int z = 0; z < network.neuron[2][i].weight.length; z++)
                        network.neuron[2][i].weight[z] += network.neuron[2][i].weightDelta[z];
                    // System.out.println("N2 i"+i+": "+network.neuron[2][i].weightDelta);
                    network.neuron[2][i].bias += network.neuron[2][i].biasDelta;
                }
                for (int i = 0; i < perceptronsNumber; i++) {
                    for (int z = 0; z < network.neuron[1][i].weight.length; z++)
                        network.neuron[1][i].weight[z] += network.neuron[1][i].weightDelta[z];
                    // System.out.println("N2 i"+i+": "+network.neuron[2][i].weightDelta);
                    network.neuron[1][i].bias += network.neuron[1][i].biasDelta;
                }
          /*      for (int i = 0; i < perceptronsNumber; i++)
                    for (int z=0;z<network.neuron[1][i].weight.length;z++)
                        network.neuron[1][i].weight[z]+=network.neuron[1][i].weightDelta[z]; */

                for (int i = 0; i < perceptronsNumber; i++) {
                    for (int z = 0; z < network.neuron[0][i].weight.length; z++)
                        network.neuron[0][i].weight[z] += network.neuron[0][i].weightDelta[z];
                    network.neuron[0][i].bias += network.neuron[0][i].biasDelta;
                }


/*
                for (int i = 0; i < perceptronsNumber; i++) {
                    for (int z = 0; z < network.neuron[0][i].weight.length; z++)
                        network.neuron[0][i].weight[z] += network.neuron[0][i].weightDelta[z];
                    network.neuron[0][i].bias += network.neuron[0][i].biasDelta;
                } */


            }
        }

        //   network.dataShuffle(data);


        /*     System.out.println("neuron B, d:"+network.neuron[1][1].d);*/
        System.out.println("PO UCZENIU, WAGI W UKRYTEJ WARSTWIE:");

        System.out.println("[0][0]w0 " + network.neuron[0][0].weight[0]);
        System.out.println("[0][0]w1 " + network.neuron[0][0].weight[1]);
        System.out.println("[0][1]w0 " + network.neuron[0][1].weight[0]);
        System.out.println("[0][1]w1 " + network.neuron[0][1].weight[1]);


        double[] entrance = new double[]{0.8, 0.7,
                                         0.07,0.05};;//,
        //1, 1};
        network.countOutput(network.neuron, entrance);

        System.out.println("PO UCZENIU, OUTPUTY W OSTATNIEJ WARSTWIE:");
        System.out.println("[2][0] " + network.neuron[2][0].output);
        System.out.println("[2][1] " + network.neuron[2][1].output);
        System.out.println("[2][2] " + network.neuron[2][2].output);
        System.out.println("[2][3] " + network.neuron[2][3].output);

    }

    public void startLearn(Perceptron perceptron, double[] input, int expected) {

        // NAUKA Z INPUTÓW

        perceptron.learn(input, expected);

    }

    public void countOutput(Perceptron[][] neuron, double[] input) {

        // WARSTWA 0 - ustawiamy wejścia na warstwę 0
        for (int i = 0; i < neuron[0].length; i++) { // było i<4
            for (int j = 0; j < neuron[0][i].input.length; j++) // !!!!!!!!!!!!!!!!!!!! było 4
                neuron[0][i].input[j] = input[j];
        }

        // WARSTWA 0
        for (int i = 0; i < neuron[0].length; i++) {
            neuron[0][i].getOutput(input);
        }

        // WARSTWA 1
        for (int i = 0; i < neuron[1].length; i++) {
            //    double[] tradere = new double[neuron[1][i].inputs.length];
            //     for (int j = 0; j < neuron[1][i].inputs.length; j++) {
            //  tradere[i] = neuron[1][i].inputs[j].output;
            neuron[1][i].getOutput(neuron[1][i].inputs);
            //     }

        }

       // WARSTWA 2
        for (int i = 0; i < perceptronsNumber; i++) {
          /*  double[] tradere = new double[neuron[2][i].inputs.length];
            for (int j = 0; j < neuron[2][i].inputs.length; j++) {
                tradere[i] = neuron[2][i].inputs[j].output;
            }
            neuron[2][i].getOutput(tradere); */
            neuron[2][i].getOutput(neuron[2][i].inputs);
        }


   /*     System.out.println("\n\nOUTPUT: \n WYNIK ROW 0:");
        for(int i=0;i<2;i++) System.out.println(i+". "+neuron[0][i].output);
        System.out.println("\n WYNIK ROW 1:");
        for(int i=0;i<2;i++) System.out.println(i+". "+neuron[1][i].output); */
        //    System.out.println("\n WYNIK ROW 2:");
        //  for(int i=0;i<4;i++) System.out.println(i+". "+neuron[2][i].output);

    }

    public Data[] loadData() {
        Data data[] = new Data[4];

        for (int z = 0; z < data.length; z++) {
            data[z] = new Data();
            //        data[z].input = new double[perceptronsNumber];

            //       for (int j=0;j<perceptronsNumber;j++){
            //         data[z].input[j] = new double[9];
            //   }
            //      data[z].expected = new char;
        }


        //   for(int i=0;i<perceptronsNumber;i++) {
        data[0].input = new double[]{0, 1,
                                     0, 1};
        //1, 1};
        //       if(i==0)
        data[0].expected = 'l';
        //        else
        //          data[0].expected[i]=0;
        //       }

        for(int i=0;i<perceptronsNumber;i++) {
            data[1].input = new double[]   {1, 0,
                                            1, 0};
      //      if(i==1)
                data[1].expected='j';
     //       else
     //           data[1].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[2].input = new double[]   {1, 1,
                                            0, 0};
       //     if(i==2)
                data[2].expected='^';
        //    else
        //        data[2].expected[i]=0;
        }

        for(int i=0;i<perceptronsNumber;i++) {
            data[3].input = new double[]   {0, 0,
                                            1, 1 };
       //     if(i==3)
                data[3].expected='_';
       //     else
       //         data[3].expected[i]=0;
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

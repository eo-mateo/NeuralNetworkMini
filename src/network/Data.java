package network;

import static network.Network.NEURONS_IN_LAYER;

public class Data {
  //  int perceptronsNumber = 8;
    double[] input;
    char expected;

  public static void main(String[] args) {
    Data data = new Data();
    data.input = new double[NEURONS_IN_LAYER];
   // for (int i=0;i<perceptronsNumber;i++){
      data.input = new double[2];
 //   }
 //   data.expected = new Character();
  }

}

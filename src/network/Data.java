package network;

import static network.Network.perceptronsNumber;

public class Data {
  //  int perceptronsNumber = 8;
    double[][] input;
    int[] expected;

  public static void main(String[] args) {
    Data data = new Data();
    data.input = new double[perceptronsNumber][];
    for (int i=0;i<perceptronsNumber;i++){
      data.input[i] = new double[36];
    }
    data.expected = new int[perceptronsNumber];
  }

}

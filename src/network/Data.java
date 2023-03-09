package network;

import static network.Network.perceptronsNumber;

public class Data {
  //  int perceptronsNumber = 8;
    double[] input;
    float expected;

  public static void main(String[] args) {
    Data data = new Data();
    data.input = new double[perceptronsNumber];
   // for (int i=0;i<perceptronsNumber;i++){
      data.input = new double[2];
 //   }
 //   data.expected = new Character();
  }

}

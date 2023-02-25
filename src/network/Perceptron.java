package network;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.Objects;

public class Perceptron {

    int[] input;
    Perceptron inputs[];
    Perceptron nexts[];
    float[] weight;
    float[] weightDelta;
    String mode;

    double output;
    double d = 0;
    int row;
    int value;
    static int extInputsNumber = 9; //

    public
    Perceptron(String mode, int weightAndInputNumber) {

        this.mode = mode;

        if("firstLayer".equals(this.mode))
        {
            input = new int[weightAndInputNumber];
        }

        this.weight = new float[weightAndInputNumber];
        this.weightDelta = new float[weightAndInputNumber];

        // POCZĄTKOWE USTALENIE WAG
        for (int i = 0; i < weightAndInputNumber; i++) {
            double val = (Math.random() * 2) - 1;
            this.weight[i] = (float) val;
            this.weightDelta[i]=0;
        }
    }

    public Perceptron() {

    }


    public static void main(String[] args) {

    }

    public void run() {
        //   Perceptron perceptron = new Perceptron();
        //  perceptron.weight = new float[extInputsNumber];
        //  perceptron.d = new float[inputsNumber];



        //     System.out.println("Jesteśmy w main Perceptronu");


    }

    float sum = (float) 0;

    public double getOutput(int[] input) {
        double[] temp = new double[input.length];

        for (int i = 0; i < input.length; i++) {
//            temp[i] = new Double((double) input[i]);
            temp[i] = input[i];
     //       System.out.println("getOutput, int: "+input[i]);
        }
        return getOutput(temp);
    }

    public double getOutput(double[] input) {

        float value = 0;
        //    output=0;
        for (int i = 0; i < input.length; i++) {
            value += input[i] * this.weight[i];
            sum += input[i];
        }
        output = sigmoid(value);
        return output;

    }

    public double getOutput(Perceptron[] input) {

        double[] tradere = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            tradere[i] = input[i].output;
        }
        return getOutput(tradere);
    }


    private static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private static double sigmoidPrim(double x) {
    /*    if((x>0.50)||(x<-0.50))
            return 0;
        else */
            return Math.exp(-x) / Math.pow(1 + Math.exp(-x),2);
    }

    public void learn(double[] input, double expected) {

        double correction;
        double q = 1.5;
        double sum = 0;
        this.d = (float) 0;
        for (int i=0 ; i< this.weightDelta.length;i++)
            this.weightDelta[i] =0;

        // OBLICZAMY S, CZYLI SUMĘ WEJŚĆ RAZY WAGI
        if ("firstLayer".equals(this.mode)) {
            for (int i = 0; i < this.weight.length; i++) {
                sum += input[i] * this.weight[i];
            }
        }
        if (("lastLayer".equals(this.mode))||("middleLayer".equals(this.mode))) {
            for (int i = 0; i < this.inputs.length; i++) {
                sum += this.inputs[i].output * this.weight[i];

            }
        }

        // OBLICZAMY D, CZYLI WSPÓŁCZYNNIK POMOCNICZY BŁĘDU
        // OBLICZAMY WAGI


        for (int i = 0; i < this.weight.length; i++) {
            System.out.print(this.mode+" "+this.row+i+" Waga przed: "+this.weight[i]);
            if ("lastLayer".equals(this.mode))
                {
                this.d = sigmoidPrim(sum) * (expected - output);
                this.weightDelta[i] += q * this.d * this.inputs[i].output; // Było bezpośrednio "weight"
//                System.out.println("| d: "+this.d+" th.input:"+this.inputs[i].output+" sigmPrim: "+sigmoidPrim(sum)+" d.nexts0: "+this.nexts[1].d+" th.next.weig: "+this.nexts[0].weight[this.row]);

                    if(("lastLayer".equals(this.mode))&&(this.row==0))
                    {
                    //    System.out.print("d. sum: "+sum+" sigmPrim: "+sigmoidPrim(sum)+" exp: "+expected+" output: "+output+" d: "+d+" nowa waga: "+this.weight[i]);
                    }

            } else {
                this.d = 0;

                for (int j = 0; j < 4; j++) {
                    this.d += sigmoidPrim(sum) * this.nexts[j].d * this.nexts[j].weight[this.row];
                }
                if("firstLayer".equals(this.mode))
                {
                    this.weightDelta[i] += q * this.d * this.input[i]; // Było bezpośrednio
                 //   System.out.println("| d: "+this.d+" th.input:"+this.input+": "+this.input[i]+" sigmPrim: "+sigmoidPrim(sum)+" d.nexts0: "+this.nexts[1].d+" th.next.weig: "+this.nexts[0].weight[this.row]);
                }
                else
                {
                    this.weightDelta[i] += q * this.d * this.inputs[i].output; // Było bezpośrednio
                    System.out.println("| d: "+this.d+" th.input: "+this.inputs[i].output+" sigmPrim: "+sigmoidPrim(sum)+" d.nexts0: "+this.nexts[0].d+" th.next.weig: "+this.nexts[0].weight[this.row]);
                }

            }
           // System.out.println(this.mode+this.row+"  dW"+i+": "+this.weightDelta[i]);
        }


    }

    public void getWeights() {
        for (int i = 0; i < this.weight.length; i++) {
            System.out.println("W" + i + ": " + this.weight[i]);
        }
    }


}

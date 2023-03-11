package network;

public class Neuron {

    double[] input;
    char expected;
    Neuron inputs[];
    Neuron nexts[];
    float[] weight;
    float[] weightDelta;
    float bias;
    float biasDelta;
    String mode;

    double output;
    float sum = (float) 0;
    double d = 0;
    int row;
    int value;
    static int extInputsNumber = 9; //


    public Neuron(String mode, int weightAndInputNumber) {

        this.mode = mode;

        if("firstLayer".equals(this.mode)) {
            input = new double[weightAndInputNumber];
        }

        this.weight = new float[weightAndInputNumber];
        this.weightDelta = new float[weightAndInputNumber];
        this.expected='0';

        // POCZĄTKOWE USTALENIE WAG
        for (int i = 0; i < weightAndInputNumber; i++) {
            double val = (Math.random() * 2) - 1;
            this.weight[i] = (float) val;
            this.weightDelta[i]=0;
        }
        this.bias = 0;
        this.biasDelta = 0;

    }

    public Neuron() {

    }


    public static void main(String[] args) {

    }


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
        value+=this.bias; // Ostatnia waga: bias

        output = sigmoid(value);
        return output;

    }

    public double getOutput(Neuron[] input) {

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
        double q = 0.5;
        double sum = 0;
        this.d = (float) 0;
        for (int i=0 ; i< this.weightDelta.length;i++)
            this.weightDelta[i] =0;
        this.biasDelta=0;
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
        sum += this.bias; // Dodajemy ostatnią wagę: bias

        // OBLICZAMY D, CZYLI WSPÓŁCZYNNIK POMOCNICZY BŁĘDU
        // OBLICZAMY WAGI


        for (int i = 0; i < this.weight.length; i++) {
            if ("lastLayer".equals(this.mode)) {
                double deriv = expected-output;
                this.d = sigmoidPrim(sum) * (expected-output);
                this.weightDelta[i] = (float)(q*this.d * this.inputs[i].output); // Było jeszcze *q i "+="
                double wyjsc = this.weight[i]+this.weightDelta[i];
                System.out.println("this.weightDelta["+i+"]="+this.weightDelta[i]+"; q * "+this.d+"* this.inputs[i].output: "+this.inputs[i].output+"; WYJSC: "+wyjsc);
            } else {
                this.d = 0;

                for (int j = 0; j < this.nexts.length; j++) {
                    this.d += sigmoidPrim(sum) * this.nexts[j].d * this.nexts[j].weight[this.row];
                }
                if("firstLayer".equals(this.mode)) {
                    this.weightDelta[i] = (float)(q * this.d * this.input[i]); // Było bezpośrednio
                }
                else {
                    this.weightDelta[i] = (float)(q * this.d * this.inputs[i].output); // Było bezpośrednio
                }
            }
        }

        // Dodajemy wagi-biasy

            this.biasDelta = (float)(q * this.d);


    }

    public void getWeights() {
        for (int i = 0; i < this.weight.length; i++) {
            System.out.println("W" + i + ": " + this.weight[i]);
        }
    }


}

public class FizzBuzz {
    public String print(int numero){
        if(multiploCinco(numero) && multiploTres(numero)){
            return "FizzBuzz";
        }

        if (multiploTres (numero)){
            return "Fizz";
        }
        if (multiploCinco (numero)){
            return "Buzz";
        }
     return numero+"";
    }

    private boolean multiploTres(int numero){
        return numero% 3 == 0;
    }

    private boolean multiploCinco (int  numero){
        return numero % 5 == 0;
    }

}

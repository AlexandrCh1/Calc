import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws CalcException {
        Scanner s = new Scanner(System.in);
        System.out.println("Введите выражение:");
        String string = s.nextLine();
        System.out.println(calc(string));
    }
    public static String calc(String input) throws CalcException {
        String Operation = "";
        String strNum1 = "", strNum2 = "";
        int Num1 = 0, Num2 = 0;
        boolean isRim = false;
        Convertor PCh = new Convertor();

        // только операции сложения, вычитания, умножения и деления
        // только два числа
        String[] Operations = {"\\+","-","\\*","/"};
        int CountOp = 0;
        for(String op:Operations){
            String[] els = input.split(op);
            if (els.length == 2) {
                Operation = op;
                strNum1 = els[0].trim();
                strNum2 = els[1].trim();
                CountOp++;
            }
        }
        if (Operation.isEmpty() || CountOp > 1) {
            throw new CalcException("т.к. строка не соответствует формату [два операнда и один оператор (+, -, /, *)]");
        }

        // работа либо только с римскими, либо только с арабскими
        boolean isRimNum1 = CheckRim(strNum1);
        boolean isRimNum2 = CheckRim(strNum2);
        if (isRimNum1 != isRimNum2) {
            throw new CalcException("т.к. используются одновременно разные системы счисления");
        }

        if (isRimNum1 || isRimNum2) {
            isRim = true;
        }

        if (isRim) {
            // Преобразование римских в арабские
            Num1 = PCh.RimToArab(strNum1);
            Num2 = PCh.RimToArab(strNum2);
        } else {
            // только целые числа
            try {
                Num1 = Integer.parseInt(strNum1);
                Num2 = Integer.parseInt(strNum2);
            } catch (NumberFormatException nfe) {
                throw new CalcException("т.к. числа должны быть целыми");
            }
        }

        // на вход числа от 1 до 10 включительно
        if (!(Num1 >= 1 && Num1 <= 10) || !(Num2 >= 1 && Num2 <= 10)) {
            throw new CalcException("т.к. числа должны быть от 1 до 10 включительно");
        }

        int VZ = 0;
        switch (Operation) {
            case  ("\\+"):
                VZ =  Num1 + Num2;
                break;
            case ("-"):
                VZ =  Num1 - Num2;
                break;
            case ("\\*"):
                VZ =  Num1 * Num2;
                break;
            case ("/"):
                VZ = (int) (Num1 / Num2);
                // вывод деления: целое число, остаток отбрасывается
                break;
            default:
                throw new CalcException("не найден соответствующий оператор");
        }

        String strVZ = "";
            if (isRim) {
                // результат римских: только >= 1
                if (isRim && VZ < 1) {
                    throw new CalcException("т.к. в римской системе нет отрицательных чисел");
                }
                strVZ = PCh.ArabToRim(VZ);
        } else {
            strVZ = String.valueOf(VZ);
        }

        return strVZ;
    }
    static boolean CheckRim(String ChNum) {
        String RC= "IVXLC";
        char[] RimC = RC.toCharArray();
        char[] charArray = ChNum.toCharArray();

        for (char element:charArray) {
            for(char elRimC:RimC){
                if (element == elRimC) {
                    return true;
                }
            }
        }
        return false;
    }
    static class  Convertor {
        String[] RimNums = {"I","II","III","IV","V","VI","VII","VIII","IX","X"};
        String[] RimNumsDes = {"X","XX","XXX","XL","L","LX","LXX","LXXX","XC","C"};

        int RimToArab(String RimNum){
            for(int i = 0; i < RimNums.length; i++){
                if(RimNum.equals(RimNums[i]) == true){
                    return i + 1 ;
                }
            }
            return -1;
        }
        String ArabToRim(int ArabNum){
            if(ArabNum > 10) {
                int Ost = ArabNum % 10;
                if(Ost == 0) {
                    return RimNumsDes[ArabNum/10-1];
                } else {
                    int Des = (int)(ArabNum/10);
                    String SV = RimNumsDes[Des-1] + RimNums[Ost-1];
                    return SV;
                }
            } else {
                return RimNums[ArabNum-1];
            }
        }
    }

    static class CalcException extends Exception{
        CalcException(String description){
            super(description);
        }
    }
}


package testproj;

public class Currency {

    public String valuteID;
    public int numCode;
    public String charCode;
    public int nominal;
    public String name;
    public double value;

    public Currency() {
    }

    public Currency(/*String valuteID,*/ int numCode, String charCode, int nominal, String name, double value){
        //this.valuteID = valuteID;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }


    @Override
    public String toString() {
        return "Valute{" +//+"ID='" + valuteID + '\'' +
                ", numCode='" + numCode + '\'' +", " +
                "charCode='" + charCode + '\'' +
                ", nominal='" + nominal + '\'' +
                ", name='" + name+ '\'' +
                ", value='" + value+ '\'' +
                '}';
    }
}

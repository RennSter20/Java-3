package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Sučelje s metodama koje se implementiraju u klasama FakultetRacunarstva i VeleucilisteJave.
 */
public interface Visokoskolska {


    BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta (Ispit[] ispiti, Integer pismeni, Integer obrana, Student student);

    /**
     * Metoda služi za izdvajanje polozenih ispita.
     * @param ispiti Lista ispita prema kojima se izdvajaju samo polozeni ispiti.
     * @return Lista polozenih ispita.
     */
    private Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti){

        Integer broj = 0;
        Ispit[] tempIspiti = new Ispit[broj];

        for(int i = 0;i< ispiti.length;i++){
            if(ispiti[i].getOcjena() > 1){
                broj++;
                tempIspiti = Arrays.copyOf(tempIspiti, broj);
                tempIspiti[broj-1] = ispiti[i];
            }
        }
        return tempIspiti;
    }

    /**
     * Metoda određuje prosjek ocjena za sve ispite koji su priloženi u parametrima. U slučaju da je jedan od ispita ocjenjen s nedovoljan, prekida se obrada prosjeka ocjena.
     * @param ispiti
     * @return Prosjek ocjena na ispitima.
     * @throws NemoguceOdreditiProsjekStudentaException
     */
    default BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) throws NemoguceOdreditiProsjekStudentaException {

        Integer suma = 0;
        Integer broj = 0;

            for(int i = 0;i<ispiti.length;i++){
                if(ispiti[i].getOcjena() > 1){
                    suma+= ispiti[i].getOcjena();
                    broj++;
                }else{
                    throw new NemoguceOdreditiProsjekStudentaException("Student " + ispiti[i].getStudent().getIme() + " " + ispiti[i].getStudent().getPrezime() + " zbog negativne ocjene na jednom od ispita ima prosjek nedovoljan (1)!");
                }
            }

        return BigDecimal.valueOf(suma/broj);
    }

    /**
     * Metoda nalazi sve ispite koje je student priložen u parametru pisao.
     * @param ispiti Ispiti po kojima se gleda da li je student pisao ispit.
     * @param student Student prema kojem se trebaju filtrirati ispiti.
     * @return Lista ispita.
     */
    default  Ispit[] filtrirajIspitePoStudentu(Ispit[] ispiti, Student student) {

        int brojIspita = 0;

        Ispit[] tempIspiti = new Ispit[brojIspita];
        for(int i = 0;i<ispiti.length;i++){
            if(ispiti[i].getStudent() == student){
                brojIspita++;
                tempIspiti = Arrays.copyOf(tempIspiti, brojIspita);
                tempIspiti[brojIspita-1] = ispiti[i];
            }
        }
        return tempIspiti;
    }


}

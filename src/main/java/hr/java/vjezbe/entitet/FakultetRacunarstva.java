package hr.java.vjezbe.entitet;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmadjihStudenataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Klasa FakultetRačunarstva nasljeđuje ObrazovnaUstanova i implementira sučelje Diplomski.
 * Unutar klase implementirane su metode koje se koriste tijekom izvođenja programa u svrhu
 * određivanja konačnih ocjena, najuspješnijeg studenta na fakultetu te određivanja studenta
 * za rektorovu nagradu.
 */
public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

    public static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    public FakultetRacunarstva(String naziv, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
        super(naziv, predmeti, profesori, studenti, ispiti);
    }

    /**
     *  U metodi se poziva metoda filtrirajIspitePoStudentu. Vraćena lista sadržava sve ispite koje je pisao navedeni student. Zatim pozivamo metodu odrediProsjekOcjenaNaIspitima koja vraća prosjek studenta na ispitima.
     *  Metoda također sadrži try/catch dio koji hvata NemoguceOdreditiProsjekStudentaException u slučaju da student ima jednu ocjenu nedovoljan.
     * @param ispiti svi ispiti
     * @param pismeni ocjena pismenog ispita
     * @param diplomski ocjena diplomskog
     * @param student student za kojeg je potrebno izračunati konačnu ocjenu
     * @return metoda vraća konačnu ocjenu.
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer pismeni, Integer diplomski, Student student) {

        Ispit[] ispitiStudenta = filtrirajIspitePoStudentu(ispiti, student);
        BigDecimal prosjekOcjenaNaIspitima = null;

        try{

            prosjekOcjenaNaIspitima = odrediProsjekOcjenaNaIspitima(ispitiStudenta);

        }catch(NemoguceOdreditiProsjekStudentaException e){
            System.out.println(e.getMessage());
            logger.error(String.valueOf(e.getCause()));
            return null;
        }


        return (prosjekOcjenaNaIspitima.multiply(BigDecimal.valueOf(3)).add(BigDecimal.valueOf(diplomski).add(BigDecimal.valueOf(pismeni)))).divide(BigDecimal.valueOf(5));

    }

    /**
     * Metoda koja izdvaja studenta s najviše ocjenjenih ispita s ocjenom izvrstan. U slučaju da ima više njih, određuje se onaj s manjim indexom.
     * @param godina godina za koju se određuje najuspješniji student na godini
     * @return najuspješniji student određen prema ocjenama na ispitima
     */
    @Override
    public Student odrediNajuspjesnijegStudentaNaGodini(Integer godina) {

        Integer indexStudenta = 0;
        BigDecimal najboljiProsjek = BigDecimal.valueOf(0);

        for(int i = 0;i< getIspiti().length;i++){

            BigDecimal temp = null;

            try{
                temp = odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(getIspiti(), getStudenti()[i]));
            }catch(NemoguceOdreditiProsjekStudentaException e){
                System.out.println(e.getMessage());
            }
            if(temp.compareTo(najboljiProsjek) >= 0){
                indexStudenta = i;
                najboljiProsjek = temp;
            }
        }

        return getStudenti()[indexStudenta];
    }

    /**
     * Metoda za određivanje studenta koji je zaslužio rektorovu nagradu. Student se određuje prema najvećem prosjeku. Ako takvih studenata ima više, odabire se najmlađi student.
     * U slućaju da ima više studenata s istim, najboljim prosjekom te su ujedino i najmlađi, baca se iznimka PostojiViseNajmadjihStudenataException.
     * @return
     * @throws PostojiViseNajmadjihStudenataException
     */
    @Override
    public Student odrediStudentaZaRektorovuNagradu() throws PostojiViseNajmadjihStudenataException {

        BigDecimal[] prosjeci = new BigDecimal[getStudenti().length];


        for(int i = 0;i< getStudenti().length;i++){
            try{
                prosjeci[i] = odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(getIspiti(), getStudenti()[i]));
            }catch(NemoguceOdreditiProsjekStudentaException e){
                System.out.println(e.getMessage());

            }
        }

        Integer najbolji = 0;
        for(int i = 1;i< getStudenti().length;i++){
            if(prosjeci[i].compareTo(prosjeci[najbolji]) > 0){
                najbolji = i;
            }else if(prosjeci[i].compareTo(prosjeci[najbolji]) == 0){
                if(getStudenti()[i].getDatumRodjenja().isAfter(getStudenti()[i].getDatumRodjenja())){
                    najbolji = i;
                }
            }
        }

        Integer brojIstihStudenata = 0;
        Student[] studentiIstogProsjekaIRodjendana = new Student[brojIstihStudenata];

        for(int i = 0;i<getStudenti().length;i++){
            if(prosjeci[i].equals(prosjeci[najbolji]) && getStudenti()[i].getDatumRodjenja().isEqual(getStudenti()[i].getDatumRodjenja())){
                brojIstihStudenata++;
                studentiIstogProsjekaIRodjendana = Arrays.copyOf(studentiIstogProsjekaIRodjendana, brojIstihStudenata);
                studentiIstogProsjekaIRodjendana[brojIstihStudenata - 1] = getStudenti()[i];
            }
        }

        if(brojIstihStudenata > 0){
            String studentiZaIspisati = "";
            for(int i = 0;i<brojIstihStudenata;i++){
                if(i == brojIstihStudenata - 1){
                    studentiZaIspisati += " i ";
                }
                studentiZaIspisati += studentiIstogProsjekaIRodjendana[i].getIme() + " " + studentiIstogProsjekaIRodjendana[i].getPrezime() + " ";

            }
            throw new PostojiViseNajmadjihStudenataException("Pronadeno je vise najmladih studenata s istim datumom rodenja, a to su " + studentiZaIspisati);

        }
        return getStudenti()[najbolji];

    }
}

public class PrepaidCard extends Card{
    
    private double balance;

    public PrepaidCard (int CustomerID, int cardNumber, String cardLogo, double expirationDate, int cvv, double balance){
        super(CustomerID, cardNumber, cardLogo, expirationDate, cvv);
        this.balance = balance;
    }

}

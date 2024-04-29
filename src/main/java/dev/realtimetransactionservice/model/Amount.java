package dev.realtimetransactionservice.model;

public class Amount {
    private String amount;
    private String currency;
    private DebitCredit debitOrCredit;

    public Amount() {
    }

    public Amount(String amount, String currency, DebitCredit debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DebitCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", debitOrCredit=" + debitOrCredit +
                '}';
    }
}

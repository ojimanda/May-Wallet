package id.yozi.may_wallet.model;

public class HistoryTopupModel {

    public String bank;
    public String rekening;
    public String amount;
    public String tanggal;


    public HistoryTopupModel(String bank, String rekening, String amount, String tanggal) {
        this.bank = bank;
        this.rekening = rekening;
        this.amount = amount;
        this.tanggal = tanggal;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}

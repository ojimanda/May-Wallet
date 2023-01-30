package id.yozi.may_wallet.model;

public class HistoryTransferModel {

    public String pengirimOrPenerima;
    public String e_mail;
    public String tanggal;
    public String amount;

    public HistoryTransferModel(String pengirimOrPenerima, String e_mail, String tanggal, String amount) {
        this.pengirimOrPenerima = pengirimOrPenerima;
        this.e_mail = e_mail;
        this.tanggal = tanggal;
        this.amount = amount;
    }

    public String getPengirimOrPenerima() {
        return pengirimOrPenerima;
    }

    public void setPengirimOrPenerima(String pengirimOrPenerima) {
        this.pengirimOrPenerima = pengirimOrPenerima;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

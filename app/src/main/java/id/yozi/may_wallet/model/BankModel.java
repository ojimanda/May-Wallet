package id.yozi.may_wallet.model;

public class BankModel {

    public int imageBank;
    public String textBank;

    public BankModel(int imageBank, String textBank) {
        this.imageBank = imageBank;
        this.textBank = textBank;
    }

    public int getImageBank() {
        return imageBank;
    }

    public void setImageBank(int imageBank) {
        this.imageBank = imageBank;
    }

    public String getTextBank() {
        return textBank;
    }

    public void setTextBank(String textBank) {
        this.textBank = textBank;
    }
}

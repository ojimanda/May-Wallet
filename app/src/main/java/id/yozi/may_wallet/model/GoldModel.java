package id.yozi.may_wallet.model;

public class GoldModel {

    public String cost;
    public String total;
    public String date;
    public String hal;


    public GoldModel(String cost, String total, String date, String hal) {
        this.cost = cost;
        this.total = total;
        this.date = date;
        this.hal = hal;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHal() {
        return hal;
    }

    public void setHal(String hal) {
        this.hal = hal;
    }
}

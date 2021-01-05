package bean;

import java.util.Objects;

public class CompanyData {
    private String date;
    private String fund;
    private String company;
    private String ticker;
    private String cusip;
    private Double shares;
    private Double marketValue;
    private Double weight;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip;
    }

    public Double getShares() {
        return shares;
    }

    public void setShares(Double shares) {
        this.shares = shares;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyData that = (CompanyData) o;
        return Objects.equals(cusip, that.cusip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cusip);
    }

    @Override
    public String toString() {
        return "CompanyData{" +
                "date='" + date + '\'' +
                ", fund='" + fund + '\'' +
                ", company='" + company + '\'' +
                ", ticker='" + ticker + '\'' +
                ", cusip='" + cusip + '\'' +
                ", shares=" + shares +
                ", marketValue=" + marketValue +
                ", weight=" + weight +
                '}';
    }
}

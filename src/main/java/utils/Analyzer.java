package utils;

import bean.CompanyData;
import com.slack.api.webhook.Payload;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {

    /**
     * return the slack message for company list
     * @return • Detective Chimp\n• Bouncing Boy\n• Aqualad
     */
    private static String getCompaniesStr(Set<CompanyData> companies){
        StringBuilder sb = new StringBuilder();
        for(CompanyData cmp : companies){
            String marketValue = cmp.getMarketValue() >=0 ? "+" + cmp.getMarketValue() : "" + cmp.getMarketValue();
            sb.append("- ").append(cmp.getCompany()).append(", market value:").append(marketValue).append("\n");
        }
        return sb.toString();
    }
    private static String getCompaniesStr(Map<CompanyData, String> companies){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<CompanyData, String> cmp : companies.entrySet()){
            sb.append("- ").append(cmp.getKey().getCompany()).append(", shares changing:").append(cmp.getValue()).append("\n");
        }
        return sb.toString();
    }
    /**
     * 1.check the new company added into portfolio
     * 2.check the company delete from portfolio
     * 3.check the same company shares (+-) 5.0+%
     */
    public static void highLevelAnalyze(List<CompanyData> oldList, List<CompanyData> newList) {
        Map<String, CompanyData> newMap = newList.stream().collect(Collectors.toMap(CompanyData::getCusip, x -> x));

        StringBuilder sb = new StringBuilder();
//        String fundName = newList.get(0).getFund();
//        sb.append("[" + fundName + "]").append("\n");

        //check the new company added into portfolio
        Set<CompanyData> newAddCompanies = new HashSet<>(newList);
        newAddCompanies.removeAll(oldList);
        if(!newAddCompanies.isEmpty()) {
            String newAddCompaniesStr = getCompaniesStr(newAddCompanies);
            String newAddSlackMsg = "New Add Companies:\n" + newAddCompaniesStr;
            sb.append(newAddSlackMsg);
        }

        //check the company delete from portfolio
        Set<CompanyData> deleteCompanies = new HashSet<>(oldList);
        deleteCompanies.removeAll(newList);
        if(!deleteCompanies.isEmpty()){
            String deleteCompaniesStr = getCompaniesStr(deleteCompanies);
            String deleteSlackMsg = "Removed Companies:\n" + deleteCompaniesStr;
            sb.append(deleteSlackMsg);
        }

        Map<CompanyData, String> tradingCompanies = new HashMap<>();
        final double threshold = 0.05;
        //check the same company shares (+-) 5.0+%
        for(CompanyData oldCmp : oldList){
            if(newMap.containsKey(oldCmp.getCusip())){
                CompanyData newCmp = newMap.get(oldCmp.getCusip());
                double changing = oldCmp.getShares() - newCmp.getShares();
                //String changingStr = String.format("%.2f", Math.abs(changing) / oldCmp.getShares());
                String flag = changing >=0 ? "+" : "-";
                String changingStr = flag + round(Math.abs(changing) / oldCmp.getShares() * 100, 2) +"";
                System.out.println(Math.abs(changing)+","+ oldCmp.getShares() +","+newCmp.getShares()+","+Math.abs(changing) / oldCmp.getShares());
                //only show the change larger than 5%
                if(Math.abs(changing) / oldCmp.getShares() >= threshold){
                    tradingCompanies.put(oldCmp, changingStr + "%");
                }
            }
        }
        if(!tradingCompanies.isEmpty()){
            String tradingCompaniesStr = getCompaniesStr(tradingCompanies);
            String tradingSlackMsg = "Trading Companies (+-"+ threshold * 100 +"% shares):\n" + tradingCompaniesStr;
            sb.append(tradingSlackMsg);
        }

        //send slack
        Payload payload = Payload.builder().text(sb.toString()).build();
        SlackUtils.sendMessage(payload);
    }

    public static double round(double value, int places) {
        if (places < 0){
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

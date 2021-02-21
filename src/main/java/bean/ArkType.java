package bean;

/**
 * @author ryang
 */
public enum ArkType {

    /**
     * ark type, and its csv link
     */
    ARKK("ARKK_OLD_JSON", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_INNOVATION_ETF_ARKK_HOLDINGS.csv"),
    ARKQ("ARKQ_OLD_JSON", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_FINTECH_INNOVATION_ETF_ARKF_HOLDINGS.csv"),
    ARKW("ARKW_OLD_JSON", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_NEXT_GENERATION_INTERNET_ETF_ARKW_HOLDINGS.csv"),
    ARKG("ARKG_OLD_JSON", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_GENOMIC_REVOLUTION_MULTISECTOR_ETF_ARKG_HOLDINGS.csv"),
    ARKF("ARKF_OLD_JSON", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_FINTECH_INNOVATION_ETF_ARKF_HOLDINGS.csv"),
    ;

    private final String fileName;
    private final String csvLink;

    ArkType(String fileName, String csvLink){
        this.fileName = fileName;
        this.csvLink = csvLink;
    }


    public String getFileName() {
        return fileName;
    }
    public String getCsvLink() {
        return csvLink;
    }
}

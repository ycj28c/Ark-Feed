package bean;

/**
 * @author ryang
 */
public enum ArkType {

    /**
     * ark type, and its csv link
     */
    ARKK("arkk_old_result.json", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_INNOVATION_ETF_ARKK_HOLDINGS.csv"),
    ARKQ("arkq_old_result.json", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_FINTECH_INNOVATION_ETF_ARKF_HOLDINGS.csv"),
    ARKW("arkw_old_result.json", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_NEXT_GENERATION_INTERNET_ETF_ARKW_HOLDINGS.csv"),
    ARKG("arkg_old_result.json", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_GENOMIC_REVOLUTION_MULTISECTOR_ETF_ARKG_HOLDINGS.csv"),
    ARKF("arkf_old_result.json", "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_FINTECH_INNOVATION_ETF_ARKF_HOLDINGS.csv"),
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

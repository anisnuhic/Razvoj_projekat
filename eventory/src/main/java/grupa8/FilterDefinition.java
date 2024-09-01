package grupa8;

import java.util.List;

public class FilterDefinition {

    private String searchText;

    private Integer fromPrice;

    private Integer toPrice;

    private List<String> locationNames;

    private String category;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Integer getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(Integer fromPrice) {
        this.fromPrice = fromPrice;
    }

    public Integer getToPrice() {
        return toPrice;
    }

    public void setPrice(String fromPrice, String toPrice) {
        if (fromPrice.isEmpty()) {
            this.fromPrice = null;
        } else {
            this.fromPrice = Integer.parseInt(fromPrice);
        }
        if (toPrice.isEmpty()) {
            this.toPrice = null;
        } else {
            this.toPrice = Integer.parseInt(toPrice);
        }
    }

    public void setToPrice(Integer toPrice) {
        this.toPrice = toPrice;
    }

    public List<String> getLocationNames() {
        return locationNames;
    }

    public void setLocationNames(List<String> locationNames) {
        this.locationNames = locationNames;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void removeLocation(String locationName) {
        if (locationNames == null) {
            return;
        }
        this.locationNames.remove(locationName);
    }
}

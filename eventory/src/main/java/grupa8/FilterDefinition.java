package grupa8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FilterDefinition {

    private static FilterDefinition single_instance = null;

    private String searchText;

    private Integer fromPrice;

    private Integer toPrice;

    private List<String> locationNames = new ArrayList<>();

    private String category;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private FilterDefinition() {
    }

    public static synchronized FilterDefinition getInstance() {
        if (single_instance == null) {
            single_instance = new FilterDefinition();
        }
        return single_instance;
    }

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

    public void setDate(String fromDate, String toDate) {
        if (fromDate.isEmpty()) {
            this.fromDate = null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            this.fromDate = LocalDate.parse(fromDate, formatter).atStartOfDay();
        }
        if (toDate.isEmpty()) {
            this.toDate = null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            this.toDate = LocalDate.parse(toDate, formatter).atTime(LocalTime.MAX);
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

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public boolean filterPriceBetweenOn() {
        return fromPrice != null && toPrice != null;
    }

    public boolean filterPriceFromOn() {
        return fromPrice != null && toPrice == null;
    }

    public boolean filterPriceToOn() {
        return fromPrice == null && toPrice != null;
    }

    public boolean filterCategoryOn() {
        return category != null;
    }

    public boolean searchTextOn() {
        return searchText != null;
    }

    public boolean filterLocationsOn() {
        return locationNames != null && !locationNames.isEmpty();
    }

    public boolean filterDateBetweenOn() {
        return fromDate != null && toDate != null;
    }

    public boolean filterDateFromOn() {
        return fromDate != null && toDate == null;
    }

    public boolean filterDateToOn() {
        return fromDate == null && toDate != null;
    }

    public void resetFilters() {
        fromPrice = null;
        toPrice = null;
        locationNames = new ArrayList<>();
        category = null;
        fromDate = null;
        toDate = null;
        searchText = null;
    }

}

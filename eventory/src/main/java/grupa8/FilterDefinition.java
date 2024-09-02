package grupa8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FilterDefinition {

    private String searchText;

    private Integer fromPrice;

    private Integer toPrice;

    private List<String> locationNames;

    private String category;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

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
}

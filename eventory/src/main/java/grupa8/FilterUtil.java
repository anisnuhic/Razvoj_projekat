package grupa8;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class FilterUtil {

    public static List<Dogadjaj> filterDogadjaj() {
        try (EntityManager em = EntityManagerFactoryInstance.getInstance().getEntityManagerFactory().createEntityManager()) {
            FilterDefinition filterDefinition = FilterDefinition.getInstance();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            // Create main query
            CriteriaQuery<Dogadjaj> cq = cb.createQuery(Dogadjaj.class);
            Root<Dogadjaj> eventRoot = cq.from(Dogadjaj.class);
            List<Predicate> predicates = new ArrayList<>();
            if (filterDefinition.filterPriceBetweenOn()) {
                // Create subquery for filtering events by ticket price
                predicates.add(getPredicateForPriceBetween(cb, cq, eventRoot, filterDefinition));
            }
            if (filterDefinition.filterPriceFromOn()) {
                predicates.add(getPredicateForPriceFrom(cb, cq, eventRoot, filterDefinition));
            }
            if (filterDefinition.filterPriceToOn()) {
                predicates.add(getPredicateForPriceTo(cb, cq, eventRoot, filterDefinition));
            }
            if (filterDefinition.filterCategoryOn()) {
                predicates.add(cb.equal(eventRoot.get("vrstaDogadjaja"), filterDefinition.getCategory()));
            }
            if (filterDefinition.searchTextOn()) {
                predicates.add(cb.like(eventRoot.get("naziv"), "%" + filterDefinition.getSearchText() + "%"));
            }
            if (filterDefinition.filterDateBetweenOn()) {
                predicates.add(cb.between(eventRoot.get("datumVrijeme"), filterDefinition.getFromDate(), filterDefinition.getToDate()));
            }
            if (filterDefinition.filterDateToOn()) {
                predicates.add(cb.lessThanOrEqualTo(eventRoot.get("datumVrijeme"), filterDefinition.getToDate()));
            }
            if (filterDefinition.filterDateFromOn()) {
                predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("datumVrijeme"), filterDefinition.getFromDate()));
            }
            if (filterDefinition.filterLocationsOn()) {
                predicates.add(getPredicateForLocations(eventRoot, filterDefinition.getLocationNames()));
            }
            predicates.add(cb.equal(eventRoot.get("odobreno"), true));
            // Combine the predicates
            cq.where(predicates.toArray(new Predicate[0]));

            return em.createQuery(cq).getResultList();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static Predicate getPredicateForLocations(Root<Dogadjaj> eventRoot, List<String> locationNames) {
        Join<Dogadjaj, Lokacija> locationJoin = eventRoot.join("lokacija");
        return locationJoin.get("grad").in(locationNames);
    }

    private static Predicate getPredicateForPriceBetween(CriteriaBuilder cb, CriteriaQuery<Dogadjaj> cq, Root<Dogadjaj> eventRoot, FilterDefinition filterDefinition) {
        Subquery<Integer> subquery = cq.subquery(Integer.class);
        Root<Karta> ticketRoot = subquery.from(Karta.class);
        subquery.select(ticketRoot.get("dogadjaj").get("dogadjajId"))
                .where(cb.between(ticketRoot.get("cijena"), filterDefinition.getFromPrice(), filterDefinition.getToPrice()));
        return cb.in(eventRoot.get("dogadjajId")).value(subquery);
    }

    private static Predicate getPredicateForPriceFrom(CriteriaBuilder cb, CriteriaQuery<Dogadjaj> cq, Root<Dogadjaj> eventRoot, FilterDefinition filterDefinition) {
        Subquery<Integer> subquery = cq.subquery(Integer.class);
        Root<Karta> ticketRoot = subquery.from(Karta.class);
        subquery.select(ticketRoot.get("dogadjaj").get("dogadjajId"))
                .where(cb.ge(ticketRoot.get("cijena"), filterDefinition.getFromPrice()));
        return cb.in(eventRoot.get("dogadjajId")).value(subquery);
    }

    private static Predicate getPredicateForPriceTo(CriteriaBuilder cb, CriteriaQuery<Dogadjaj> cq, Root<Dogadjaj> eventRoot, FilterDefinition filterDefinition) {
        Subquery<Integer> subquery = cq.subquery(Integer.class);
        Root<Karta> ticketRoot = subquery.from(Karta.class);
        subquery.select(ticketRoot.get("dogadjaj").get("dogadjajId"))
                .where(cb.le(ticketRoot.get("cijena"), filterDefinition.getToPrice()));
        return cb.in(eventRoot.get("dogadjajId")).value(subquery);
    }
}

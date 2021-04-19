package labs.pm.data;

import labs.pm.enumaration.Rating;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private static Map<String, ResourceFormatter> formatters
            = Map.of("en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "en-FR", new ResourceFormatter(Locale.FRANCE),
            "zh-CN-RU", new ResourceFormatter(Locale.CHINA));

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());


    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProductById(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r -> r.getRating().ordinal())
                                        .average()
                                        .orElse(0)
                        )
                )
        );
        products.put(product, reviews);
        return product;
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProducts(product));
        txt.append("\n");

        if (reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews") + "\n");
        } else {
            txt.append(reviews.stream()
                    .map(r -> formatter.formatReviews(r) + "\n")
                    .collect(Collectors.joining()));
        }

        System.out.println(txt);
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        StringBuilder txt = new StringBuilder();
        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(p->txt.append(formatter.formatProducts(p) + "\n"));
        System.out.println(txt);
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProductById(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    public Product findProductById(int id) throws ProductManagerException {
        return products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductManagerException("Product with id = "+id+" not found"));
    }

    public Map<String, String> getDiscounts() {
        return products.keySet()
            .stream()
            .collect(Collectors.groupingBy(
                product -> product.getRating().getStars(),
                Collectors.collectingAndThen(
                    Collectors.summingDouble(
                        product -> product.getDiscount().doubleValue()),
                        discount -> formatter.moneyFormat.format(discount)
                    )
                )
            );
    }

    private static class ResourceFormatter {

        private Locale locale;
        private ResourceBundle resource;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            this.locale = locale;
            resource = ResourceBundle.getBundle("labs.pm.data.resources");
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProducts(Product product) {
            return MessageFormat.format(resource.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReviews(Review review) {
            return MessageFormat.format(resource.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resource.getString(key);
        }

    }

}

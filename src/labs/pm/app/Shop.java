package labs.pm.app;

import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.enumaration.Rating;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * {@code Shop} represents an application that manages Products
 *
 * @author gabrielsouza
 * @version 1.0
 */

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager(Locale.getDefault());

        pm.createProduct(100, "Bread", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        //pm.printProductReport(100);
        pm.reviewProduct(100, Rating.FIVE_STAR, "Nice hot cup of tea");
        pm.reviewProduct(100, Rating.FOUR_STAR, "Fine Tea");
        pm.reviewProduct(100, Rating.THREE_STAR, "Good Tea");
        //pm.printProductReport(100);

        pm.createProduct(101, "Coffee", BigDecimal.valueOf(7.90), Rating.NOT_RATED);
        pm.reviewProduct(101, Rating.THREE_STAR, "Nice hot cup of Coffee");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Fine Coffee");
        pm.reviewProduct(101, Rating.TWO_STAR, "Good Coffee");
         pm.printProductReport(101);
        Predicate<Product> filter = (p -> p.getPrice().floatValue() > 2);
        pm.printProducts(filter, (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal());

        //Comparator<Product> ratingSorter  = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        //Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
        //pm.printProducts(ratingSorter);
        //pm.printProducts(priceSorter);
        //pm.printProducts(ratingSorter.thenComparing(priceSorter));
        //pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());


        /*
        Product p2 = pm.createProduct(101, "Coffee", BigDecimal.TEN, Rating.ONE_STAR);
        Product p3 = pm.createProduct(102, "Cake", BigDecimal.valueOf(7.99), Rating.TWO_STAR, LocalDate.now().plusDays(2));
        Product p4 = pm.createProduct(103, "Cookie", BigDecimal.valueOf(10.98), Rating.THREE_STAR, LocalDate.now().plusDays(2));
        Product p5 = p3.applyRating(Rating.ONE_STAR);
        Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FOUR_STAR);
        Product p7 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(4.00), Rating.FIVE_STAR, LocalDate.now());

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);
        System.out.println(p6);
        System.out.println(p7);
        //System.out.println(p6.equals(p7));

         */

    }

}

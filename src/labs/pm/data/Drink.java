package labs.pm.data;

import labs.pm.enumaration.Rating;

import java.math.BigDecimal;
import java.time.LocalTime;

public final class Drink extends Product{

    Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public BigDecimal getDiscount() {
        LocalTime now = LocalTime.now();
        return (now.isAfter(LocalTime.of(00, 01)) && now.isBefore(LocalTime.of(23, 59)))
                ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Drink(getId(), getName(), getPrice(), newRating);
    }
}

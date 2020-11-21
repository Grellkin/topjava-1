package ru.javawebinar.topjava.testData;

import org.springframework.data.util.Pair;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealTestData {

    //inserted and fake data
    public final static LocalDateTime date0928time23 = LocalDateTime.of(2020, Month.SEPTEMBER, 28, 23, 0);
    public final static LocalDateTime date1111time1111 = LocalDateTime.of(2020, Month.NOVEMBER, 11, 11, 11);
    public final static LocalDateTime fake_date0822time2208 = LocalDateTime.of(2020, Month.OCTOBER, 22, 22, 8);

    public final static Pair<Integer, Pair<String, Integer>> taco = Pair.of(50, Pair.of("taco with extra cheese", 1000));
    public final static Pair<Integer, Pair<String, Integer>> burrito = Pair.of(51, Pair.of("burrito with mayo", 1299));
    public final static Pair<Integer, Pair<String, Integer>> beans = Pair.of(52, Pair.of("baked beans", 800));
    public final static Pair<Integer, Pair<String, Integer>> salad = Pair.of(53, Pair.of("salad", 300));
    public final static Pair<Integer, Pair<String, Integer>> potato = Pair.of(54, Pair.of("fried potato", 1800));
    public final static Pair<Integer, Pair<String, Integer>> salmon = Pair.of(55, Pair.of("salmon", 390));
    public final static Pair<Integer, Pair<String, Integer>> fake_ice_cream = Pair.of(12, Pair.of("fake ice cream", 9999));

    public static Meal tacoUser1 = new Meal(taco.getFirst(), date0928time23, taco.getSecond().getFirst(), taco.getSecond().getSecond());
    public static Meal burritoUser1 = new Meal(burrito.getFirst(), date0928time23.minusHours(3), burrito.getSecond().getFirst(), burrito.getSecond().getSecond());
    public static Meal potatoUser1 = new Meal(potato.getFirst(), date1111time1111, potato.getSecond().getFirst(), potato.getSecond().getSecond());
    public static Meal salmonUser1 = new Meal(salmon.getFirst(), date1111time1111.minusHours(10), salmon.getSecond().getFirst(), salmon.getSecond().getSecond());
    public static List<Meal> mealListUser1sortedByDateDsc = Arrays.asList(potatoUser1, salmonUser1, tacoUser1, burritoUser1);


    public static Meal beansUser2 = new Meal(beans.getFirst(), date0928time23.minusHours(6), beans.getSecond().getFirst(), beans.getSecond().getSecond());
    public static Meal saladUser2 = new Meal(salad.getFirst(), date0928time23.minusDays(2), salad.getSecond().getFirst(), salad.getSecond().getSecond());
    public static List<Meal> mealListUser2sortedByDateDsc = Arrays.asList(beansUser2, saladUser2);

    public static Meal fake_ice_cream_User1 = new Meal(fake_date0822time2208, fake_ice_cream.getSecond().getFirst(), fake_ice_cream.getSecond().getSecond());


    private MealTestData() {
    }
}


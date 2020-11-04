package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }


//    public static List<UserMealWithExcess> filtered(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//
//        final Map<LocalDate, Long> collect = meals.stream()
//                .collect(
//                        Collectors.groupingBy(
//                                meal -> meal.getDateTime().toLocalDate(),
//                                Collectors.mapping(UserMeal::getCalories, Collectors.counting())
//                                             )
//                        );
//
//        final Map<LocalDateTime, List<UserMeal>> collect1 = meals.stream()
//                .collect(
//                        Collectors.groupingBy(
//                                UserMeal::getDateTime));
//
//        Set<UserMeal> mealsSet  = new HashSet<>();
//
//        final Map<Boolean, List<UserMeal>> collect2 = meals.stream().collect(Collectors.partitioningBy(mealsSet::add));
//
//    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        List<UserMealWithExcess> excessMeals = new ArrayList<>();


        for (UserMeal meal :
                meals) {
            final LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesByDate.compute(localDate, (k, v) -> {
                if (v == null) {
                    v = meal.getCalories();
                } else {
                    v += meal.getCalories();
                }
                return v;
            });

            final LocalTime localTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime)) {
                excessMeals.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
            }
        }

        excessMeals.forEach(meal -> {
            final LocalDate localDate = meal.getDateTime().toLocalDate();
            if (caloriesByDate.get(localDate).compareTo(caloriesPerDay) > 0) {
                meal.setExcess(true);
            }
        });

        return excessMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();

        return meals.stream()
                .map(userMeal -> {
                    final LocalDate localDate = userMeal.getDateTime().toLocalDate();
                    caloriesByDate.compute(localDate, (k, v) -> {
                        if (v == null) {
                            v = userMeal.getCalories();
                        } else {
                            v += userMeal.getCalories();
                        }
                        return v;
                    });
                    return userMeal;
                })
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false))
                .collect(Collectors.toList()).stream().peek(userMealWithExcess -> {
                    if (caloriesByDate.get(userMealWithExcess.getDateTime().toLocalDate()).compareTo(caloriesPerDay) > 0) {
                        userMealWithExcess.setExcess(true);
                    }
                })
                .collect(Collectors.toList());


    }
}

package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.testData.MealTestData;
import ru.javawebinar.topjava.testData.UserTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@ContextConfiguration(value = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@Sql(scripts = "classpath:db/populateDB.sql")
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        final Integer beansId = MealTestData.beans.getFirst();
        final Meal beansUser2 = MealTestData.beansUser2;
        final Meal extractedEntity = service.get(beansId, UserTestData.ADMIN_ID);
        assertThat(beansUser2).usingRecursiveComparison().isEqualTo(extractedEntity);
    }

    @Test
    public void getFakeMeal() {
        final Integer iceCreamId = MealTestData.fake_ice_cream.getFirst();
        assertThatThrownBy(() -> service.get(iceCreamId, UserTestData.USER_ID)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void delete() {
        final Integer potatoId = MealTestData.potatoUser1.getId();
        final Meal foundMeal = service.get(potatoId, UserTestData.USER_ID);
        if (Objects.nonNull(foundMeal)) {
            final Integer mealId = foundMeal.getId();
            service.delete(mealId, UserTestData.USER_ID);
            assertThatThrownBy(() -> service.get(mealId, UserTestData.USER_ID)).isInstanceOf(NotFoundException.class);
    } else {
        assert false;
    }
    }

    @Test
    public void deleteAnotherUserMeal() {
        final Integer potatoId = MealTestData.potatoUser1.getId();
        final Meal foundMeal = service.get(potatoId, UserTestData.USER_ID);
        if (Objects.nonNull(foundMeal)) {
            final Integer mealId = foundMeal.getId();

            assertThatThrownBy(() -> service.delete(mealId, UserTestData.ADMIN_ID)).isInstanceOf(NotFoundException.class);
        } else {
            assert false;
        }
    }


    @Test
    public void getAnotherPersonMeal() {
        final Integer salmonId = MealTestData.salmonUser1.getId();
        assertThatThrownBy(() -> service.get(salmonId, UserTestData.ADMIN_ID)).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void getBetweenInclusive() {
        final List<Meal> mealListUser2sortedByDateDsc = MealTestData.mealListUser2sortedByDateDsc;
        final Meal mealZero = mealListUser2sortedByDateDsc.get(0);
        final Meal mealOne = mealListUser2sortedByDateDsc.get(1);
        final List<Meal> result = service.getBetweenInclusive(mealOne.getDate().minus(1, ChronoUnit.DAYS),
                mealOne.getDate().plus(1, ChronoUnit.DAYS), UserTestData.ADMIN_ID);
        assertThat(result).hasSize(1);
    }

    @Test
    public void getAll() {
        assertThat(MealTestData.mealListUser1sortedByDateDsc).containsExactlyElementsOf(service.getAll(UserTestData.USER_ID));
    }

    @Test
    public void update() {
        final Meal saladUser2 = MealTestData.saladUser2;
        final Meal foundMeal = service.get(saladUser2.getId(), UserTestData.ADMIN_ID);
        assertThat(saladUser2).usingRecursiveComparison().isEqualTo(foundMeal);
        foundMeal.setDescription("no more salad");
        foundMeal.setCalories(15);
        service.update(foundMeal, UserTestData.ADMIN_ID);
        final Meal updatedMeal = service.get(foundMeal.getId(), UserTestData.ADMIN_ID);
        assertThat("no more salad").isEqualTo(updatedMeal.getDescription());
        assertThat(15).isEqualTo(updatedMeal.getCalories());
        assertThat(saladUser2).usingRecursiveComparison().isNotEqualTo(updatedMeal);
        assertThat(saladUser2).usingRecursiveComparison().ignoringFields("description", "calories").isEqualTo(updatedMeal);

    }

    @Test
    public void create() {
        final Meal iceCream = MealTestData.fake_ice_cream_User1;
        service.create(iceCream, UserTestData.USER_ID);
        assertThat(iceCream).isEqualTo(service.get(iceCream.getId(), UserTestData.USER_ID));
        assertThatThrownBy(() -> service.get(iceCream.getId(), UserTestData.ADMIN_ID)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void createDuplicateTime() {
        final Meal iceCream = MealTestData.fake_ice_cream_User1;
        final Meal newMeal = new Meal(iceCream);
        newMeal.setId(null);
        newMeal.setDateTime(MealTestData.burritoUser1.getDateTime());
        assertThatThrownBy(() -> service.create(newMeal, UserTestData.USER_ID)).isInstanceOf(DuplicateKeyException.class);
    }
}

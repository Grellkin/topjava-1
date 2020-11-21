package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Primary
public class JdbcMealRepository implements MealRepository {

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insert;

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("foodOwnerId", userId);

        if (meal.isNew()) {
            final Number number = insert.executeAndReturnKey(source);
            meal.setId(number.intValue());

        } else {
            final int result = namedJdbcTemplate.update("UPDATE meals SET date_time = :dateTime, " +
                    "description = :description, calories = :calories WHERE id = :id AND food_owner_id = :foodOwnerId;", source);
            if (result == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        final int result = jdbcTemplate.update("DELETE FROM meals WHERE id = ? AND food_owner_id = ?;", id, userId);
        return result > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        final List<Meal> queryResult = jdbcTemplate.query("SELECT * FROM meals WHERE food_owner_id = ? AND id = ?;", ROW_MAPPER, userId, id);
        return DataAccessUtils.singleResult(queryResult);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE food_owner_id = ? ORDER BY date_time desc;", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE food_owner_id = ? AND date_time < ? AND date_time >= ? ORDER BY date_time desc;", ROW_MAPPER, userId, endDateTime, startDateTime);
    }
}

package com.tenten.eatmatjib.restaurant.repository.custom;

import static com.tenten.eatmatjib.restaurant.domain.QRestaurant.restaurant;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenten.eatmatjib.member.domain.Coord;
import com.tenten.eatmatjib.restaurant.controller.response.QRestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.controller.response.RestaurantsQueryRes;
import com.tenten.eatmatjib.restaurant.domain.FilterType;
import com.tenten.eatmatjib.restaurant.service.command.RestaurantSearchCondition;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class RestaurantCustomRepositoryImpl implements RestaurantCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RestaurantsQueryRes> search(
        RestaurantSearchCondition searchCondition,
        Coord memberLocation,
        Pageable pageable
    ) {
        List<RestaurantsQueryRes> content = getContent(searchCondition, memberLocation, pageable);
        Long count = getCount(searchCondition, memberLocation);

        return new PageImpl<>(content, pageable, count);
    }

    private List<RestaurantsQueryRes> getContent(
        RestaurantSearchCondition searchCondition,
        Coord memberLocation,
        Pageable pageable
    ) {
        JPAQuery<RestaurantsQueryRes> query = queryFactory
            .select(
                new QRestaurantsQueryRes(
                    restaurant.id,
                    restaurant.name,
                    restaurant.address,
                    restaurant.zipCode,
                    restaurant.cuisine,
                    restaurant.phoneNumber,
                    restaurant.homepageUrl,
                    restaurant.avgScore
                )
            )
            .from(restaurant)
            .where(
                keywordContains(searchCondition.getKeyword()),
                cuisineEq(searchCondition.getFilterType()),
                distanceLoe(memberLocation, searchCondition.getRangeType().getValue())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        switch (searchCondition.getOrderType()) {
            case DISTANCE -> query.orderBy(getDistanceExpression(memberLocation).asc());
            case RATE -> query.orderBy(restaurant.avgScore.desc());
            default -> query.orderBy(
                getDistanceExpression(memberLocation).asc(),
                restaurant.avgScore.desc()
            );
        }

        return query.fetch();
    }

    private Long getCount(RestaurantSearchCondition searchCondition, Coord memberLocation) {
        return queryFactory
            .select(restaurant.count())
            .from(restaurant)
            .where(
                keywordContains(searchCondition.getKeyword()),
                cuisineEq(searchCondition.getFilterType()),
                distanceLoe(memberLocation, searchCondition.getRangeType().getValue())
            )
            .fetchOne();
    }

    private BooleanBuilder keywordContains(String keyword) {
        return Optional.ofNullable(keyword)
            .map(kw -> new BooleanBuilder(
                restaurant.name.contains(kw).or(restaurant.address.contains(kw)))
            )
            .orElseGet(BooleanBuilder::new);
    }

    private BooleanBuilder cuisineEq(FilterType filterType) {
        return Optional.ofNullable(filterType)
            .map(ft -> new BooleanBuilder(restaurant.cuisine.eq(ft.getValue())))
            .orElseGet(BooleanBuilder::new);
    }

    private BooleanExpression distanceLoe(Coord memberLocation, int range) {
        return getDistanceExpression(memberLocation).loe(range * 1000.0);
    }

    private NumberTemplate<Double> getDistanceExpression(Coord memberLocation) {
        return Expressions.numberTemplate(
            Double.class,
            "sqrt(power({0} - {1}, 2) + power({2} - {3}, 2))",
            restaurant.x.doubleValue(), memberLocation.x(),
            restaurant.y.doubleValue(), memberLocation.y()
        );
    }
}

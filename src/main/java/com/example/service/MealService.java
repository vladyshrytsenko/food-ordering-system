package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.MealDto;
import com.example.model.entity.Cuisine;
import com.example.model.entity.Meal;
import com.example.model.request.MealRequest;
import com.example.repository.MealRepository;
import com.stripe.model.Price;
import com.stripe.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final CuisineService cuisineService;
    private final StripeService stripeService;

    @Transactional
    public MealDto create(MealRequest request) {
        Meal mealToSave = MealRequest.toEntity(request);

        if (isBlank(request.getCuisineName())) {
            throw new RuntimeException("Cuisine should not be blank");
        }

        Cuisine cuisineByName = this.cuisineService.getByName(request.getCuisineName());
        mealToSave.setCuisine(cuisineByName);
        Meal createdMeal = this.mealRepository.save(mealToSave);

        Product stripeProduct = this.stripeService.createProduct(createdMeal.getName());
        Price stripePrice = this.stripeService.createPrice(stripeProduct.getId(), createdMeal.getPrice());

        return MealDto.toDto(createdMeal);
    }

    public MealDto getById(Long id) {
        Meal mealById = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        return MealDto.toDto(mealById);
    }

    public Meal getByName(String name) {
        return this.mealRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));
    }

    public Page<Meal> findAll(Pageable pageable) {
        return this.mealRepository.findAll(pageable);
    }

    public MealDto updateById(Long id, MealRequest mealExists) {
        Meal mealById = this.mealRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Meal.class));

        if (StringUtils.isNotBlank(mealExists.getName())) {
            mealById.setName(mealExists.getName());
        }
        if (mealExists.getPortionWeight() != null) {
            mealById.setPortionWeight(mealExists.getPortionWeight());
        }
        if (mealExists.getPrice() != null) {
            mealById.setPrice(mealExists.getPrice());
        }

        Meal updatedMeal = mealRepository.save(mealById);
        return MealDto.toDto(updatedMeal);
    }

    public void deleteById(Long id) {
        this.mealRepository.deleteById(id);
    }
}

package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.DrinkDto;
import com.example.model.entity.Drink;
import com.example.model.request.DrinkRequest;
import com.example.repository.DrinkRepository;
import com.stripe.model.Price;
import com.stripe.model.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final StripeService stripeService;

    public DrinkDto create(DrinkRequest request) {
        Drink drinkToSave = DrinkRequest.toEntity(request);
        Drink createdDrink = this.drinkRepository.save(drinkToSave);

        Product stripeProduct = this.stripeService.createProduct(createdDrink.getName());
        Price stripePrice = this.stripeService.createPrice(stripeProduct.getId(), createdDrink.getPrice());

        return DrinkDto.toDto(createdDrink);
    }

    public DrinkDto getById(Long id) {
        Drink drinkById = this.drinkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Drink.class));

        return DrinkDto.toDto(drinkById);
    }

    public Drink getByName(String name) {
        return this.drinkRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(Drink.class));
    }

    public Page<Drink> findAll(Pageable pageable) {
        return this.drinkRepository.findAll(pageable);
    }

    public DrinkDto updateById(Long id, DrinkRequest drinkExists) {
        Drink drinkById = this.drinkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Drink.class));

        if (StringUtils.isNotBlank(drinkExists.getName())) {
            drinkById.setName(drinkExists.getName());
        }
        if (drinkExists.getPrice() != null) {
            drinkById.setPrice(drinkExists.getPrice());
        }

        Drink updatedDrink = drinkRepository.save(drinkById);
        return DrinkDto.toDto(updatedDrink);
    }

    public void deleteById(Long id) {
        this.drinkRepository.deleteById(id);
    }
}

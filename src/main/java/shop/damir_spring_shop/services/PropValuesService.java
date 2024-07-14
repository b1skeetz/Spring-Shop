package shop.damir_spring_shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.damir_spring_shop.models.PropValues;
import shop.damir_spring_shop.repositories.PropValuesRepository;

@RequiredArgsConstructor
@Component
public class PropValuesService {
    private final PropValuesRepository propValuesRepository;

    public void createPropValues(PropValues propValues){
        propValuesRepository.save(propValues);
    }
}

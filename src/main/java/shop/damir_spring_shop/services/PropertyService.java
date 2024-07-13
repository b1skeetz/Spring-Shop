package shop.damir_spring_shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.damir_spring_shop.models.Property;
import shop.damir_spring_shop.repositories.PropertyRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;

    public List<Property> getPropertiesByCategoryId(Long id){
        return propertyRepository.findPropertiesByCategory_Id(id);
    }

}

package sn.mycompany.monapptest.service.mapper;

import org.mapstruct.*;
import sn.mycompany.monapptest.domain.Category;
import sn.mycompany.monapptest.service.dto.CategoryDTO;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}

package sn.mycompany.monapptest.service.mapper;

import org.mapstruct.*;
import sn.mycompany.monapptest.domain.Author;
import sn.mycompany.monapptest.domain.Category;
import sn.mycompany.monapptest.domain.Post;
import sn.mycompany.monapptest.service.dto.AuthorDTO;
import sn.mycompany.monapptest.service.dto.CategoryDTO;
import sn.mycompany.monapptest.service.dto.PostDTO;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "author", source = "author", qualifiedByName = "authorId")
    PostDTO toDto(Post s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("authorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthorDTO toDtoAuthorId(Author author);
}

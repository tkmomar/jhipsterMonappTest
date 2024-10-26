package sn.mycompany.monapptest.service.mapper;

import org.mapstruct.*;
import sn.mycompany.monapptest.domain.Author;
import sn.mycompany.monapptest.service.dto.AuthorDTO;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {}

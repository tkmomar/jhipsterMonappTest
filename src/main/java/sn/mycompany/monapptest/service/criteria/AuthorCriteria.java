package sn.mycompany.monapptest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.mycompany.monapptest.domain.Author} entity. This class is used
 * in {@link sn.mycompany.monapptest.web.rest.AuthorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /authors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter lastname;

    private StringFilter firstname;

    private StringFilter style;

    private LongFilter postId;

    private Boolean distinct;

    public AuthorCriteria() {}

    public AuthorCriteria(AuthorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.lastname = other.optionalLastname().map(StringFilter::copy).orElse(null);
        this.firstname = other.optionalFirstname().map(StringFilter::copy).orElse(null);
        this.style = other.optionalStyle().map(StringFilter::copy).orElse(null);
        this.postId = other.optionalPostId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AuthorCriteria copy() {
        return new AuthorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public Optional<StringFilter> optionalLastname() {
        return Optional.ofNullable(lastname);
    }

    public StringFilter lastname() {
        if (lastname == null) {
            setLastname(new StringFilter());
        }
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public Optional<StringFilter> optionalFirstname() {
        return Optional.ofNullable(firstname);
    }

    public StringFilter firstname() {
        if (firstname == null) {
            setFirstname(new StringFilter());
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getStyle() {
        return style;
    }

    public Optional<StringFilter> optionalStyle() {
        return Optional.ofNullable(style);
    }

    public StringFilter style() {
        if (style == null) {
            setStyle(new StringFilter());
        }
        return style;
    }

    public void setStyle(StringFilter style) {
        this.style = style;
    }

    public LongFilter getPostId() {
        return postId;
    }

    public Optional<LongFilter> optionalPostId() {
        return Optional.ofNullable(postId);
    }

    public LongFilter postId() {
        if (postId == null) {
            setPostId(new LongFilter());
        }
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuthorCriteria that = (AuthorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(style, that.style) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, style, postId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLastname().map(f -> "lastname=" + f + ", ").orElse("") +
            optionalFirstname().map(f -> "firstname=" + f + ", ").orElse("") +
            optionalStyle().map(f -> "style=" + f + ", ").orElse("") +
            optionalPostId().map(f -> "postId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

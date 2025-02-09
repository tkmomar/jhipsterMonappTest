package sn.mycompany.monapptest.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorAllPropertiesEquals(Author expected, Author actual) {
        assertAuthorAutoGeneratedPropertiesEquals(expected, actual);
        assertAuthorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorAllUpdatablePropertiesEquals(Author expected, Author actual) {
        assertAuthorUpdatableFieldsEquals(expected, actual);
        assertAuthorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorAutoGeneratedPropertiesEquals(Author expected, Author actual) {
        assertThat(expected)
            .as("Verify Author auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorUpdatableFieldsEquals(Author expected, Author actual) {
        assertThat(expected)
            .as("Verify Author relevant properties")
            .satisfies(e -> assertThat(e.getLastname()).as("check lastname").isEqualTo(actual.getLastname()))
            .satisfies(e -> assertThat(e.getFirstname()).as("check firstname").isEqualTo(actual.getFirstname()))
            .satisfies(e -> assertThat(e.getStyle()).as("check style").isEqualTo(actual.getStyle()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAuthorUpdatableRelationshipsEquals(Author expected, Author actual) {
        // empty method
    }
}

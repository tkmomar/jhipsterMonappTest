package sn.mycompany.monapptest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.mycompany.monapptest.domain.CategoryTestSamples.*;
import static sn.mycompany.monapptest.domain.PostTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.mycompany.monapptest.web.rest.TestUtil;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void postTest() {
        Category category = getCategoryRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        category.addPost(postBack);
        assertThat(category.getPosts()).containsOnly(postBack);
        assertThat(postBack.getCategory()).isEqualTo(category);

        category.removePost(postBack);
        assertThat(category.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getCategory()).isNull();

        category.posts(new HashSet<>(Set.of(postBack)));
        assertThat(category.getPosts()).containsOnly(postBack);
        assertThat(postBack.getCategory()).isEqualTo(category);

        category.setPosts(new HashSet<>());
        assertThat(category.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getCategory()).isNull();
    }
}

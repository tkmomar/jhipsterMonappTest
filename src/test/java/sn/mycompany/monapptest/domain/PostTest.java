package sn.mycompany.monapptest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.mycompany.monapptest.domain.AuthorTestSamples.*;
import static sn.mycompany.monapptest.domain.CategoryTestSamples.*;
import static sn.mycompany.monapptest.domain.PostTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.mycompany.monapptest.web.rest.TestUtil;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void categoryTest() {
        Post post = getPostRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        post.setCategory(categoryBack);
        assertThat(post.getCategory()).isEqualTo(categoryBack);

        post.category(null);
        assertThat(post.getCategory()).isNull();
    }

    @Test
    void authorTest() {
        Post post = getPostRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        post.setAuthor(authorBack);
        assertThat(post.getAuthor()).isEqualTo(authorBack);

        post.author(null);
        assertThat(post.getAuthor()).isNull();
    }
}

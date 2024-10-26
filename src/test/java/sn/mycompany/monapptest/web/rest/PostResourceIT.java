package sn.mycompany.monapptest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.mycompany.monapptest.domain.PostAsserts.*;
import static sn.mycompany.monapptest.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.mycompany.monapptest.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.mycompany.monapptest.IntegrationTest;
import sn.mycompany.monapptest.domain.Author;
import sn.mycompany.monapptest.domain.Category;
import sn.mycompany.monapptest.domain.Post;
import sn.mycompany.monapptest.repository.PostRepository;
import sn.mycompany.monapptest.service.dto.PostDTO;
import sn.mycompany.monapptest.service.mapper.PostMapper;

/**
 * Integration tests for the {@link PostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMockMvc;

    private Post post;

    private Post insertedPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createEntity() {
        return new Post().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).dateCreation(DEFAULT_DATE_CREATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createUpdatedEntity() {
        return new Post().title(UPDATED_TITLE).content(UPDATED_CONTENT).dateCreation(UPDATED_DATE_CREATION);
    }

    @BeforeEach
    public void initTest() {
        post = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPost != null) {
            postRepository.delete(insertedPost);
            insertedPost = null;
        }
    }

    @Test
    @Transactional
    void createPost() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);
        var returnedPostDTO = om.readValue(
            restPostMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostDTO.class
        );

        // Validate the Post in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPost = postMapper.toEntity(returnedPostDTO);
        assertPostUpdatableFieldsEquals(returnedPost, getPersistedPost(returnedPost));

        insertedPost = returnedPost;
    }

    @Test
    @Transactional
    void createPostWithExistingId() throws Exception {
        // Create the Post with an existing ID
        post.setId(1L);
        PostDTO postDTO = postMapper.toDto(post);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        post.setTitle(null);

        // Create the Post, which fails.
        PostDTO postDTO = postMapper.toDto(post);

        restPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPosts() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }

    @Test
    @Transactional
    void getPost() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc
            .perform(get(ENTITY_API_URL_ID, post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    void getPostsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        Long id = post.getId();

        defaultPostFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPostFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPostFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPostsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where title equals to
        defaultPostFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPostsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where title in
        defaultPostFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPostsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where title is not null
        defaultPostFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllPostsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where title contains
        defaultPostFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPostsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where title does not contain
        defaultPostFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllPostsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where content equals to
        defaultPostFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllPostsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where content in
        defaultPostFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllPostsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where content is not null
        defaultPostFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllPostsByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where content contains
        defaultPostFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllPostsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where content does not contain
        defaultPostFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation equals to
        defaultPostFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation in
        defaultPostFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation is not null
        defaultPostFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation is greater than or equal to
        defaultPostFiltering(
            "dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION,
            "dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation is less than or equal to
        defaultPostFiltering(
            "dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION,
            "dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation is less than
        defaultPostFiltering("dateCreation.lessThan=" + UPDATED_DATE_CREATION, "dateCreation.lessThan=" + DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPostsByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        // Get all the postList where dateCreation is greater than
        defaultPostFiltering("dateCreation.greaterThan=" + SMALLER_DATE_CREATION, "dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllPostsByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            postRepository.saveAndFlush(post);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        post.setCategory(category);
        postRepository.saveAndFlush(post);
        Long categoryId = category.getId();
        // Get all the postList where category equals to categoryId
        defaultPostShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the postList where category equals to (categoryId + 1)
        defaultPostShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllPostsByAuthorIsEqualToSomething() throws Exception {
        Author author;
        if (TestUtil.findAll(em, Author.class).isEmpty()) {
            postRepository.saveAndFlush(post);
            author = AuthorResourceIT.createEntity();
        } else {
            author = TestUtil.findAll(em, Author.class).get(0);
        }
        em.persist(author);
        em.flush();
        post.setAuthor(author);
        postRepository.saveAndFlush(post);
        Long authorId = author.getId();
        // Get all the postList where author equals to authorId
        defaultPostShouldBeFound("authorId.equals=" + authorId);

        // Get all the postList where author equals to (authorId + 1)
        defaultPostShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    private void defaultPostFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPostShouldBeFound(shouldBeFound);
        defaultPostShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));

        // Check, that the count call also returns 1
        restPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPost() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost.title(UPDATED_TITLE).content(UPDATED_CONTENT).dateCreation(UPDATED_DATE_CREATION);
        PostDTO postDTO = postMapper.toDto(updatedPost);

        restPostMockMvc
            .perform(put(ENTITY_API_URL_ID, postDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostToMatchAllProperties(updatedPost);
    }

    @Test
    @Transactional
    void putNonExistingPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(put(ENTITY_API_URL_ID, postDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostWithPatch() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the post using partial update
        Post partialUpdatedPost = new Post();
        partialUpdatedPost.setId(post.getId());

        partialUpdatedPost.title(UPDATED_TITLE).dateCreation(UPDATED_DATE_CREATION);

        restPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPost))
            )
            .andExpect(status().isOk());

        // Validate the Post in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPost, post), getPersistedPost(post));
    }

    @Test
    @Transactional
    void fullUpdatePostWithPatch() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the post using partial update
        Post partialUpdatedPost = new Post();
        partialUpdatedPost.setId(post.getId());

        partialUpdatedPost.title(UPDATED_TITLE).content(UPDATED_CONTENT).dateCreation(UPDATED_DATE_CREATION);

        restPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPost))
            )
            .andExpect(status().isOk());

        // Validate the Post in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostUpdatableFieldsEquals(partialUpdatedPost, getPersistedPost(partialUpdatedPost));
    }

    @Test
    @Transactional
    void patchNonExistingPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        post.setId(longCount.incrementAndGet());

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Post in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePost() throws Exception {
        // Initialize the database
        insertedPost = postRepository.saveAndFlush(post);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the post
        restPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, post.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Post getPersistedPost(Post post) {
        return postRepository.findById(post.getId()).orElseThrow();
    }

    protected void assertPersistedPostToMatchAllProperties(Post expectedPost) {
        assertPostAllPropertiesEquals(expectedPost, getPersistedPost(expectedPost));
    }

    protected void assertPersistedPostToMatchUpdatableProperties(Post expectedPost) {
        assertPostAllUpdatablePropertiesEquals(expectedPost, getPersistedPost(expectedPost));
    }
}

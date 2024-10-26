package sn.mycompany.monapptest.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.mycompany.monapptest.domain.Post;
import sn.mycompany.monapptest.repository.PostRepository;
import sn.mycompany.monapptest.service.PostService;
import sn.mycompany.monapptest.service.dto.PostDTO;
import sn.mycompany.monapptest.service.mapper.PostMapper;

/**
 * Service Implementation for managing {@link sn.mycompany.monapptest.domain.Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        LOG.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public PostDTO update(PostDTO postDTO) {
        LOG.debug("Request to update Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public Optional<PostDTO> partialUpdate(PostDTO postDTO) {
        LOG.debug("Request to partially update Post : {}", postDTO);

        return postRepository
            .findById(postDTO.getId())
            .map(existingPost -> {
                postMapper.partialUpdate(existingPost, postDTO);

                return existingPost;
            })
            .map(postRepository::save)
            .map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> findOne(Long id) {
        LOG.debug("Request to get Post : {}", id);
        return postRepository.findById(id).map(postMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}

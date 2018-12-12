package org.superbiz.moviefun.moviesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class MoviesClient {

    private String moviesUrl;
    private RestOperations restOperations;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ParameterizedTypeReference<List<MovieInfo>> movieListType = new ParameterizedTypeReference<List<MovieInfo>>() {
    };

    public MoviesClient(String moviesUrl, RestOperations restOperations) {
        this.moviesUrl = moviesUrl;
        this.restOperations = restOperations;
    }

    public void addMovie(MovieInfo movie) {
        logger.debug("Creating movie with title {}, and year {}", movie.getTitle(), movie.getYear());

        restOperations.postForEntity(moviesUrl,movie,MovieInfo.class);
    }

    public void updateMovie(MovieInfo movie) {

//        entityManager.merge(movie);
        restOperations.postForEntity(moviesUrl,movie,MovieInfo.class);
    }

    @Transactional
    public void deleteMovie(MovieInfo movie) {
//        entityManager.remove(movie);
        restOperations.delete(moviesUrl,movie,MovieInfo.class);
    }


    @Transactional
    public void deleteMovieId(long id) {
//        MovieInfo movie = entityManager.find(Movie.class, id);
//        deleteMovie(movie);
        restOperations.delete(moviesUrl+"/"+id);
    }

    public List<MovieInfo> getMovies() {
        /*CriteriaQuery<MovieInfo> cq = entityManager.getCriteriaBuilder().createQuery(MovieInfo.class);
        cq.select(cq.from(MovieInfo.class));
        return entityManager.createQuery(cq).getResultList();*/
        return restOperations.exchange(moviesUrl, GET, null, movieListType).getBody();
    }

    public List<MovieInfo> findAll(int firstResult, int maxResults) {
       /* CriteriaQuery<Movie> cq = entityManager.getCriteriaBuilder().createQuery(Movie.class);
        cq.select(cq.from(Movie.class));
        TypedQuery<Movie> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();*/
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("start", firstResult)
                .queryParam("pageSize", maxResults);

        return restOperations.exchange(moviesUrl, GET, null, movieListType).getBody();
    }

    public int countAll() {
        /*CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<Movie> rt = cq.from(Movie.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        return (q.getSingleResult()).intValue();*/

        return restOperations.getForObject(moviesUrl+"/count",Integer.class);

    }

    public int count(String field, String searchTerm) {
        /*CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Movie> root = cq.from(Movie.class);
        EntityType<Movie> type = entityManager.getMetamodel().entity(Movie.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.select(qb.count(root));
        cq.where(condition);

        return entityManager.createQuery(cq).getSingleResult().intValue();*/

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl + "/count")
                .queryParam("field", field)
                .queryParam("key", searchTerm);

        return restOperations.getForObject(builder.toUriString(), Integer.class);

    }

    public List<MovieInfo> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        /*CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = qb.createQuery(Movie.class);
        Root<Movie> root = cq.from(Movie.class);
        EntityType<Movie> type = entityManager.getMetamodel().entity(Movie.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.where(condition);
        TypedQuery<Movie> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();*/

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("field", field)
                .queryParam("key", searchTerm)
                .queryParam("start", firstResult)
                .queryParam("pageSize", maxResults);

        return restOperations.exchange(moviesUrl, GET, null, movieListType).getBody();
    }
}

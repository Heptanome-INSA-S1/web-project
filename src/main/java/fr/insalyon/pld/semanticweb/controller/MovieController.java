package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.model.persistence.MovieModel;
import fr.insalyon.pld.semanticweb.repositories.entities.Movie;
import fr.insalyon.pld.semanticweb.repositories.mapper.MovieMapper;
import fr.insalyon.pld.semanticweb.repositories.services.MovieRepository;
import fr.insalyon.pld.semanticweb.repositories.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
public class MovieController {

  @Autowired
  MovieRepository movieRepository;
  @Autowired
  MovieMapper movieMapper;

  @RequestMapping(value = "/movies/{uri}", produces = "application/json")
  public @ResponseBody
  MovieModel getMovie(@PathVariable String uri) {

    Optional<Movie> filmOptional = movieRepository.findById(uri);

    if (filmOptional.isPresent()) {
      return movieMapper.entityToFullModel(filmOptional.get());
    } else {
      throw new RuntimeException("Impossible to reach this uri: " + uri + " at the resource path " + SPARQLRepository.HTTP_DBPEDIA_ORG);
    }
  }

  @RequestMapping("/movies")
  public @ResponseBody
  List<MovieModel> index(@RequestParam(value = "name", required = false) String query,
                    @RequestParam(value = "short", required = false, defaultValue = "true") String isShort) {
    if (null == query) {
      return movieMapper.entitiesToLightModels(movieRepository.findAll());
    } else {
      return movieMapper.entitiesToLightModels(movieRepository.findByName(query));
    }
  }

}
